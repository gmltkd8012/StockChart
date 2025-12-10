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
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.receiveAsFlow
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

    private val latestTicks: StateFlow<Map<String, NasdaqTick>> =
        webSocketDataSource.channelStockTick
            .receiveAsFlow()
            .flowOn(ioDispatcher)
            .mapNotNull { tick ->
                tick.symb?.let { code -> code to tick }
            }
            .scan(emptyMap<String, NasdaqTick>()) { acc, (code, tick) ->
                acc + (code to tick)
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