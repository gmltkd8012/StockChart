package com.leecoder.stockchart.util.parser

import android.util.Log
import com.leecoder.stockchart.model.stock.StockTick

object StockTickParser {

    private enum class FieldIndex(val idx: Int) {
        MKSC_SHRN_ISCD(0),
        STCK_CNTG_HOUR(1),
        STCK_PRPR(2),
        PRDY_VRSS_SIGN(3),
        PRDY_VRSS(4),
        PRDY_CTRT(5),
        WGHN_AVRG_STCK_PRC(6),
        STCK_OPRC(7),
        STCK_HGPR(8),
        STCK_LWPR(9),
        ASKP1(10),
        BIDP1(11),
        CNTG_VOL(12),
        ACML_VOL(13),
        ACML_TR_PBMN(14),
        SELN_CNTG_CSNU(15),
        SHNU_CNTG_CSNU(16),
        NTBY_CNTG_CSNU(17),
        CTTR(18),
        SELN_CNTG_SMTN(19),
        SHNU_CNTG_SMTN(20),
        CCLD_DVSN(21),
        SHNU_RATE(22),
        PRDY_VOL_VRSS_ACML_VOL_RATE(23),
        OPRC_HOUR(24),
        OPRC_VRSS_PRPR_SIGN(25),
        OPRC_VRSS_PRPR(26),
        HGPR_HOUR(27),
        HGPR_VRSS_PRPR_SIGN(28),
        HGPR_VRSS_PRPR(29),
        LWPR_HOUR(30),
        LWPR_VRSS_PRPR_SIGN(31),
        LWPR_VRSS_PRPR(32),
        BSOP_DATE(33),
        NEW_MKOP_CLS_CODE(34),
        TRHT_YN(35),
        ASKP_RSQN1(36),
        BIDP_RSQN1(37),
        TOTAL_ASKP_RSQN(38),
        TOTAL_BIDP_RSQN(39),
        VOL_TNRT(40),
        PRDY_SMNS_HOUR_ACML_VOL(41),
        PRDY_SMNS_HOUR_ACML_VOL_RATE(42),
        HOUR_CLS_CODE(43),
        MRKT_TRTM_CLS_CODE(44),
        VI_STND_PRC(45)
    }

    // 안전한 변환 헬퍼들
    private fun String?.toIntSafe(): Int? = try { this?.toInt() } catch (e: NumberFormatException) { null }
    private fun String?.toLongSafe(): Long? = try { this?.toLong() } catch (e: NumberFormatException) { null }
    private fun String?.toDoubleSafe(): Double? = try { this?.toDouble() } catch (e: NumberFormatException) { null }

