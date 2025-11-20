package com.leecoder.data.repository.symbol

import com.leecoder.stockchart.model.symbol.SymbolData
import com.leecoder.stockchart.room.dao.SymbolDao
import com.leecoder.stockchart.room.entity.toData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SymbolRepositoryImpl @Inject constructor(
    private val krxSymbolDao: SymbolDao
): SymbolRepository {

    override suspend fun searchSymbol(region: String, keyword: String): Flow<List<SymbolData>> =
        krxSymbolDao.searchSymbol(region, keyword).map { it.map { it.toData() } }

    override suspend fun searchAllSymbol(keyword: String): Flow<List<SymbolData>> =
        krxSymbolDao.searchAllSymbol(keyword).map { it.map { it.toData() } }
}