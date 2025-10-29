package com.leecoder.stockchart.model.stock

sealed class IncomingMessage {
    data class Subscribe(val body: SubscribeResponse) : IncomingMessage()
    data class PayLoad(val raw: String) : IncomingMessage()
}