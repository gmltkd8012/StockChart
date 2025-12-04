package com.leecoder.stockchart.util.extension

fun String.isJsonHuristic(): Boolean =
    this.startsWith("{")|| this.startsWith("[")


fun String.convertToDouble(): Double =
    this.replace(",", "").toDouble()