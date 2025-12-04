package com.leecoder.stockchart.util.extension

import java.text.DecimalFormat

fun Double.toCurrency(): String =
    try {
        DecimalFormat("#,###").format(this.toInt())
    } catch (e: NumberFormatException) {
        this.toInt().toString()
    }

