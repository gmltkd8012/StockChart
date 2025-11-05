package com.leecoder.stockchart.domain.usecase

import com.leecoder.data.repository.KsInvestmentRepository
import com.leecoder.data.repository.RoomDatabaseRepository
import com.leecoder.stockchart.model.stock.TimeItemChartPriceData
import com.leecoder.stockchart.model.stock.TimeItemChartPriceWithCode
import com.leecoder.stockchart.util.calculator.BollingerCalculator
import com.leecoder.stockchart.util.calculator.MinuteAggregator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.first
import java.util.concurrent.ConcurrentHashMap
import javax.inject.Inject

class InitLiveBollingersUseCase @Inject constructor(
    private val roomDatabaseRepository: RoomDatabaseRepository,
    private val ksInvestmentRepository: KsInvestmentRepository,
) {
    private val calculator = BollingerCalculator()

    suspend operator fun invoke(
        iscds: List<String>,
        windowSize: Int = 20,
    ): ConcurrentHashMap<String, MinuteAggregator> = coroutineScope {
        val deferred = iscds.map { iscd ->
           async(Dispatchers.IO) {
                try {
                    val chartPrice =  ksInvestmentRepository.getTimeItemChartPrice(iscd).first()
                    iscd to chartPrice.take(windowSize)
                } catch (e: Exception) {
                    iscd to emptyList<TimeItemChartPriceData>()
                }
            }
        }

        val results = deferred.awaitAll()
        val map = ConcurrentHashMap<String, MinuteAggregator>()

        val aggregators = results.map { (code, list) ->
            async {
                val aggregator = MinuteAggregator(code, calculator, windowSize)
                aggregator.initWith(list)
                code to aggregator
            }
        }.awaitAll()

        aggregators.forEach { (code, agg) ->  map[code] = agg }

        map
    }



}