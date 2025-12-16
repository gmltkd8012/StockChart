package com.leecoder.stockchart.app.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
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
import com.leecoder.stockchart.design_system.component.BaseAlarmBox
import com.leecoder.stockchart.design_system.component.BaseRegistedBox
import com.leecoder.stockchart.model.room.BollingerData
import com.leecoder.stockchart.model.ui.BollingerUiData
import com.leecoder.stockchart.util.extension.toDateTimeString

@Composable
fun AlarmScreen(
    bollingers: List<BollingerUiData> = emptyList(),
    onDeletedAlarm: (String, String) -> Unit,
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
    ) {
        Spacer(Modifier.height(20.dp))
        BaseRegistedBox(
            title = "알림",
            currentCount = bollingers.size,
            maxCount = 0,
        )
        Spacer(Modifier.height(16.dp))

        if (bollingers.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = "볼린저 하한가 알림 없음",
                    style = TextStyle(
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Bold,
                    ),
                    color = Color.Gray
                )
            }
        } else {
            LazyColumn {
                items(bollingers.sortedByDescending { it.cntgHour }) { bollinger ->
                    BaseAlarmBox(
                        name = bollinger.name,
                        code = bollinger.code,
                        cntgHour = bollinger.cntgHour.toDateTimeString(),
                        onDelete = { code, name ->
                            onDeletedAlarm(code, name)
                        }
                    )

                    Spacer(Modifier.height(12.dp))
                }
            }
        }
    }
}