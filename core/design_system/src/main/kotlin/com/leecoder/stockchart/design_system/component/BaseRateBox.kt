package com.leecoder.stockchart.design_system.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun BaseRateBox(
    name: String,
    unit: String,
    price: String, // 현재가
    diffPrice: String?, // 전일 대비 상승가
    diffPer: String?, // 전일 대비 상승률
    modifier: Modifier = Modifier,
) {
    val diffValue = diffPrice?.toDoubleOrNull()

    val textColor = when {
        diffValue == null -> Color.LightGray
        diffValue > 0 -> Color.Red
        diffValue < 0 -> Color.Blue
        else -> Color.LightGray
    }

    Column(
        modifier = modifier
            .width(170.dp)
            .background(color = Color.Black.copy(alpha= 0.5f), shape = RoundedCornerShape(16.dp))
            .padding(horizontal = 12.dp,  vertical = 20.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = "$name ($unit)",
            style = TextStyle(
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
            ),
            color = Color.White
        )

        Spacer(Modifier.height(4.dp))

        Row(
            modifier = Modifier.wrapContentSize(),
            verticalAlignment = Alignment.Bottom,
        ) {
            Text(
                text = price,
                style = TextStyle(
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                ),
                color = textColor
            )

            Spacer(Modifier.width(6.dp))

            Text(
                text = if (diffValue != null) "${diffPrice} (${diffPer}%)" else "",
                style = TextStyle(
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                ),
                color = textColor,
                modifier = Modifier.padding(bottom = 1.dp)
            )
        }




    }

}



@Preview(backgroundColor = 0xFFFFFFFF, showBackground = true)
@Composable
fun BaseRateBoxPreivew() {
    BaseRateBox(
        name = "달러",
        unit = "$",
        price = "1471.03",
        diffPrice = "-13.5",
        diffPer = "-0.3",
        modifier = Modifier
    )
}