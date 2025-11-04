package com.leecoder.stockchart.model.bollinger

import com.leecoder.stockchart.model.room.BollingerData

data class LiveBollingerData(
    val recentTime: Int, // 최근 합계 시간 (분)
    val bollinger: BollingerData, // 볼린저 밴드 계산 결과 데이터
)
