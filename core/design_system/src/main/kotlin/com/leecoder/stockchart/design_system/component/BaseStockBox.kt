package com.leecoder.stockchart.design_system.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
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

// 볼린저 하한가 알림용 색상 (현재 검은 배경과 대비되는 주황색 계열)
private val AlertColor = Color(0xFF00AFF0)

@Composable
fun BaseStockBox(
    name: String,
    code: String,
    tradePrice: Double, // 현재 체결가
    priceDiff: Double, // 전일 대비 상승가
    rate: Double, // 등락율
    date: String, // 거래 일자 한국 시간
    currencyUSD: Double,
    isBollingerLowerAlert: Boolean = false, // 볼린저 하한가 알림 여부
    onBollingerAlertAnimationEnd: (() -> Unit)? = null, // 애니메이션 종료 콜백
    onDelete: (String, String) -> Unit,
) {
    // 애니메이션 완료 후 알림 배지 표시 여부
    var showAlertBadge by remember { mutableStateOf(false) }

    // 좌측에서 우측으로 스위핑되는 애니메이션 진행률 (0f -> 1f)
    val sweepProgress by animateFloatAsState(
        targetValue = if (isBollingerLowerAlert) 1f else 0f,
        animationSpec = tween(durationMillis = 300),
        finishedListener = { finalValue ->
            if (finalValue == 1f) {
                // 애니메이션이 완료되고 알림 상태(1f)에 도달했을 때
                showAlertBadge = true
                onBollingerAlertAnimationEnd?.invoke()
            } else {
                // 애니메이션이 사라질 때 (0f)
                showAlertBadge = false
            }
        }
    )

    // 기본 배경색
    val defaultColor = Color.Black.copy(alpha = 0.3f)

    // 애니메이션 배경 Brush 생성 (좌측에서 우측으로 색상이 채워지는 효과)
    val backgroundBrush = if (sweepProgress > 0f) {
        Brush.horizontalGradient(
            colorStops = arrayOf(
                0f to AlertColor.copy(alpha = 0.7f),
                sweepProgress to AlertColor.copy(alpha = 0.7f),
                sweepProgress to defaultColor,
                1f to defaultColor
            )
        )
    } else {
        Brush.horizontalGradient(colors = listOf(defaultColor, defaultColor))
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .background(brush = backgroundBrush, shape = RoundedCornerShape(12.dp))
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
                text = name,
                style = TextStyle(
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold,
                ),
                color = if (showAlertBadge) Color.Yellow else Color.White
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
            text = "($code)",
            style = TextStyle(
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
            ),
            color = if (showAlertBadge) Color.Yellow else Color.White
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
                    color = if (showAlertBadge) Color.Yellow else Color.White
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

            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                // 애니메이션 완료 후 표시되는 알림 배지
                AnimatedVisibility(
                    visible = showAlertBadge,
                    enter = slideInHorizontally(
                        initialOffsetX = { -it },
                        animationSpec = tween(200)
                    ) + fadeIn(animationSpec = tween(200)),
                    exit = slideOutHorizontally(
                        targetOffsetX = { -it },
                        animationSpec = tween(200)
                    ) + fadeOut(animationSpec = tween(200))
                ) {
                    Text(
                        modifier = Modifier
                            .padding(start = 8.dp)
                            .background(
                                color = Color.Yellow,
                                shape = RoundedCornerShape(4.dp)
                            )
                            .padding(horizontal = 8.dp, vertical = 4.dp),
                        text = "볼린저 하한가 도달!",
                        style = TextStyle(
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                        ),
                        color = Color.Black
                    )
                }

                Spacer(Modifier.width(8.dp))

                Text(
                    text = "${date.convertDate()}",
                    style = TextStyle(
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                    ),
                    color = if (showAlertBadge) Color.Yellow else Color.White
                )
            }
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

@Preview(
    showBackground = true,
    device = "spec:width=1280dp,height=800dp,dpi=240",
    name = "Bollinger Lower Alert"
)
@Composable
fun BaseStockBoxBollingerAlertPreview() {
    BaseStockBox(
        name = "볼린저 알림 종목",
        code = "ALERT01",
        tradePrice = 10000.0,
        priceDiff = -500.0,
        rate = -2.5,
        date = "19970816000000",
        currencyUSD = 1465.0,
        isBollingerLowerAlert = true,
        onDelete = { _, _ -> }
    )
}