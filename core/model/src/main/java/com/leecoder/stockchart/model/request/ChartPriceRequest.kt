package com.leecoder.stockchart.model.request

data class ChartPriceRequest(
    val authorization: String, // 접근 토큰
    val auth: String = "", // 사용자 권한 정보 (공백 입력)
    val excd: String, // 거래소 코드
    val symb: String, // 종목 코드
    val nmin: String = "1", // 분갭 (1분봉만 조회)
    val pinc: String = "1", // 전일포함여부 (전일 포함)
    val next: String = "", // 다음 여부 (공백 입력)
    val nrec: String = "20", // 요청 갯수 (볼린저 계산에 필요한 20개 고정)
    val fill: String = "", // 미체결채움구분 (공백 입력)
    val keyb: String = "", // 조회 Buff (공백 입력)
)