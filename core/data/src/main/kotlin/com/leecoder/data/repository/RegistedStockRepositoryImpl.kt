package com.leecoder.data.repository

import com.leecoder.stockchart.model.stock.RegistedStockData
import com.leecoder.stockchart.room.dao.RegistedStockDao
import com.leecoder.stockchart.room.entity.RegistedStockEntity
import com.leecoder.stockchart.room.entity.toData
import com.leecoder.stockchart.room.entity.toEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class RegistedStockRepositoryImpl @Inject constructor(
    private val registedStockDao: RegistedStockDao
): RegistedStockRepository {

    override suspend fun insert(stock: RegistedStockData) {
        registedStockDao.insert(stock.toEntity())
    }

    override suspend fun getRegistedStock(): Flow<List<RegistedStockData>> =
        registedStockDao.getRegistedStock().map { it.map { it.toData() } }

    override suspend fun delete(stock: RegistedStockData) {
        registedStockDao.delete(stock.toEntity())
    }
}