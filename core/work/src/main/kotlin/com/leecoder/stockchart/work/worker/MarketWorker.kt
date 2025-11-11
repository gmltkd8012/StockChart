package com.leecoder.stockchart.work.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.leecoder.stockchart.datastore.repository.DataStoreRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class MarketWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val dataStoreRepository: DataStoreRepository,
) : CoroutineWorker(appContext, workerParams) {

    companion object {
        const val UNIQUE_WORK_NAME = "market_worker"
        const val INPUT_DATA_TARGET_OPTION = "target_option"
    }

    override suspend fun doWork(): Result {
        try {
            val targetOption = inputData.getString(INPUT_DATA_TARGET_OPTION) ?: return Result.failure()
            dataStoreRepository.updateMarketInfo(targetOption)
            return Result.Success()
        } catch (e: Exception) {
            return Result.failure()
        }
    }
}