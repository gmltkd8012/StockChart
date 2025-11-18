package com.leecoder.stockchart.model.stock

import kotlinx.serialization.SerialName

data class CurrentPriceNasdaqData(
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

data class TimeItemChartPriceNasdaqInfoData(
    val rsym: String, // 실시간종목코드
    val zdiv: String, // 소수점자리수
    val stim: String, // 장시작현지시간
    val etim: String, // 장종료현지시간
    val sktm: String, // 장시작한국시간
    val ektm: String, // 장종료한국시간
    val next: String, // 다음가능여부
    val more: String, // 추가데이타여부
    val nrec: String, // 레코드갯수
) //

data class TimeItemChartPriceNasdaqDetailData(
    val tymd: String, // 현지영업일자
    val xymd: String, // 현지기준일자
    val xhms: String, // 현지기준시간
    val kymd: String, // 현지기준일자
    val khms: String, // 한국기준시간
    val open: String, // 시가
    val high: String, // 고가
    val low: String,  // 저가
    val last: String, // 종가
    val evol: String, // 체결량
    val eamt: String, // 체결대금
)
