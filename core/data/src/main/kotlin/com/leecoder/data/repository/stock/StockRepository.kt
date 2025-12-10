package com.leecoder.data.repository.stock

import com.leecoder.stockchart.model.stock.NasdaqTick
import com.leecoder.stockchart.model.stock.StockTick
import com.leecoder.stockchart.model.ui.NasdaqUiData
import kotlinx.coroutines.flow.Flow

interface StockRepository {

    val stocksUiFlow: Flow<List<NasdaqUiData>>
}