package com.leecoder.stockchart.domain.usecase.overseas

import android.util.Log
import com.leecoder.data.repository.KisInvestmentOverseasRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class SaveOverseasStockCurrentPriceUseCase @Inject constructor(
    private val kisInvestmentOverseasRepository: KisInvestmentOverseasRepository,
) {

    suspend operator fun invoke() {
        val response = kisInvestmentOverseasRepository.getCurrentPriceNasdaq(
            auth = "",
            excd = "NAS",
            symb = "TSLA",
        ).first()

        Log.e("lynn", "[Response]: ${response}")
    }
}