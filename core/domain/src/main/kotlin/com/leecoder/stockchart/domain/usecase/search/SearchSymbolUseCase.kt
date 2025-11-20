package com.leecoder.stockchart.domain.usecase.search

import android.graphics.Region
import com.leecoder.data.repository.symbol.SymbolRepository
import javax.inject.Inject

class SearchSymbolUseCase @Inject constructor(
    private val symbolRepository: SymbolRepository,
) {

    suspend operator fun invoke(region: String, name: String) = symbolRepository.searchSymbol(region, name)
}