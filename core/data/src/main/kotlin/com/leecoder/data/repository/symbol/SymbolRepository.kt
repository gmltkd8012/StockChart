package com.leecoder.data.repository.symbol

import com.leecoder.stockchart.model.symbol.SymbolData
import kotlinx.coroutines.flow.Flow

interface SymbolRepository {

    suspend fun searchSymbol(region: String, keyword: String): Flow<List<SymbolData>>

    suspend fun searchAllSymbol(keyword: String): Flow<List<SymbolData>>
}
