package com.leecoder.data.repository

import com.leecoder.stockchart.model.room.BollingerData
import com.leecoder.stockchart.room.entity.BollingerEntity
import kotlinx.coroutines.flow.Flow

interface RoomDatabaseRepository {

    // 전체 종목 볼린저 데이터 일괄 저장
    suspend fun insertAllBollingers(bollingers: List<BollingerData>)

    // 개별 종목 볼린저 데이터 저장
    suspend fun insertBollinger(bollinger: BollingerData)

    // 개별 종목 볼린저 데이터 삭제
    suspend fun deleteBollinger(bollinger: BollingerData)

    // 전체 종목 볼린저 리스트 조회
    suspend fun getAllBollingers(): Flow<List<BollingerData>>
}