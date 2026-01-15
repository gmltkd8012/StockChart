package com.leecoder.stockchart.util.calculator

import com.leecoder.stockchart.model.room.BollingerData
import com.leecoder.stockchart.util.extension.roundToTwoDecimals
import kotlin.math.pow
import kotlin.math.round
import kotlin.math.sqrt

class BollingerCalculator(
    private val period: Int = 20,
    private val k: Double = 2.0,
) {
    fun calculate(
        code: String,
        name: String = "",
        prices: List<Double>
    ): BollingerData {
        // 데이터 개수가 period보다 적으면 계산 불가
        if (prices.size < period) {
            return BollingerData(
                code = code,
                name = name,
                middle = -1.0,
                upper = -1.0,
                lower = -1.0,
            )
        }

        // 최근 period일 데이터만 사용
        val recent = prices.takeLast(period)

        // 평균 (중심선 = 20기간 단순이동평균)
        val avg = recent.average()

        // 모집단 표준편차 (N으로 나눔 - 볼린저 밴드 표준 공식)
        val variance = recent.map { (it - avg).pow(2) }.sum() / period
        val std = sqrt(variance)

        // 밴드 계산 (소수점 둘째자리까지)
        return BollingerData(
            code = code,
            name = name,
            middle = (avg).roundToTwoDecimals(),
            upper = (avg + k * std).roundToTwoDecimals(),
            lower = (avg - k * std).roundToTwoDecimals(),
        )
    }
}