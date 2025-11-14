package com.leecoder.stockchart.util.extension

import java.text.DecimalFormat

fun Int.toCurrency(): String =
    try {
        DecimalFormat("#,###").format(this)
    } catch (e: NumberFormatException) {
        this.toString()
    }