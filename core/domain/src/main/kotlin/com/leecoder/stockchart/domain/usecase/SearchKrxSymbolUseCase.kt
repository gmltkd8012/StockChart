package com.leecoder.stockchart.domain.usecase

import com.leecoder.data.repository.KrxSymbolRepository
import javax.inject.Inject

class SearchKrxSymbolUseCase @Inject constructor(
    private val krxSymbolRepository: KrxSymbolRepository,
) {

    suspend operator fun invoke(name: String) = krxSymbolRepository.searchKrxSymbol(name)
}