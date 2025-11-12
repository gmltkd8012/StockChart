package com.leecoder.stockchart.domain.usecase

import com.leecoder.stockchart.datastore.repository.DataStoreRepository
import javax.inject.Inject

class UpdateMarketInfoUseCase @Inject constructor(
    private val dataStoreRepository: DataStoreRepository,
) {

    suspend operator fun invoke() {
        // TODO - 코스피 <-> 나스닥 Room 스위칭 로직 필요
    }
}