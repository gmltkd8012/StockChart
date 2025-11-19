package com.leecoder.stockchart.domain.usecase

import android.util.Log
import com.leecoder.data.repository.KsInvestmentRepository
import com.leecoder.data.repository.RoomDatabaseRepository
import com.leecoder.stockchart.model.stock.CurrentPriceData
import com.leecoder.stockchart.model.stock.SubscribedStockData
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.supervisorScope
import javax.inject.Inject

class SaveStockWithCurrentPriceUseCase @Inject constructor(
    private val ksInvestmentRepository: KsInvestmentRepository,
    private val roomDatabaseRepository: RoomDatabaseRepository,
) {

    suspend operator fun invoke(vararg params: SubscribedStockData) = coroutineScope {
        val deferred = params.map { stock ->
            async {
                try {
                    val currentPrices = ksInvestmentRepository.getCurrentPrice(iscd = stock.code).first()

                    SubscribedStockData(
                        code = stock.code,
                        name = stock.name,
                        price = currentPrices.stckPrpr
                    )
                } catch (e: Exception) {
                    SubscribedStockData()
                }
            }
        }

        roomDatabaseRepository.subscribeStock(deferred.awaitAll())
    }
}