package com.leecoder.stockchart.model.network

sealed class WebSocketState {
    data object Disconnected : WebSocketState()
    data object Connected : WebSocketState()
    data class Error(val message: String) : WebSocketState()
}