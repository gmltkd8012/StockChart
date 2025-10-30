package com.leecoder.data.repository

import com.leecoder.stockchart.model.stock.RegistedStockData
import com.leecoder.stockchart.room.entity.RegistedStockEntity
import kotlinx.coroutines.flow.Flow

interface RegistedStockRepository {

    suspend fun insert(stock: RegistedStockData)

    suspend fun getRegistedStock(): Flow<List<RegistedStockData>>

    suspend fun getRegistedStockByCode(code: String?): RegistedStockData

    suspend fun delete(stock: RegistedStockData)
}