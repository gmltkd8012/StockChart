package com.leecoder.data.repository

import com.leecoder.stockchart.model.symbol.KrxSymbolData
import com.leecoder.stockchart.room.entity.KrxSymbolEntity
import kotlinx.coroutines.flow.Flow

interface KrxSymbolRepository {

    suspend fun searchKrxSymbol(name: String): Flow<List<KrxSymbolData>>
}