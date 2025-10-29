package com.leecoder.data.repository

import com.leecoder.stockchart.model.symbol.KrxSymbolData
import com.leecoder.stockchart.room.dao.KrxSymbolDao
import com.leecoder.stockchart.room.entity.KrxSymbolEntity
import com.leecoder.stockchart.room.entity.toData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class KrxSymbolRepositoryImpl @Inject constructor(
    private val krxSymbolDao: KrxSymbolDao
): KrxSymbolRepository {

    override suspend fun searchKrxSymbol(name: String): Flow<List<KrxSymbolData>> =
        krxSymbolDao.searchKrxSymbol(name).map { it.map { it.toData() } }
}