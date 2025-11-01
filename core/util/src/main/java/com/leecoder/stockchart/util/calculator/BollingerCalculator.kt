package com.leecoder.stockchart.util.calculator

import kotlin.math.pow
import kotlin.math.sqrt

data class BollingerResult(
    val middle: Int,
    val upper: Int,
    val lower: Int,
)

class BollingerCalculator(
    private val period: Int = 20,
    private val k: Double = 2.0,
) {
    fun calculate(prices: List<Int>): BollingerResult? {
        // 데이터 개수가 period보다 적으면 계산 불가
        if (prices.size < period) return null

        // 최근 period일 데이터만 사용
        val recent = prices.take(20)

        // 평균
        val avg = recent.average()

        // 표준편차
        val std = sqrt(recent.map { (it - avg).pow(2) }.average())

        // 밴드 계산
        return BollingerResult(
            middle = avg.toInt(),
            upper = (avg + k * std).toInt(),
            lower = (avg - k * std).toInt()
        )
    }
}