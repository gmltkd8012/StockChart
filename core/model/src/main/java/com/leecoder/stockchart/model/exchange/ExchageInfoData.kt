package com.leecoder.stockchart.model.exchange

import kotlinx.serialization.SerialName

data class ExchangeInfoData(
    val result: Int,
    val curUnit: String,
    val curNm: String,
    val ttb: String,
    val tts: String,
    val dealBasR: String,       // 매매 기준율
    val bkpr: String,           // 장부 가격
    val yyEfeeR: String,        // 년환가료율
    val tenDdEffeR: String,     // 10일환가료율
    val kftcDealBasR: String,   // 서울외국환중개 매매기준율
    val kftcBkpr: String,       // 서울외국환중개 장부가격
)
