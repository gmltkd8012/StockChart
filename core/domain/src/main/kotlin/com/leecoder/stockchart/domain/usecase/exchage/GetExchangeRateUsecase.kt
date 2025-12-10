package com.leecoder.stockchart.domain.usecase.exchage

import com.leecoder.data.repository.room.RoomDatabaseRepository
import com.leecoder.network.AppDispatchers
import com.leecoder.network.Dispatcher
import com.leecoder.stockchart.model.exchange.ExchangeRateData
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.filterNot
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetExchangeRateUsecase @Inject constructor(
    private val roomDatabaseRepository: RoomDatabaseRepository,
    @Dispatcher(AppDispatchers.Default) private val defaultDispatcher: CoroutineDispatcher,
) {

    suspend operator fun invoke(): List<ExchangeRateData> =
        withContext(defaultDispatcher) {
            roomDatabaseRepository.getAllExChangeRates()
                .first()
                .filterNot { it.curUnit == "KRW" } // 원화는 필터링 고정 값 1이라서
                .sortedBy { // 달러 > 엔화 > 유로 > 위안 순
                    when (it.curUnit) {
                        "USD" -> 0
                        "JPY" -> 1
                        "EUR" -> 2
                        "CNH" -> 3
                        else -> 4
                    }
                }
        }
}