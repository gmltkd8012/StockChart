package com.leecoder.stockchart.util.extension

import java.text.DecimalFormat
import kotlin.math.round

fun Double.toCurrency(): String =
    try {
        DecimalFormat("#,###").format(this.toInt())
    } catch (e: NumberFormatException) {
        this.toInt().toString()
    }

fun Int.toPlusMinus(): String =
    when {
        this > 0 -> "+"
        this < 0 -> "-"
        else -> ""
    }


/** 소수점 둘째자리까지 반올림 */
fun Double.roundToTwoDecimals(): Double = round(this * 100) / 100

