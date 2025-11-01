package com.leecoder.data.repository

import com.leecoder.stockchart.model.room.BollingerData
import com.leecoder.stockchart.room.dao.BollingerDao
import com.leecoder.stockchart.room.entity.toData
import com.leecoder.stockchart.room.entity.toEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RoomDatabaseRepositoryImpl @Inject constructor(
    private val bollingerDao: BollingerDao
): RoomDatabaseRepository {

    override suspend fun insertAllBollingers(bollingers: List<BollingerData>) {
        bollingerDao.insertAll(bollingers.map { it.toEntity() })
    }

    override suspend fun insertBollinger(bollinger: BollingerData) {
        bollingerDao.insert(bollinger.toEntity())
    }

    override suspend fun deleteBollinger(bollinger: BollingerData) {
        bollingerDao.delete(bollinger.toEntity())
    }

    override suspend fun getAllBollingers(): Flow<List<BollingerData>> =
        bollingerDao.getBollingers().map { it.map { it.toData() } }
}