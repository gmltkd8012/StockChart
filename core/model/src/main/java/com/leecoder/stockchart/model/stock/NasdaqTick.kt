package com.leecoder.stockchart.model.stock

data class NasdaqTick(
    val rsym: String?,   // 실시간종목코드
    val symb: String?,   // 종목코드
    val zdiv: String?,   // 수수점자리수
    val tymd: String?,   // 현지영업일자
    val xymd: String?,   // 현지일자
    val xhms: String?,   // 현지시간
    val kymd: String?,   // 한국일자
    val khms: String?,   // 한국시간
    val open: String?,   // 시가
    val high: String?,   // 고가
    val low: String?,    // 저가
    val last: String?,   // 현재가
    val sign: String?,   // 대비구분
    val diff: String?,   // 전일대비
    val rate: String?,   // 등락율
    val pbid: String?,   // 매수호가
    val pask: String?,   // 매도호가
    val vbid: String?,   // 매수잔량
    val vask: String?,   // 매도잔량
    val evol: String?,   // 체결량
    val tvol: String?,   // 거래량
    val tamt: String?,   // 거래대금
    val bivl: String?,   // 매도체결량
    val asvl: String?,   // 매수체결량
    val strn: String?,   // 체결강도
    val mtyp: String?    // 시장구분 1:장중,2:장전,3:장후
)