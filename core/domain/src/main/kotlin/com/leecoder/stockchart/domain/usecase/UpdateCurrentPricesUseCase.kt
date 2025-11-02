package com.leecoder.stockchart.domain.usecase

import android.util.Log
import com.leecoder.data.repository.KsInvestmentRepository
import com.leecoder.data.repository.RegistedStockRepository
import com.leecoder.data.repository.RoomDatabaseRepository
import com.leecoder.stockchart.model.stock.CurrentPriceData
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.supervisorScope
import javax.inject.Inject

class UpdateCurrentPricesUseCase @Inject constructor(
    private val ksInvestmentRepository: KsInvestmentRepository,
    private val roomDatabaseRepository: RoomDatabaseRepository,
    private val registedStockRepository: RegistedStockRepository,
) {

    suspend operator fun invoke() = supervisorScope {
        val subscribeStocks = registedStockRepository.getRegistedStock().first()

        val async = subscribeStocks.map {
            async {
                try {
                    val currentPrices = ksInvestmentRepository.getCurrentPrice(
                        iscd = it.code,
                    ).first()

                    CurrentPriceData(
                        stckPrpr = currentPrices.stckPrpr
                    )
                } catch (e: Exception) {
                    CurrentPriceData(
                        stckPrpr = "-1"
                    )
                }
            }
        }

        val aa = async.awaitAll()
        Log.e("lynn", "invoke: $aa")
    }
}