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

    lateinit var latestKey: String
    lateinit var latestIv: String

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
                Log.i("heesang", "onMessage (text) -> ${text}")

                // 시도: JSON 파싱 (구독성공, PINGPONG, 암호화 표기 등)
                try {
                    val j = JSONObject(text)
                    val header = j.optJSONObject("header")
                    val trId = header?.optString("tr_id")
                    val encryptFlag = header?.optString("encrypt") // "Y" or "N" or null

                    // 1) PINGPONG 처리 (서버가 보내면 문서대로 PONG 응답)
                    if (trId == "PINGPONG") {
                        val now = SimpleDateFormat("yyyyMMddHHmmss", Locale.KOREA).format(Date())
                        val pong = JSONObject().put("header", JSONObject().put("tr_id", "PINGPONG").put("datetime", now))
                        webSocket.send(pong.toString())
                        Log.i("heesang","SENT PONG -> $pong")
                        return
                    }

                    // 2) SUBSCRIBE SUCCESS 처리: iv/key 저장
                    val body = j.optJSONObject("body")
                    val msg1 = body?.optString("msg1")
                    if (msg1 == "SUBSCRIBE SUCCESS") {
                        val output = body.optJSONObject("output")
                        val iv = output?.optString("iv")
                        val key = output?.optString("key")
                        if (!iv.isNullOrEmpty() && !key.isNullOrEmpty()) {
                            latestIv = iv
                            latestKey = key
                            Log.i("heesang","Got iv/key -> iv:$iv key:$key")
                        }
                        return
                    }

                    // 3) 암호화된 데이터(예: body.data 등) 처리
                    if (encryptFlag == "Y" || j.optJSONObject("body")?.has("data") == true) {
                        // 케이스별로 필드명이 다를 수 있음: 예시로 body.data 사용
                        val enc = j.optJSONObject("body")?.optString("data")
                        if (!enc.isNullOrEmpty() && latestKey != null && latestIv != null) {
                            try {
                                val plain = decryptAES256(enc, latestKey!!, latestIv!!)
                                Log.i("heesang", "DECRYPTED -> $plain")
                                // 이제 plain 을 ^ 구분자로 파싱
                            } catch (e: Exception) {
                                Log.e("heesang", "decrypt failed: ${e.message}")
                            }
                        }
                        return
                    }
                } catch (e: JSONException) {
                    // JSON이 아닐 경우 pipe 포맷 등 다른 포맷 파싱 시도
                    if (text.isNotEmpty() && (text[0] == '0' || text[0] == '1')) {
                        val parts = text.split("|")
                        if (parts.size >= 4) {
                            val trid = parts[1]
                            val content = parts[3]
                            Log.i("heesang", "PIPE PARSED trid=$trid content=$content")
                        }
                    }
                }
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                super.onFailure(webSocket, t, response)
                Log.e("heesang", "onFailure -> ${t.message}")
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