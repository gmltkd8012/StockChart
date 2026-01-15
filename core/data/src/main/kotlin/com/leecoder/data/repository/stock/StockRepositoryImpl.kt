package com.leecoder.data.repository.stock

import com.leecoder.data.source.WebSocketDataSource
import com.leecoder.network.AppDispatchers
import com.leecoder.network.Dispatcher
import com.leecoder.network.di.ApplicationScope
import com.leecoder.stockchart.model.stock.NasdaqTick
import com.leecoder.stockchart.model.ui.NasdaqUiData
import com.leecoder.stockchart.room.dao.SubscribedStockDao
import com.leecoder.stockchart.util.extension.convertToDouble
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.scan
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StockRepositoryImpl @Inject constructor(
    private val subscribedStockDao: SubscribedStockDao,
    private val webSocketDataSource: WebSocketDataSource,
    @ApplicationScope private val appScope: CoroutineScope,
    @Dispatcher(AppDispatchers.IO) private val ioDispatcher: CoroutineDispatcher,
    @Dispatcher(AppDispatchers.Default) private val defaultDispatcher: CoroutineDispatcher,
): StockRepository {

    override val tickFlow: SharedFlow<NasdaqTick> = webSocketDataSource.stockTickFlow

    private val latestTicks: StateFlow<Map<String, NasdaqTick>> =
        tickFlow
            .mapNotNull { tick: NasdaqTick ->
                tick.symb?.let { code -> code to tick }
            }
            .scan(emptyMap<String, NasdaqTick>()) { acc: Map<String, NasdaqTick>, pair: Pair<String, NasdaqTick> ->
                acc + (pair.first to pair.second)
            }
            .stateIn(appScope, SharingStarted.WhileSubscribed(5_000), emptyMap())


    override val stocksUiFlow: Flow<List<NasdaqUiData>> =
        subscribedStockDao.getAllSubscribedStocks()
            .combine(latestTicks) { stocks, tickMap ->
                stocks.map { info ->
                    val tick = tickMap[info.code]
                    NasdaqUiData(
                        code = info.code,
                        name = info.name,
                        kymd = tick?.kymd,
                        khms = tick?.khms,
                        last = tick?.last?.toDoubleOrNull()
                            ?: info.price.takeIf { it.isNotEmpty() }?.toDouble(),
                        diff = tick?.diff?.toDoubleOrNull() ?: 0.0,
                        rate = tick?.rate?.toDoubleOrNull() ?: 0.0,
                        tvol = tick?.tvol,
                        tamt = tick?.tamt,
                    )
                }
            }.flowOn(defaultDispatcher)
            .distinctUntilChanged()
}