package com.leecoder.stockchart.util.calculator

import com.leecoder.stockchart.model.room.BollingerData
import kotlin.math.pow
import kotlin.math.sqrt

class BollingerCalculator(
    private val period: Int = 20,
    private val k: Double = 2.0,
) {
    fun calculate(
        code: String,
        name: String,
        prices: List<Int>
    ): BollingerData {
        // 데이터 개수가 period보다 적으면 계산 불가
        if (prices.size < period) {
            return BollingerData(
                code = code,
                name = name,
                middle = -1,
                upper = -1,
                lower = -1,
            )
        }

        // 최근 period일 데이터만 사용
        val recent = prices.take(20)

        // 평균
        val avg = recent.average()

        // 표준편차
        val std = sqrt(recent.map { (it - avg).pow(2) }.average())

        // 밴드 계산
        return BollingerData(
            code = code,
            name = name,
            middle = avg.toInt(),
            upper = (avg + k * std).toInt(),
            lower = (avg - k * std).toInt()
        )
    }
}