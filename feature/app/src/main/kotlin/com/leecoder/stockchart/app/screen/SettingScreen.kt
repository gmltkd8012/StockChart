package com.leecoder.stockchart.app.screen

import android.util.Log
import android.widget.Space
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.leecoder.stockchart.app.viewmodel.SettingViewModel
import com.leecoder.stockchart.design_system.component.BaseRegistedBox
import com.leecoder.stockchart.design_system.component.BaseSettingBox
import com.leecoder.stockchart.model.screen.BollingerSetting
import com.leecoder.stockchart.model.screen.MarketInfo
import com.leecoder.stockchart.model.screen.Screen

@Composable
fun SettingScreen(
    viewModel: SettingViewModel = hiltViewModel<SettingViewModel>()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    val isShownMarketInfo = remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.currentBollingerSetting()
        viewModel.currentMarketInfo()
    }

    LaunchedEffect(state.marketInfo) {
        if (state.marketInfo.isNotEmpty()) {
            isShownMarketInfo.value = true
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
    ) {
        Spacer(Modifier.height(20.dp))
        Text(
            text = "설정 메뉴",
            style = TextStyle(
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            ),
            modifier = Modifier.padding(start = 8.dp)
        )

        BollingerSettingMenu(
            bollingerValue = state.bollingerSettingValue,
            onChangedBollinger = { value ->
                viewModel.changedBollingerSetting(value)
            }
        )

        if (isShownMarketInfo.value) {
            MarketInfoMenu(
                marketOption = state.marketInfo
            )
        }
    }
}

@Composable
fun BollingerSettingMenu(
    bollingerValue: String,
    onChangedBollinger:(String) -> Unit,
) {
    var selectedBollingerOption = remember { mutableStateOf("") }

    LaunchedEffect(bollingerValue) {
        selectedBollingerOption.value = bollingerValue
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
    ) {
        Spacer(Modifier.height(32.dp))

        Text(
            text = "볼린저 밴드 하한가 알림 설정",
            style = TextStyle(
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            ),
            color = Color.Gray,
            modifier = Modifier.padding(start = 8.dp)
        )

        Spacer(Modifier.height(8.dp))

        LazyColumn {
            items(BollingerSetting.bollingerMenus) { menu ->
                BaseSettingBox(
                    id = menu.menuId,
                    title = menu.title,
                    description =  menu.description,
                    isChecked = menu.menuId == selectedBollingerOption.value,
                    onCheckedChanged = { selectedMenuId ->
                        if (selectedBollingerOption.value == selectedMenuId) {
                            selectedBollingerOption.value = when {
                                selectedMenuId == BollingerSetting.DailyBollinger.menuId -> {
                                    onChangedBollinger(BollingerSetting.LiveBollinger.menuId)
                                    BollingerSetting.LiveBollinger.menuId
                                }
                                else -> {
                                    onChangedBollinger(BollingerSetting.DailyBollinger.menuId)
                                    BollingerSetting.DailyBollinger.menuId
                                }
                            }
                        } else {
                            onChangedBollinger(selectedMenuId)
                            selectedBollingerOption.value = selectedMenuId
                        }
                    }
                )

                Spacer(Modifier.height(8.dp))
            }
        }

        Spacer(Modifier.height(16.dp))
        Spacer(Modifier
            .fillMaxWidth()
            .height(0.5.dp)
            .background(Color.LightGray))
    }
}


@Composable
fun MarketInfoMenu(
    marketOption: String,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
    ) {
        Spacer(Modifier.height(32.dp))

        Text(
            text = "현재 시장 정보",
            style = TextStyle(
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            ),
            color = Color.Gray,
            modifier = Modifier.padding(start = 8.dp)
        )

        Spacer(Modifier.height(8.dp))

        BaseSettingBox(
            title = if (marketOption == MarketInfo.Kospi.menuId) MarketInfo.Kospi.title else MarketInfo.Nasdaq.title,
            description = if (marketOption == MarketInfo.Kospi.menuId) MarketInfo.Kospi.description else MarketInfo.Nasdaq.description
        )

        Spacer(Modifier.height(16.dp))
        Spacer(Modifier
            .fillMaxWidth()
            .height(0.5.dp)
            .background(Color.LightGray))
    }
}