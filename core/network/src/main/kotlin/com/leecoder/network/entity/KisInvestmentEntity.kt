package com.leecoder.network.entity

import com.leecoder.stockchart.model.stock.CurrentPriceData
import com.leecoder.stockchart.model.stock.DailyPriceData
import com.leecoder.stockchart.model.stock.TimeItemChartPriceData
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class KisInvestmentRquestHeader(
    @SerialName("content-type") val contentType: String?, // 컨텐츠타입
    @SerialName("authorization") val authorization: String, // 접근 토큰
    @SerialName("appkey") val appkey: String, // 앱 키
    @SerialName("appsecret") val appsecret: String, // 앱 시크릿 키
    @SerialName("personalseckey") val personalseckeypkey: String?, // 고객 식별키 [법인 필수]
    @SerialName("tr_id") val trId: String, // 거래ID
    @SerialName("tr_cont") val trCont: String?, // 연속 거래 여부 [필수 아님]
    @SerialName("custtype") val custtype: String, // 고객 타입
    @SerialName("seq_no") val seqNo: String?, // 일련 번호 [법인 필수]
    @SerialName("mac_address") val macAddress: String?, // 맥주소 [필수 아님]
    @SerialName("phone_number") val phoneNumber: String?, // 핸드폰 번호 [법인 필수]
    @SerialName("ip_addr") val ipAddr: String?, // 접속 단말 공인 IP [법인 필수]
    @SerialName("gt_uid") val gtUid: String?, // 거래 고유 번호 [법인 전용]
)

@Serializable
data class KisInvestmentResponse(
    @SerialName("header") val header: KisInvestmentResponse.Header,
    @SerialName("body") val body: KisInvestmentResponse.Body,
) {
    @Serializable
    data class Header(
        @SerialName("content-type") val contentType: String,
        @SerialName("tr_id") val trId: String,
        @SerialName("tr_cont") val trCont: String,
        @SerialName("gt_uid") val gtUid: String, // 법인 전용
    )

    @Serializable
    data class Body(
        @SerialName("rt_cd") val rtCd: String,
        @SerialName("msg_cd") val msgCd: String,
        @SerialName("msg1") val msg1: String,
        @SerialName("output") val output: List<DailyPriceResponse.Output>,
    )
}

@Serializable
data class DailyPriceResponse(
    @SerialName("output") val output: List<DailyPriceResponse.Output>,
) {
    @Serializable
    data class Output(
        @SerialName("stck_bsop_date") val stckBsopDate: String,
        @SerialName("stck_oprc") val stckOprc: String,
        @SerialName("stck_hgpr") val stckHgpr: String,
        @SerialName("stck_lwpr") val stckLwpr: String,
        @SerialName("stck_clpr") val stckClpr: String, // 주식 종가
        @SerialName("acml_vol") val acmlVol: String, // 누적 거래량
        @SerialName("prdy_vrss_vol_rate") val prdyVrssVolRate: String,
        @SerialName("prdy_vrss") val prdyVrss: String,
        @SerialName("prdy_vrss_sign") val prdyVrssSign: String,
        @SerialName("prdy_ctrt") val prdyCtrt: String,
        @SerialName("hts_frgn_ehrt") val htsFrgnEhrt: String,
        @SerialName("frgn_ntby_qty") val frgnNtbyQty: String,
        @SerialName("flng_cls_code") val flngClsCode: String,
        @SerialName("acml_prtt_rate") val acmlPrttRate: String,
    )
}

@Serializable
data class CurrentPriceResponse(
    @SerialName("output") val output: List<CurrentPriceResponse.Output>,
) {
    @Serializable
    data class Output(
        @SerialName("stck_cntg_hour") val stckCntgHour: String,
        @SerialName("stck_prpr") val stckPrpr: String,
        @SerialName("prdy_vrss") val prdyVrss: String,
        @SerialName("prdy_vrss_sign") val prdyVrssSign: String,
        @SerialName("cntg_vol") val cntgVol: String,
        @SerialName("tday_rltv") val tdayRltv: String,
        @SerialName("prdy_ctrt") val prdyCtrt: String,
    )
}

@Serializable
data class TimeItemChartPriceResponse(
    @SerialName("output1") val output1: TimeItemChartPriceResponse.Output1,
    @SerialName("output2") val output2: List<TimeItemChartPriceResponse.Output2>,
) {
    @Serializable
    data class Output1(
        @SerialName("prdy_vrss") val prdyVrss: String,
        @SerialName("prdy_vrss_sign") val prdyVrssSign: String,
        @SerialName("prdy_ctrt") val prdyCtrt: String,
        @SerialName("stck_prdy_clpr") val stckPrdyClpr: String,
        @SerialName("acml_vol") val acmlVol: String,
        @SerialName("acml_tr_pbmn") val acmlTrPbmn: String,
        @SerialName("hts_kor_isnm") val htsKorIsnm: String,
        @SerialName("stck_prpr") val stckPrpr: String,
    )

    @Serializable
    data class Output2(
        @SerialName("stck_bsop_date") val stckBsopDate: String,
        @SerialName("stck_cntg_hour") val stckCntgHour: String,
        @SerialName("stck_prpr") val stckPrpr: String,
        @SerialName("stck_oprc") val stckOprc: String,
        @SerialName("stck_hgpr") val stckHgpr: String,
        @SerialName("stck_lwpr") val stckLwpr: String,
        @SerialName("cntg_vol") val cntgVol: String,
        @SerialName("acml_tr_pbmn") val acmlTrPbmn: String,
    )
}

fun DailyPriceResponse.Output.toData() = DailyPriceData(
    stckBsopDate = stckBsopDate,
    stckClpr = stckClpr,
    acmlVol = acmlVol
)

fun CurrentPriceResponse.Output.toData() = CurrentPriceData(
    stckPrpr = stckPrpr
)

fun TimeItemChartPriceResponse.Output2.toData() = TimeItemChartPriceData(
    stckCntgHour = stckCntgHour,
    stckPrpr = stckPrpr
)