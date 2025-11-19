package com.leecoder.stockchart.util.extension

import java.text.DecimalFormat

fun Double.toCurrency(): String =
    try {
        DecimalFormat("#,###").format(this)
    } catch (e: NumberFormatException) {
        this.toString()
    }