package com.leecoder.stockchart.domain.usecase

import com.leecoder.data.repository.room.RoomDatabaseRepository
import com.leecoder.data.repository.WebSocketRepository
import com.leecoder.stockchart.model.network.WebSocketState
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

class ReconnectWebSocketUseCase @Inject constructor(
    private val webSocketRepository: WebSocketRepository,
    private val roomDatabaseRepository: RoomDatabaseRepository,
) {

    suspend operator fun invoke() = coroutineScope {
        webSocketRepository.connect(
            "ws://ops.koreainvestment.com:21000/tryitout/H0STCNT0",
        )

        webSocketRepository.connectedWebSocketSession
            .onEach { state ->
                if (state is WebSocketState.Connected) {
                    val subscribedStocks = roomDatabaseRepository.getAllSubscribedStocks().first()
                    webSocketRepository.initSubscribe(subscribedStocks.map { it.code }) // DB 저장 목록으로 WebSocket 세션 연결.
                }
            }.launchIn(this@coroutineScope)
    }
}