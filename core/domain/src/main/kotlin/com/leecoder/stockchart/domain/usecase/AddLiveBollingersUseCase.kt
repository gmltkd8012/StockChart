package com.leecoder.stockchart.domain.usecase

import com.leecoder.data.repository.KsInvestmentRepository
import com.leecoder.data.repository.room.RoomDatabaseRepository
import com.leecoder.stockchart.model.stock.TimeItemChartPriceData
import com.leecoder.stockchart.util.calculator.BollingerCalculator
import com.leecoder.stockchart.util.calculator.MinuteAggregator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.first
import java.util.concurrent.ConcurrentHashMap
import javax.inject.Inject

class AddLiveBollingersUseCase @Inject constructor(
    private val roomDatabaseRepository: RoomDatabaseRepository,
    private val ksInvestmentRepository: KsInvestmentRepository,
) {

    private val calculator = BollingerCalculator()

    suspend operator fun invoke(
        iscd: String,
        windowSize: Int = 20,
    ): ConcurrentHashMap<String, MinuteAggregator> = coroutineScope {
        val deferred = async(Dispatchers.IO) {
            try {
                val chartPrice = ksInvestmentRepository.getTimeItemChartPrice(iscd).first()
                iscd to chartPrice.take(windowSize)
            } catch (e: Exception) {
                iscd to emptyList<TimeItemChartPriceData>()
            }
        }

        val result = deferred.await()
        val map = ConcurrentHashMap<String, MinuteAggregator>()

        val aggregator = MinuteAggregator(result.first, calculator, windowSize)

        async {
            aggregator.initWith(result.second)
        }.await()

        map.put(result.first, aggregator)
        map
    }
}