package com.leecoder.stockchart.app.screen

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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.leecoder.stockchart.design_system.component.BaseRegistedBox
import com.leecoder.stockchart.design_system.component.BaseSettingBox
import com.leecoder.stockchart.model.screen.BollingerSetting
import com.leecoder.stockchart.model.screen.Screen

@Composable
fun SettingScreen(

) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
    ) {
        BollingerSettingMenu()
    }
}


@Composable
fun BollingerSettingMenu() {
    var selectedBollingerOption = remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
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
                        selectedBollingerOption.value = selectedMenuId
                    }
                )

                Spacer(Modifier.height(8.dp))
            }
        }

        Spacer(Modifier.height(16.dp))
        Spacer(Modifier.fillMaxWidth().height(0.5.dp).background(Color.LightGray))
    }
}