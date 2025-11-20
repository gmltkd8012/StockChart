package com.leecoder.stockchart.domain.usecase

import android.util.Log
import com.leecoder.data.repository.KsInvestmentRepository
import com.leecoder.data.repository.room.RoomDatabaseRepository
import com.leecoder.stockchart.model.room.BollingerData
import com.leecoder.stockchart.util.calculator.BollingerCalculator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SaveAllBollingersUseCase @Inject constructor(
    private val ksInvestmentRepository: KsInvestmentRepository,
    private val roomDatabaseRepository: RoomDatabaseRepository,
) {
    private val calculator = BollingerCalculator()

    suspend operator fun invoke(): Boolean = coroutineScope {
        try {
            val subscribeStocks = roomDatabaseRepository.getAllSubscribedStocks().first()

            val async = subscribeStocks.map { stock ->
                async {
                    try {
                        val prices = ksInvestmentRepository.getDailyPrice(
                            iscd = stock.code,
                            periodCode = "D",
                        ).first().map { it.stckClpr.toInt() }

                        withContext(Dispatchers.Default) {
                            calculator.calculate(
                                code = stock.code,
                                name = stock.name,
                                prices = prices
                            )
                        }
                    } catch (e: Exception) {
                        BollingerData(
                            code = stock.code,
                            name = stock.name,
                            upper = -1,
                            middle = -1,
                            lower = -1
                        )
                    }
                }
            }

            val result = async.awaitAll()

            withContext(Dispatchers.IO) {
                roomDatabaseRepository.insertAllBollingers(result)
            }

            return@coroutineScope true
        } catch (e: Exception) {
            Log.e("[LeeCoder]", "Insert Error -> ${e.message}")
            return@coroutineScope false
        }
    }
}