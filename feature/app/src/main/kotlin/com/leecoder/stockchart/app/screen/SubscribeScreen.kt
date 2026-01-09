package com.leecoder.stockchart.app.screen

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.leecoder.stockchart.app.viewmodel.SubscribeViewModel
import com.leecoder.stockchart.design_system.component.BaseRateBox
import com.leecoder.stockchart.design_system.component.BaseRegistedBox
import com.leecoder.stockchart.design_system.component.BaseStockBox
import com.leecoder.stockchart.design_system.component.BaseSymbolItem
import com.leecoder.stockchart.model.exchange.ExchangeRateData
import com.leecoder.stockchart.model.stock.StockTick
import com.leecoder.stockchart.model.ui.NasdaqUiData
import com.leecoder.stockchart.model.ui.StockUiData
import com.leecoder.stockchart.util.extension.convertToDouble

@Composable
fun SubscribeScreen(
    viewModel: SubscribeViewModel = hiltViewModel<SubscribeViewModel>(),
    onDeletedSymbol: (code: String, name: String) -> Unit,
) {
    val state by viewModel.state.collectAsState()
    val stockTick = state.tick

    var isBool by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
    ) {
        Column {
            Spacer(Modifier.height(20.dp))

            Text(
                text = "오늘의 환율 (KRW 1000)",
                style = TextStyle(
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier.padding(start = 8.dp)
            )

            Spacer(Modifier.height(8.dp))

            Row() {
                BaseRateBox(
                    name = "KRW",
                    unit = "원",
                    price = 1000.0,
                    modifier = Modifier.weight(1f),
                )

                Spacer(Modifier.width(4.dp))

                BaseRateBox(
                    name = "USD",
                    unit = "달러",
                    price = state.currency,
                    modifier = Modifier.weight(1f),
                )
            }

            Spacer(Modifier.height(20.dp))

            BaseRegistedBox(
                title = "구독 종목",
                currentCount = stockTick.size,
            )
            Spacer(Modifier.height(16.dp))

            if (stockTick.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "종목을 구독해주세요.",
                        style = TextStyle(
                            fontSize = 30.sp,
                            fontWeight = FontWeight.Bold,
                        ),
                        color = Color.Gray
                    )
                }
            } else {
                LazyColumn {
                    items(stockTick) { stockTick ->
                        BaseStockBox(
                            name =  stockTick.name ?: "",
                            code = stockTick.code ?: "",
                            tradePrice = stockTick.last ?: 0.0,
                            priceDiff = stockTick.diff ?: 0.0,
                            rate = stockTick.rate ?: 0.0,
                            currencyUSD = state.currency,
                            date = stockTick.kymd + stockTick.khms,
                            onDelete = { code, name ->
                                isBool = true
                            },
                            isBollingerLowerAlert = isBool
                        )

                        Spacer(Modifier.height(12.dp))
                    }
                }
            }
        }
    }
}