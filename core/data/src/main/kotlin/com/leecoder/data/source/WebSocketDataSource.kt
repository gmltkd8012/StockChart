package com.leecoder.data.source

import okhttp3.WebSocketListener

interface WebSocketDataSource {
    fun connect(url: String)
    fun disconnect()
    fun sendMessage()
}