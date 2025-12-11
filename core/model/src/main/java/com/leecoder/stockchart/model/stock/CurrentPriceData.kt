package com.leecoder.stockchart.model.stock

data class CurrentPriceData(
    val rsym: String, // 실시간조회종목코드
    val zdiv: String, // 소수점자리수
    val base: String, // 전일종가
    val pvol: String, // 전일거래량
    val last: String, // 현재가
    val sign: String, // 대비기호
    val diff: String, // 대비
    val rate: String, // 등락율
    val tvol: String, // 거래량
    val tamt: String, // 거래대금
    val ordy: String, // 매수가능여부
)
