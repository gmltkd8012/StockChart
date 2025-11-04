package com.leecoder.stockchart.domain.usecase

import android.util.Log
import com.leecoder.data.repository.KsInvestmentRepository
import com.leecoder.data.repository.RoomDatabaseRepository
import com.leecoder.stockchart.model.bollinger.LiveBollingerData
import com.leecoder.stockchart.model.room.BollingerData
import com.leecoder.stockchart.model.stock.TimeItemChartPriceData
import com.leecoder.stockchart.model.stock.TimeItemChartPriceWithCode
import com.leecoder.stockchart.util.calculator.BollingerCalculator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.invoke
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetLiveBollingersUseCase @Inject constructor(
    private val roomDatabaseRepository: RoomDatabaseRepository,
    private val ksInvestmentRepository: KsInvestmentRepository,
) {

    private val calculator = BollingerCalculator()

    suspend operator fun invoke(iscds: List<String>): List<LiveBollingerData> = coroutineScope {
        Log.e("lynn", "UseCase Coroutine Scope = ${this@coroutineScope.coroutineContext}")

        val chartPricesDeferred = iscds.map { iscd ->
           async {
                try {
                    val chartPrice =  ksInvestmentRepository.getTimeItemChartPrice(iscd).first()

                    TimeItemChartPriceWithCode(
                        code = iscd,
                        prices = chartPrice
                    )
                } catch (e: Exception) {
                    TimeItemChartPriceWithCode (
                        code = iscd,
                        prices = emptyList()
                    )
                }
            }
        }

        val chartPricesWithCode = chartPricesDeferred.awaitAll()

        chartPricesWithCode.map { chartPrice ->
            val recentTime = chartPrice.prices.firstOrNull()?.stckCntgHour?.toInt()
            val prices = chartPrice.prices.map { it.stckPrpr.toInt() }

            val bollingerData = withContext(Dispatchers.Default) {
                calculator.calculate(
                    code = chartPrice.code,
                    prices = prices
                )
            }

            LiveBollingerData(
                recentTime = recentTime ?: 0,
                bollinger = bollingerData
            )
        }
    }



}