package com.leecoder.stockchart.app.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.leecoder.stockchart.design_system.component.BaseRateBox
import com.leecoder.stockchart.design_system.component.BaseRegistedBox
import com.leecoder.stockchart.design_system.component.BaseStockBox
import com.leecoder.stockchart.design_system.component.BaseSymbolItem
import com.leecoder.stockchart.model.exchange.ExchangeRateData
import com.leecoder.stockchart.model.stock.StockTick
import com.leecoder.stockchart.model.ui.StockUiData

@Composable
fun SubscribeScreen(
    stockTick: List<StockUiData>,
    exchangeRates: List<ExchangeRateData>,
    onDeletedSymbol: (code: String, name: String) -> Unit,
) {
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

            LazyRow() {
                itemsIndexed(exchangeRates) { idx, curreny ->
                    BaseRateBox(
                        name = curreny.curName,
                        unit = curreny.curUnit,
                        price = curreny.exchageRate,
                        diffPrice = curreny.prdyVrss,
                        diffPer = curreny.prdyCtrt,
                    )

                    if (idx != exchangeRates.size - 1) Spacer(Modifier.width(4.dp))
                }
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
                            name =  stockTick.name,
                            code = stockTick.code,
                            tradePrice = stockTick.tradePrice,
                            priceDiff = stockTick.priceDiff,
                            onDelete = { code, name ->
                                onDeletedSymbol(code, name)
                            }
                        )

                        Spacer(Modifier.height(12.dp))
                    }
                }
            }
        }
    }
}