package com.leecoder.stockchart.domain.usecase

import com.leecoder.data.repository.WebSocketRepository
import com.leecoder.stockchart.model.stock.StockTick
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.consumeEach
import javax.inject.Inject

class GetStockTickUseCase @Inject constructor(
    private val webSocketRepository: WebSocketRepository,
) {


//    suspend operator fun invoke(): ReceiveChannel<List<StockTick>> {
//        webSocketRepository.channelStockTick.consumeEach { tick ->
//            when (tick.mkscShrnIscd) {
//                "005930" -> sendList.add(tick) // 삼성전자
//                "000660" -> sendList.add(tick) // SK하이닉스
//            }
//
//        }
//    }

}