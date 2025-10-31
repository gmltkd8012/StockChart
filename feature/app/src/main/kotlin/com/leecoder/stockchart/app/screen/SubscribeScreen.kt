package com.leecoder.stockchart.app.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.leecoder.stockchart.design_system.component.BaseRegistedBox
import com.leecoder.stockchart.design_system.component.BaseStockBox
import com.leecoder.stockchart.design_system.component.BaseSymbolItem
import com.leecoder.stockchart.model.stock.RegistedStockData
import com.leecoder.stockchart.model.stock.StockTick
import com.leecoder.stockchart.model.symbol.KrxSymbolData
import com.leecoder.stockchart.model.ui.StockUiData

@Composable
fun SubscribeScreen(
    isConnected: Boolean = false,
    textFieldState: String,
    searchResult: List<KrxSymbolData>,
    stockTick: List<StockUiData>,
    onRegistedSymbol: (code: String, name: String) -> Unit,
    onDeletedSymbol: (code: String, name: String) -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
    ) {
        if (!isConnected) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "서버 연결중...",
                    style = TextStyle(
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Bold,
                    ),
                    color = Color.Gray
                )
            }
        } else {
            if (textFieldState.isNotEmpty()) {
                if (searchResult.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "검색 결과가 없어요.",
                            style = TextStyle(
                                fontSize = 30.sp,
                                fontWeight = FontWeight.Bold,
                            )
                        )
                    }
                } else {
                    Column {
                        Spacer(Modifier.height(8.dp))

                        Text(
                            text = "검색된 종목 결과",
                            style = TextStyle(
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            ),
                            modifier = Modifier.padding(start = 8.dp)
                        )

                        Spacer(Modifier.height(20.dp))

                        LazyColumn {
                            items(searchResult) { result ->
                                BaseSymbolItem(
                                    name = result.name,
                                    code = result.code,
                                    onClick = { (code, name) ->
                                        onRegistedSymbol(code, name)
                                    }
                                )

                                Spacer(Modifier.height(12.dp))
                            }
                        }
                    }
                }
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    Column {
                        Spacer(Modifier.height(20.dp))
                        BaseRegistedBox(count = stockTick.size)
                        Spacer(Modifier.height(16.dp))

                        if (stockTick.isEmpty()) {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "종목을 구독하세요.",
                                    style = TextStyle(
                                        fontSize = 50.sp,
                                        color = Color.Gray
                                    )
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
        }
    }
}