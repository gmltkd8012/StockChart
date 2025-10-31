package com.leecoder.stockchart.design_system.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun BaseStockBox(
    name: String?,
    code: String?,
    tradePrice: Int?, // 현재 체결가
    priceDiff: Int?, // 전일 대비 상승가
    onDelete: (String, String) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = Color.Black.copy(alpha= 0.3f), shape = RoundedCornerShape(12.dp))
            .padding(horizontal = 8.dp, vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {

        if (name != null && code != null &&
                tradePrice != null && priceDiff != null) {
            Row(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    modifier = Modifier.width(300.dp),
                    text = name.ifEmpty { "Unknown" },
                    style = TextStyle(
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Bold,
                    ),
                    color = Color.White
                )

                Spacer(Modifier.width(12.dp))

                Text(
                    text = "$tradePrice ($priceDiff)",
                    style = TextStyle(
                        fontSize = 30.sp,
                        color = when {
                            priceDiff > 0 -> Color.Red
                            priceDiff < 0 -> Color.Blue
                            else -> Color.Gray
                        }
                    )
                )
            }

            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "삭제",
                tint = Color.Red,
                modifier = Modifier.clickable {
                    onDelete(code, name)
                }
            )

            Spacer(Modifier.width(12.dp))
        } else {
            Text(
                text = "종목 정보 가져오는중...",
                style = TextStyle(
                    fontSize = 30.sp,
                    color = Color.Gray
                )
            )
        }
    }
}

@Preview
@Composable
fun BaseStockBoxPreview() {
    BaseStockBox(
        name = "주식 종목",
        code = "EX00011",
        tradePrice = 10000,
        priceDiff = 300,
        onDelete = { _, _ -> }
    )
}