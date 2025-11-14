package com.leecoder.stockchart.util.calculator


fun calculatePriceChanged(previous: Double, current: Double): Pair<String, String> {
    val diff = current - previous
    val rate = if (previous != 0.0) (diff / previous) * 100 else 0.0
    return diff.toString() to rate.toString()
}