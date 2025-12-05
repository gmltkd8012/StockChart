package com.leecoder.stockchart.design_system.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
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
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.leecoder.stockchart.util.extension.convertDate
import com.leecoder.stockchart.util.extension.toCurrency
import com.leecoder.stockchart.util.extension.toPlusMinus

@Composable
fun BaseStockBox(
    name: String,
    code: String,
    tradePrice: Double, // 현재 체결가
    priceDiff: Double, // 전일 대비 상승가
    rate: Double, // 등락율
    date: String, // 거래 일자 한국 시간
    currencyUSD: Double,
    onDelete: (String, String) -> Unit,
) {
    Column (
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .background(color = Color.Black.copy(alpha= 0.3f), shape = RoundedCornerShape(12.dp))
            .padding(horizontal = 12.dp, vertical = 16.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                modifier = Modifier.wrapContentSize(),
                text = name ?: "Unknown",
                style = TextStyle(
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold,
                ),
                color = Color.White
            )

            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "삭제",
                tint = Color.Red,
                modifier = Modifier.clickable {
                    onDelete(code, name)
                }
            )
        }

        Spacer(Modifier.height(4.dp))

        Text(
            modifier = Modifier.width(300.dp),
            text = "(${code ?: "Unknown"})",
            style = TextStyle(
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
            ),
            color = Color.White
        )

        Spacer(Modifier.height(12.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = "${(tradePrice * currencyUSD).toCurrency()}원",
                style = TextStyle(
                    fontSize = 30.sp,
                    color = Color.Black,
                    fontWeight = FontWeight.Bold
                )
            )

            Spacer(Modifier.width(4.dp))

            Text(
                text = "$${tradePrice}",
                style = TextStyle(
                    fontSize = 18.sp,
                    color = Color.DarkGray,
                    fontWeight = FontWeight.Bold
                )
            )
        }

        Spacer(Modifier.height(4.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Row(
                modifier = Modifier
                    .weight(1f)
                    .wrapContentHeight(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "전일 대비",
                    style = TextStyle(
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                    ),
                    color = Color.White
                )

                Spacer(Modifier.width(4.dp))

                Text(
                    text = "${rate.toInt().toPlusMinus()}${(priceDiff * currencyUSD).toCurrency()}원",
                    style = TextStyle(
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                    ),
                    color = when {
                        rate > 0 -> Color.Red
                        rate < 0 -> Color.Blue
                        else -> Color.Gray
                    }
                )

                Spacer(Modifier.width(2.dp))

                Text(
                    text = "(${rate})%",
                    style = TextStyle(
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                    ),
                    color = when {
                        rate > 0 -> Color.Red
                        rate < 0 -> Color.Blue
                        else -> Color.Gray
                    }
                )
            }

            Text(
                text = "${date.convertDate()}",
                style = TextStyle(
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                ),
                color = Color.White
            )
        }



//        else {
//            Text(
//                text = "종목 정보 가져오는중...",
//                style = TextStyle(
//                    fontSize = 30.sp,
//                    color = Color.Gray
//                )
//            )
//        }
    }
}

@Preview(
    showBackground = true,
    device = "spec:width=1280dp,height=800dp,dpi=240"
)
@Composable
fun BaseStockBoxPreview() {
    BaseStockBox(
        name = "주식 종목",
        code = "EX00011",
        tradePrice = 10000.0,
        priceDiff = 300.0,
        rate = 1.8,
        date = "19970816000000",
        currencyUSD = 1465.0,
        onDelete = { _, _ -> }
    )
}