package com.leecoder.stockchart.model.stock

data class StockTick(
    val mkscShrnIscd: String?,            // 단축코드
    val stckCntgHour: String?,            // 주식 체결 시간
    val stckPrpr: String?,                   // 주식 현재가 (체결가격)
    val prdyVrssSign: String?,            // 전일 대비 부호
    val prdyVrss: String?,                   // 전일 대비
    val prdyCtrt: String?,                // 전일 대비율
    val wghnAvrgStckPrc: String?,         // 가중 평균 주식 가격
    val stckOprc: String?,                   // 시가
    val stckHgpr: String?,                   // 최고가
    val stckLwpr: String?,                   // 최저가
    val askp1: String?,                      // 매도호가1
    val bidp1: String?,                      // 매수호가1
    val cntgVol: String?,                   // 체결 거래량
    val acmlVol: String?,                   // 누적 거래량
    val acmlTrPbmn: String?,                // 누적 거래 대금
    val selnCntgCsnu: String?,               // 매도 체결 건수
    val shnuCntgCsnu: String?,               // 매수 체결 건수
    val ntbyCntgCsnu: String?,               // 순매수 체결 건수
    val cttr: String?,                    // 체결강도
    val selnCntgSmtn: String?,              // 총 매도 수량
    val shnuCntgSmtn: String?,              // 총 매수 수량
    val ccldDvsn: String?,                // 체결구분
    val shnuRate: String?,                // 매수비율
    val prdyVolVrssAcmlVolRate: String?,  // 전일 거래량 대비 등락율
    val oprcHour: String?,                // 시가 시간
    val oprcVrssPrprSign: String?,        // 시가대비구분
    val oprcVrssPrpr: String?,               // 시가대비
    val hgprHour: String?,                // 최고가 시간
    val hgprVrssPrprSign: String?,        // 고가대비구분
    val hgprVrssPrpr: String?,               // 고가대비
    val lwprHour: String?,                // 최저가 시간
    val lwprVrssPrprSign: String?,        // 저가대비구분
    val lwprVrssPrpr: String?,               // 저가대비
    val bsopDate: String?,                // 영업 일자
    val newMkopClsCode: String?,          // 신 장운영 구분 코드
    val trhtYn: String?,                  // 거래정지 여부
    val askpRsqn1: String?,                 // 매도호가 잔량1
    val bidpRsqn1: String?,                 // 매수호가 잔량1
    val totalAskpRsqn: String?,             // 총 매도호가 잔량
    val totalBidpRsqn: String?,             // 총 매수호가 잔량
    val volTnrt: String?,                 // 거래량 회전율
    val prdySmnsHourAcmlVol: String?,       // 전일 동시간 누적 거래량
    val prdySmnsHourAcmlVolRate: String?, // 전일 동시간 누적 거래량 비율
    val hourClsCode: String?,             // 시간 구분 코드
    val mrktTrtmClsCode: String?,         // 임의종료구분코드
    val viStndPrc: String?                   // 정적VI발동기준가
)