package com.leecoder.stockchart.domain.usecase.exchage

import com.leecoder.data.repository.KoreaAeximRepository
import com.leecoder.data.repository.room.RoomDatabaseRepository
import com.leecoder.stockchart.datastore.repository.DataStoreRepository
import com.leecoder.stockchart.model.exchange.ExchangeRateData
import com.leecoder.stockchart.util.calculator.calculatePriceChanged
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class UpdateExChangeRateUseCase @Inject constructor(
    private val koreaAeximRepository: KoreaAeximRepository,
    private val roomDatabaseRepository: RoomDatabaseRepository,
    private val dataStoreRepository: DataStoreRepository,
) {

    suspend operator fun invoke(currentMillis: Long) {
        val response = koreaAeximRepository.getExchangeInfo().first()
        val saveCurrency = roomDatabaseRepository.getAllExChangeRates().first()

        val insertRate = response.map { currency ->
            val spl = currency.curNm.split(" ".toRegex())

            val savedCurUnit = saveCurrency.find { it.curUnit == currency.curUnit.take(3) }
            val priceRating = savedCurUnit?.let {
                calculatePriceChanged(savedCurUnit.exchageRate.toDouble(), currency.dealBasR.toDouble())
            }

            ExchangeRateData(
                curUnit = currency.curUnit.take(3), // 앞 세자리 저장
                conName = spl.first(),
                curName = spl.last(),
                exchageRate = currency.dealBasR,
                prdyVrss = priceRating?.first,
                prdyCtrt = priceRating?.second,
            )
        }

        roomDatabaseRepository.insertAllExChangeRates(insertRate)
        dataStoreRepository.updateExchangeRateSaveTime(currentMillis)
    }
}