package com.leecoder.data.repository

import com.leecoder.stockchart.model.exchange.ExchangeRateData
import com.leecoder.stockchart.model.room.BollingerData
import com.leecoder.stockchart.model.stock.SubscribedStockData
import com.leecoder.stockchart.model.symbol.NasSymbolData
import com.leecoder.stockchart.room.entity.BollingerEntity
import com.leecoder.stockchart.room.entity.ExChangeRateEntity
import kotlinx.coroutines.flow.Flow

interface RoomDatabaseRepository {

    // [NAS] 종목 검색
    suspend fun searchNasSymbol(keyword: String): Flow<List<NasSymbolData>>

    // 전체 종목 볼린저 데이터 일괄 저장
    suspend fun insertAllBollingers(bollingers: List<BollingerData>)

    // 개별 종목 볼린저 데이터 저장
    suspend fun insertBollinger(bollinger: BollingerData)

    // 개별 종목 볼린저 데이터 삭제
    suspend fun deleteBollinger(bollinger: BollingerData)

    // 전체 종목 볼린저 리스트 조회
    suspend fun getAllBollingers(): Flow<List<BollingerData>>

    // 개별 또는 전체 구독 종목 저장
    suspend fun subscribeStock(stocks: List<SubscribedStockData>)

    // 일괄 구독 종목 조회
    suspend fun getAllSubscribedStocks(): Flow<List<SubscribedStockData>>

    // 개별 구독 종목 조회
    suspend fun getSubscribedStock(code: String?): SubscribedStockData

    // 개별 구독 종목 삭제
    suspend fun unSubscribeStock(stock: SubscribedStockData)

    // 데일리 환율 저장
    suspend fun insertAllExChangeRates(rates: List<ExchangeRateData>)

    // 데일리 환율 조회
    suspend fun getAllExChangeRates(): Flow<List<ExchangeRateData>>

    // 데일리 환율 전체 삭제
    suspend fun deleteAllExChangeRates(rates: List<ExchangeRateData>)
}