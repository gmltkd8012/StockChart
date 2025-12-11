package com.leecoder.stockchart.model.request

data class CurrentPriceRequest(
    val authorization: String, // 인증 토큰
    val auth: String = "", // 사용자 권한 정보 (공백 입력)
    val excd: String, // 거래소 코드
    val symb: String, // 종목 코드
)
