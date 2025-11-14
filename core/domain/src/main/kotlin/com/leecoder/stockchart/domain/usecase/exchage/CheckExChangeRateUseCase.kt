package com.leecoder.stockchart.domain.usecase.exchage

import com.leecoder.stockchart.datastore.repository.DataStoreRepository
import com.leecoder.stockchart.util.time.TimeUtils
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class CheckExChangeRateUseCase @Inject constructor(
    private val initExChangeRateUseCase: InitExChangeRateUseCase,
    private val updateExChangeRateUseCase: UpdateExChangeRateUseCase,
    private val dataStoreRepository: DataStoreRepository,
) {
    suspend operator fun invoke() {
        val save = dataStoreRepository.currentExchangeRateSaveTime.first()
        val current = System.currentTimeMillis()

        val saveDate = TimeUtils.millisToDate(save)
        val currentDate = TimeUtils.millisToDate(current)

        when {
            save == 0L -> initExChangeRateUseCase(current)
            currentDate.isAfter(saveDate) -> updateExChangeRateUseCase(current)
            else -> {}
        }
    }
}