    fun parse(raw: String, delimiter: String = "^"): List<StockTick> {
        val headPattern = "${raw.split(delimiter).first()}$delimiter"
        val parts = raw.split(headPattern.toRegex())

        return parts.map { part ->
            val spl = part.split(delimiter)
            StockTick(
                mkscShrnIscd = spl.getOrNull(FieldIndex.MKSC_SHRN_ISCD.idx),
                stckCntgHour = spl.getOrNull(FieldIndex.STCK_CNTG_HOUR.idx),
                stckPrpr = spl.getOrNull(FieldIndex.STCK_PRPR.idx),
                prdyVrssSign = spl.getOrNull(FieldIndex.PRDY_VRSS_SIGN.idx),
                prdyVrss = spl.getOrNull(FieldIndex.PRDY_VRSS.idx),
                prdyCtrt = spl.getOrNull(FieldIndex.PRDY_CTRT.idx),
                wghnAvrgStckPrc = spl.getOrNull(FieldIndex.WGHN_AVRG_STCK_PRC.idx),
                stckOprc = spl.getOrNull(FieldIndex.STCK_OPRC.idx),
                stckHgpr = spl.getOrNull(FieldIndex.STCK_HGPR.idx),
                stckLwpr = spl.getOrNull(FieldIndex.STCK_LWPR.idx),
                askp1 = spl.getOrNull(FieldIndex.ASKP1.idx),
                bidp1 = spl.getOrNull(FieldIndex.BIDP1.idx),
                cntgVol = spl.getOrNull(FieldIndex.CNTG_VOL.idx),
                acmlVol = spl.getOrNull(FieldIndex.ACML_VOL.idx),
                acmlTrPbmn = spl.getOrNull(FieldIndex.ACML_TR_PBMN.idx),
                selnCntgCsnu = spl.getOrNull(FieldIndex.SELN_CNTG_CSNU.idx),
                shnuCntgCsnu = spl.getOrNull(FieldIndex.SHNU_CNTG_CSNU.idx),
                ntbyCntgCsnu = spl.getOrNull(FieldIndex.NTBY_CNTG_CSNU.idx),
                cttr = spl.getOrNull(FieldIndex.CTTR.idx),
                selnCntgSmtn = spl.getOrNull(FieldIndex.SELN_CNTG_SMTN.idx),
                shnuCntgSmtn = spl.getOrNull(FieldIndex.SHNU_CNTG_SMTN.idx),
                ccldDvsn = spl.getOrNull(FieldIndex.CCLD_DVSN.idx),
                shnuRate = spl.getOrNull(FieldIndex.SHNU_RATE.idx),
                prdyVolVrssAcmlVolRate = spl.getOrNull(FieldIndex.PRDY_VOL_VRSS_ACML_VOL_RATE.idx),
                oprcHour = spl.getOrNull(FieldIndex.OPRC_HOUR.idx),
                oprcVrssPrprSign = spl.getOrNull(FieldIndex.OPRC_VRSS_PRPR_SIGN.idx),
                oprcVrssPrpr = spl.getOrNull(FieldIndex.OPRC_VRSS_PRPR.idx),
                hgprHour = spl.getOrNull(FieldIndex.HGPR_HOUR.idx),
                hgprVrssPrprSign = spl.getOrNull(FieldIndex.HGPR_VRSS_PRPR_SIGN.idx),
                hgprVrssPrpr = spl.getOrNull(FieldIndex.HGPR_VRSS_PRPR.idx),
                lwprHour = spl.getOrNull(FieldIndex.LWPR_HOUR.idx),
                lwprVrssPrprSign = spl.getOrNull(FieldIndex.LWPR_VRSS_PRPR_SIGN.idx),
                lwprVrssPrpr = spl.getOrNull(FieldIndex.LWPR_VRSS_PRPR.idx),
                bsopDate = spl.getOrNull(FieldIndex.BSOP_DATE.idx),
                newMkopClsCode = spl.getOrNull(FieldIndex.NEW_MKOP_CLS_CODE.idx),
                trhtYn = spl.getOrNull(FieldIndex.TRHT_YN.idx),
                askpRsqn1 = spl.getOrNull(FieldIndex.ASKP_RSQN1.idx),
                bidpRsqn1 = spl.getOrNull(FieldIndex.BIDP_RSQN1.idx),
                totalAskpRsqn = spl.getOrNull(FieldIndex.TOTAL_ASKP_RSQN.idx),
                totalBidpRsqn = spl.getOrNull(FieldIndex.TOTAL_BIDP_RSQN.idx),
                volTnrt = spl.getOrNull(FieldIndex.VOL_TNRT.idx),
                prdySmnsHourAcmlVol = spl.getOrNull(FieldIndex.PRDY_SMNS_HOUR_ACML_VOL.idx),
                prdySmnsHourAcmlVolRate = spl.getOrNull(FieldIndex.PRDY_SMNS_HOUR_ACML_VOL_RATE.idx),
                hourClsCode = spl.getOrNull(FieldIndex.HOUR_CLS_CODE.idx),
                mrktTrtmClsCode = spl.getOrNull(FieldIndex.MRKT_TRTM_CLS_CODE.idx),
                viStndPrc = spl.getOrNull(FieldIndex.VI_STND_PRC.idx),
            )
        }
    }
}