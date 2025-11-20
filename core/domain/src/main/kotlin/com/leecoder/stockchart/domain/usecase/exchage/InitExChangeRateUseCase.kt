package com.leecoder.stockchart.domain.usecase.exchage

import com.leecoder.data.repository.KoreaAeximRepository
import com.leecoder.data.repository.room.RoomDatabaseRepository
import com.leecoder.stockchart.datastore.repository.DataStoreRepository
import com.leecoder.stockchart.model.exchange.ExchangeRateData
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class InitExChangeRateUseCase @Inject constructor(
    private val koreaAeximRepository: KoreaAeximRepository,
    private val roomDatabaseRepository: RoomDatabaseRepository,
    private val dataStoreRepository: DataStoreRepository,
) {
    suspend operator fun invoke(currentMillis: Long) {
        val response = koreaAeximRepository.getExchangeInfo().first()
        val insertRate = response.map { currency ->
            val spl = currency.curNm.split(" ".toRegex())

            ExchangeRateData(
                curUnit = currency.curUnit.take(3), // 앞 세자리 저장
                conName = spl.first(),
                curName = spl.last(),
                exchageRate = currency.dealBasR,
                prdyVrss = "",
                prdyCtrt = "",
            )
        }

        roomDatabaseRepository.insertAllExChangeRates(insertRate)
        dataStoreRepository.updateExchangeRateSaveTime(currentMillis)
    }
}