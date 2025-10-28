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
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
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

class WebSocketDataSourceImpl @Inject constructor(
    @Named("websocket") private val client: OkHttpClient,
    private val webSocketApi: WebSocketApi,
): WebSocketDataSource {

    private var webSocket: WebSocket? = null
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    private val _channelStockTick: Channel<StockTick> = Channel<StockTick>(Channel.UNLIMITED)
    override val channelStockTick: ReceiveChannel<StockTick>
        get() = _channelStockTick

    override fun connect(url: String) {
        val request = Request.Builder()
            .url(url)
            .build()

        webSocket = client.newWebSocket(request, object : WebSocketListener() {
            override fun onOpen(webSocket: WebSocket, response: Response) {
                super.onOpen(webSocket, response)
                sendMessage()
            }

            override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
                super.onMessage(webSocket, bytes)
                Log.i("heesang", "onMessage (bytes) -> $bytes")
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                super.onMessage(webSocket, text)

                scope.launch {
                    if (text.isJsonHuristic()) {
                        val response = Json.decodeFromString<SubscribeResponse>(text)
                        Log.i("heesang", "onMessage (response) -> ${response}")
                        return@launch
                    }

                    val parts = text.split("\\|".toRegex())
                    require(parts.size >= 4)
                    val stockTick: StockTick = StockTickParser.parse(parts[parts.lastIndex])
                    _channelStockTick.send(stockTick)
                }

            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                super.onFailure(webSocket, t, response)
                Log.e("heesang", "onFailure -> [code]: ${t.cause} / [msg]: ${t.message}")
                Log.e("heesang", "onFailure -> [response] ${response}")
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

    override fun sendMessage() {
        val header = WebSocketApprovalHeader(
            approvalKey = "",
            custType = "P",
            trType = "1",
            contentType = "utf-8",
        )

        val body = WebSocketApprovalBody(
            input = WebSocketApprovalInput(
                id = "H0STCNT0",
                key = "005930",
            )
        )

        Log.d("heesang", "sendMessage Text -> ${Json.encodeToString(WebSocketApproval.serializer(), WebSocketApproval(header, body))} ")

        webSocket?.send( Json.encodeToString(WebSocketApproval.serializer(), WebSocketApproval(header, body)))
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