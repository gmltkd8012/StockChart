package com.leecoder.data.repository

import androidx.compose.runtime.key
import com.leecoder.stockchart.model.room.BollingerData
import com.leecoder.stockchart.model.stock.SubscribedStockData
import com.leecoder.stockchart.model.symbol.NasSymbolData
import com.leecoder.stockchart.room.dao.BollingerDao
import com.leecoder.stockchart.room.dao.NasSymbolDao
import com.leecoder.stockchart.room.dao.SubscribedStockDao
import com.leecoder.stockchart.room.entity.toData
import com.leecoder.stockchart.room.entity.toEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RoomDatabaseRepositoryImpl @Inject constructor(
    private val nasSymbolDao: NasSymbolDao,
    private val bollingerDao: BollingerDao,
    private val subscribedStockDao: SubscribedStockDao,
): RoomDatabaseRepository {

    override suspend fun searchNasSymbol(keyword: String): Flow<List<NasSymbolData>> =
        nasSymbolDao.searchNasSymbol(keyword).map { it.map { it.toData() } }

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

    override suspend fun subscribeStock(stocks: List<SubscribedStockData>) {
        subscribedStockDao.insertStock(stocks.map { it.toEntity() })
    }

    override suspend fun unSubscribeStock(stock: SubscribedStockData) {
        subscribedStockDao.deleteStock(stock.toEntity())
    }

    override suspend fun getAllSubscribedStocks(): Flow<List<SubscribedStockData>> =
        subscribedStockDao.getAllSubscribedStocks().map { it.map { it.toData() } }


    override suspend fun getSubscribedStock(code: String?): SubscribedStockData =
        subscribedStockDao.getSubscribedStock(code ?: "").toData()
}