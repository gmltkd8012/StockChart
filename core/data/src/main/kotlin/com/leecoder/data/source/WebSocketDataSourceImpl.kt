package com.leecoder.data.source

import android.util.Base64
import android.util.Log
import com.leecoder.data.repository.WebSocketRepository
import com.leecoder.network.api.WebSocketApi
import com.leecoder.network.entity.WebSocketApproval
import com.leecoder.network.entity.WebSocketApprovalBody
import com.leecoder.network.entity.WebSocketApprovalHeader
import com.leecoder.network.entity.WebSocketApprovalInput
import com.leecoder.network.entity.WebSocketRequest
import com.leecoder.stockchart.datastore.repository.DataStoreRepository
import com.leecoder.stockchart.model.network.WebSocketState
import com.leecoder.stockchart.model.stock.HeartBeatResponse
import com.leecoder.stockchart.model.stock.StockTick
import com.leecoder.stockchart.model.stock.SubscribeResponse
import com.leecoder.stockchart.util.extension.isJsonHuristic
import com.leecoder.stockchart.util.parser.StockTickParser
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import okio.ByteString
import org.json.JSONException
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

@Singleton
class WebSocketDataSourceImpl @Inject constructor(
    @Named("websocket") private val client: OkHttpClient,
    private val webSocketApi: WebSocketApi,
    private val dataStoreRepository: DataStoreRepository,
): WebSocketDataSource {

    companion object {
        const val PING_PONG = "PINGPONG" // 웹소켓 하트비트 체크
        const val SUBSCRIBE_CODE = "1" // 등록: "1"
        const val UNSUBSCRIBE_CODE = "2" // 해제: "2"

    }

    private var webSocket: WebSocket? = null
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    private val _channelStockTick: Channel<StockTick> = Channel<StockTick>(Channel.UNLIMITED)
    override val channelStockTick: ReceiveChannel<StockTick>
        get() = _channelStockTick

    private val _connectedWebSocketSession: MutableStateFlow<WebSocketState> = MutableStateFlow(WebSocketState.Connecting)
    override val connectedWebSocketSession: Flow<WebSocketState>
        get() = _connectedWebSocketSession.asStateFlow()

    override fun connect(url: String) {
        val request = Request.Builder()
            .url(url)
            .build()

        webSocket = client.newWebSocket(request, object : WebSocketListener() {
            override fun onOpen(webSocket: WebSocket, response: Response) {
                super.onOpen(webSocket, response)
                scope.launch {
                    _connectedWebSocketSession.emit(WebSocketState.Connected)
                }
            }

            override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
                super.onMessage(webSocket, bytes)
                Log.i("heesang", "onMessage (bytes) -> $bytes")
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                super.onMessage(webSocket, text)

                Log.e("heesang", "onMessage: -> $text")

                scope.launch {
                    if (text.isJsonHuristic()) { // Json 인 경우, 파싱 작업 진행 초기 데이터
                        val json = Json { ignoreUnknownKeys = true }
                        val root = json.parseToJsonElement(text).jsonObject
                        val header = root["header"]?.jsonObject
                        val trId = header?.get("tr_id")?.jsonPrimitive?.contentOrNull

                        trId?.let { id ->
                            if (id == PING_PONG) {
                                val formatter = SimpleDateFormat("yyyyMMddHHmmss", Locale.KOREA)
                                val currentTime = formatter.format(Date(System.currentTimeMillis()))

                                val response = HeartBeatResponse(
                                    header = HeartBeatResponse.HeartBeatHeader(
                                        trId = trId,
                                        dateTime = currentTime,
                                    )
                                )

                                webSocket.send(json.encodeToString(response))
                            } else {
//                                val response = json.decodeFromString<SubscribeResponse>(text)
//                                Log.i("heesang", "onMessage (response) -> ${response}")
                            }
                        }

                        return@launch
                    }

                    val parts = text.split("\\|".toRegex())
                    require(parts.size >= 4)

                    StockTickParser.parse(parts[parts.lastIndex])
                        .forEach { stockTick ->
                            _channelStockTick.send(stockTick)
                        }
                }

            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                super.onFailure(webSocket, t, response)
                Log.e("heesang", "onFailure: ${t.message} / ${t.cause} / $response ")
                scope.launch {
                    _connectedWebSocketSession.emit(WebSocketState.Error(t.message ?: "Unknown Error"))
                }
            }

            override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
                super.onClosed(webSocket, code, reason)
                Log.e("heesang", "onClosed")
            }

            override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
                super.onClosing(webSocket, code, reason)
                Log.e("heesang", "onClosed")
            }
        })
    }

    override fun disconnect() {
        webSocket?.cancel()
        webSocket = null
    }

    override suspend fun initSubscribe(symbols: List<String>) {
        val approvalKey = dataStoreRepository.currentKrInvestmentWebSocket.first()

        symbols.forEach { symbol ->
            requestData(approvalKey, symbol, SUBSCRIBE_CODE)
        }
    }

    override suspend fun subscribe(symbol: String) {
        val approvalKey = dataStoreRepository.currentKrInvestmentWebSocket.first()
        requestData(approvalKey, symbol, SUBSCRIBE_CODE)
    }

    override suspend fun unSubscribe(symbol: String) {
        val approvalKey = dataStoreRepository.currentKrInvestmentWebSocket.first()
        requestData(approvalKey, symbol, UNSUBSCRIBE_CODE)
    }

    private fun requestData(approvalKey:String?, symbol: String, trType: String) {
        if (approvalKey == null) return

        val header = WebSocketApprovalHeader(
            approvalKey = approvalKey,
            custType = "P",
            trType = trType,
            contentType = "utf-8",
        )

        val body = WebSocketApprovalBody(
            input = WebSocketApprovalInput(
                id = "H0STCNT0",
                key = symbol,
            )
        )

        webSocket?.send(Json.encodeToString(WebSocketApproval.serializer(), WebSocketApproval(header, body)))
    }

    fun decryptAES256(encryptedBase64: String, keyString: String, ivString: String): String {
        // 1. Base64 -> ByteArray
        val encryptedBytes = Base64.decode(encryptedBase64, Base64.DEFAULT)
        val keyBytes = keyString.toByteArray(Charsets.UTF_8)   // 서버 제공 Key
        val ivBytes = ivString.toByteArray(Charsets.UTF_8)     // 서버 제공 IV

        // 2. SecretKeySpec & IvParameterSpec
        val keySpec = SecretKeySpec(keyBytes, "AES")
        val ivSpec = IvParameterSpec(ivBytes)

        // 3. Cipher 초기화
        val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding") // AES256 + CBC + 패딩
        cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec)

        // 4. 복호화
        val decryptedBytes = cipher.doFinal(encryptedBytes)
        return String(decryptedBytes, Charsets.UTF_8)
    }
}