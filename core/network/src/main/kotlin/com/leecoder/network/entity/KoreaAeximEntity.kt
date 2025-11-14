package com.leecoder.network.entity

import com.leecoder.stockchart.model.exchange.ExchangeInfoData
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ExchangeInfoEntity(
    @SerialName("result") val result: Int,
    @SerialName("cur_unit") val curUnit: String,
    @SerialName("cur_nm") val curNm: String,
    @SerialName("ttb") val ttb: String,
    @SerialName("tts") val tts: String,
    @SerialName("deal_bas_r") val dealBasR: String, // 매매 기준율
    @SerialName("bkpr") val bkpr: String, // 장부 가격
    @SerialName("yy_efee_r") val yyEfeeR: String, // 년환가료율
    @SerialName("ten_dd_efee_r") val tenDdEffeR: String, // 10일환가료율
    @SerialName("kftc_deal_bas_r") val kftcDealBasR: String, // 서울외국환중개 매매기준율
    @SerialName("kftc_bkpr") val kftcBkpr: String, // 서울외국환중개 장부가격
)

fun List<ExchangeInfoEntity>.toDataList() =
    this.map {
        ExchangeInfoData(
            result = it.result,
            curUnit = it.curUnit,
            curNm = it.curNm,
            ttb = it.ttb,
            tts = it.tts,
            dealBasR = it.dealBasR,
            bkpr = it.bkpr,
            yyEfeeR = it.yyEfeeR,
            tenDdEffeR = it.tenDdEffeR,
            kftcDealBasR = it.kftcDealBasR,
            kftcBkpr = it.kftcBkpr,
        )
    }