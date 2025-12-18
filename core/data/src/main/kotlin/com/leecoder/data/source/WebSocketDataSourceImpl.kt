package com.leecoder.data.source

import android.util.Base64
import android.util.Log
import androidx.compose.ui.input.key.Key.Companion.D
import com.leecoder.data.repository.WebSocketRepository
import com.leecoder.network.api.WebSocketApi
import com.leecoder.network.entity.WebSocketApproval
import com.leecoder.network.entity.WebSocketApprovalBody
import com.leecoder.network.entity.WebSocketApprovalHeader
import com.leecoder.network.entity.WebSocketApprovalInput
import com.leecoder.network.entity.WebSocketRequest
import com.leecoder.stockchart.appconfig.config.AppConfig
import com.leecoder.stockchart.datastore.repository.DataStoreRepository
import com.leecoder.stockchart.model.network.WebSocketState
import com.leecoder.stockchart.model.stock.HeartBeatResponse
import com.leecoder.stockchart.model.stock.NasdaqTick
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
    private val appConfig: AppConfig,
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

    // 현재 구독 중인 심볼 목록 (트레이드 코드 변경 시 재구독용)
    private val subscribedSymbols = mutableSetOf<String>()

    private val _channelStockTick: Channel<NasdaqTick> = Channel<NasdaqTick>(Channel.UNLIMITED)
    override val channelStockTick: ReceiveChannel<NasdaqTick>
        get() = _channelStockTick

    private val _connectedWebSocketSession: MutableStateFlow<WebSocketState> = MutableStateFlow(WebSocketState.Disconnected)
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

                    StockTickParser.parseNasdaq(parts[parts.lastIndex])
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
                Log.e("[Leecoder]", "WebSocket Session onClosed")

                scope.launch {
                    _connectedWebSocketSession.emit(WebSocketState.Disconnected)
                }
            }

            override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
                super.onClosing(webSocket, code, reason)
                Log.e("[Leecoder]", "WebSocket Session onClosing")
            }
        })
    }

    override fun disconnect() {
        webSocket?.cancel()
        webSocket = null
    }

    override suspend fun initSubscribe(symbols: List<String>) {
        val approvalKey = dataStoreRepository.currentKrInvestmentWebSocket.first()
        val tradeCode = dataStoreRepository.currentNasdaqTradeCode.first()

        symbols.forEach { symbol ->
            subscribedSymbols.add(symbol)
            requestData(approvalKey, tradeCode, symbol, SUBSCRIBE_CODE)
        }
    }

    override suspend fun subscribe(symbol: String) {
        val approvalKey = dataStoreRepository.currentKrInvestmentWebSocket.first()
        val tradeCode = dataStoreRepository.currentNasdaqTradeCode.first()
        subscribedSymbols.add(symbol)
        requestData(approvalKey, tradeCode, symbol, SUBSCRIBE_CODE)
    }

    override suspend fun unSubscribe(symbol: String) {
        val approvalKey = dataStoreRepository.currentKrInvestmentWebSocket.first()
        val tradeCode = dataStoreRepository.currentNasdaqTradeCode.first()
        subscribedSymbols.remove(symbol)
        requestData(approvalKey, tradeCode, symbol, UNSUBSCRIBE_CODE)
    }

    /**
     * 트레이드 코드 변경 시 기존 구독을 해제하고 새 코드로 재구독
     * @param oldCode 이전 트레이드 코드
     * @param newCode 새로운 트레이드 코드
     */
    override suspend fun refreshSubscriptionsWithNewTradeCode(oldCode: String, newCode: String) {
        val approvalKey = dataStoreRepository.currentKrInvestmentWebSocket.first()

        // 기존 구독 해제
        subscribedSymbols.forEach { symbol ->
            requestData(approvalKey, oldCode, symbol, UNSUBSCRIBE_CODE)
        }

        // 새 코드로 재구독
        subscribedSymbols.forEach { symbol ->
            requestData(approvalKey, newCode, symbol, SUBSCRIBE_CODE)
        }
    }

    /**
     * 현재 구독 중인 심볼 목록 반환
     */
    override fun getSubscribedSymbols(): Set<String> = subscribedSymbols.toSet()

    private fun requestData(
        approvalKey: String?,
        tradeCode: String,
        symbol: String,
        trType: String
    ) {
        if (approvalKey == null) return

        val header = WebSocketApprovalHeader(
            approvalKey = approvalKey,
            custType = "P",
            trType = trType,
            contentType = "utf-8",
        )

        val body = WebSocketApprovalBody(
            input = WebSocketApprovalInput(
                id = appConfig.nasdaqCode,
                key = tradeCode + symbol,
            )
        )

        webSocket?.send(Json.encodeToString(WebSocketApproval.serializer(), WebSocketApproval(header, body)))
    }
}