package com.leecoder.stockchart.model.network

sealed class WebSocketState {
    data object Connecting : WebSocketState()
    data object Connected : WebSocketState()
    data class Error(val message: String) : WebSocketState()
}