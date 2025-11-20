package com.leecoder.stockchart.domain.usecase.overseas

import android.util.Log
import com.leecoder.data.repository.KisInvestmentOverseasRepository
import com.leecoder.data.repository.room.RoomDatabaseRepository
import com.leecoder.data.repository.WebSocketRepository
import com.leecoder.stockchart.model.stock.SubscribedStockData
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.first
import javax.inject.Inject

/**
 * 해외 주식 현재체결가 가져오는 UseCase
 */
class SaveOverseasStockCurrentPriceUseCase @Inject constructor(
    private val webSocketRepository: WebSocketRepository,
    private val roomDatabaseRepository: RoomDatabaseRepository,
    private val kisInvestmentOverseasRepository: KisInvestmentOverseasRepository,
) {

    suspend operator fun invoke(vararg params: SubscribedStockData) = coroutineScope {
        val deferred = params.map { stock ->
            async() {
                try {
                    val currentPrice = kisInvestmentOverseasRepository.getCurrentPriceNasdaq(
                        auth = "",
                        excd = "NAS",
                        symb = stock.code,
                    ).first()

                    Log.e("lynn", "price -> ${currentPrice.last.replace(",", "")}")
                    SubscribedStockData(
                        code = stock.code,
                        name = stock.name,
                        price = currentPrice.last.replace(",", ""),
                    )

                } catch (e: Exception) {
                    Log.e("lynn", "error -> $e")
                    SubscribedStockData()
                }
            }
        }

        roomDatabaseRepository.subscribeStock(deferred.awaitAll())
    }
}