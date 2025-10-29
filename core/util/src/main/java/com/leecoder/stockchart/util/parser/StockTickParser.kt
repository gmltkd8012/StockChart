package com.leecoder.stockchart.util.parser

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

    fun parse(raw: String, delimiter: String = "^"): StockTick {
        val parts = raw.split(delimiter)

        return StockTick(
            mkscShrnIscd = parts.getOrNull(FieldIndex.MKSC_SHRN_ISCD.idx),
            stckCntgHour = parts.getOrNull(FieldIndex.STCK_CNTG_HOUR.idx),
            stckPrpr = parts.getOrNull(FieldIndex.STCK_PRPR.idx),
            prdyVrssSign = parts.getOrNull(FieldIndex.PRDY_VRSS_SIGN.idx),
            prdyVrss = parts.getOrNull(FieldIndex.PRDY_VRSS.idx),
            prdyCtrt = parts.getOrNull(FieldIndex.PRDY_CTRT.idx),
            wghnAvrgStckPrc = parts.getOrNull(FieldIndex.WGHN_AVRG_STCK_PRC.idx),
            stckOprc = parts.getOrNull(FieldIndex.STCK_OPRC.idx),
            stckHgpr = parts.getOrNull(FieldIndex.STCK_HGPR.idx),
            stckLwpr = parts.getOrNull(FieldIndex.STCK_LWPR.idx),
            askp1 = parts.getOrNull(FieldIndex.ASKP1.idx),
            bidp1 = parts.getOrNull(FieldIndex.BIDP1.idx),
            cntgVol = parts.getOrNull(FieldIndex.CNTG_VOL.idx),
            acmlVol = parts.getOrNull(FieldIndex.ACML_VOL.idx),
            acmlTrPbmn = parts.getOrNull(FieldIndex.ACML_TR_PBMN.idx),
            selnCntgCsnu = parts.getOrNull(FieldIndex.SELN_CNTG_CSNU.idx),
            shnuCntgCsnu = parts.getOrNull(FieldIndex.SHNU_CNTG_CSNU.idx),
            ntbyCntgCsnu = parts.getOrNull(FieldIndex.NTBY_CNTG_CSNU.idx),
            cttr = parts.getOrNull(FieldIndex.CTTR.idx),
            selnCntgSmtn = parts.getOrNull(FieldIndex.SELN_CNTG_SMTN.idx),
            shnuCntgSmtn = parts.getOrNull(FieldIndex.SHNU_CNTG_SMTN.idx),
            ccldDvsn = parts.getOrNull(FieldIndex.CCLD_DVSN.idx),
            shnuRate = parts.getOrNull(FieldIndex.SHNU_RATE.idx),
            prdyVolVrssAcmlVolRate = parts.getOrNull(FieldIndex.PRDY_VOL_VRSS_ACML_VOL_RATE.idx),
            oprcHour = parts.getOrNull(FieldIndex.OPRC_HOUR.idx),
            oprcVrssPrprSign = parts.getOrNull(FieldIndex.OPRC_VRSS_PRPR_SIGN.idx),
            oprcVrssPrpr = parts.getOrNull(FieldIndex.OPRC_VRSS_PRPR.idx),
            hgprHour = parts.getOrNull(FieldIndex.HGPR_HOUR.idx),
            hgprVrssPrprSign = parts.getOrNull(FieldIndex.HGPR_VRSS_PRPR_SIGN.idx),
            hgprVrssPrpr = parts.getOrNull(FieldIndex.HGPR_VRSS_PRPR.idx),
            lwprHour = parts.getOrNull(FieldIndex.LWPR_HOUR.idx),
            lwprVrssPrprSign = parts.getOrNull(FieldIndex.LWPR_VRSS_PRPR_SIGN.idx),
            lwprVrssPrpr = parts.getOrNull(FieldIndex.LWPR_VRSS_PRPR.idx),
            bsopDate = parts.getOrNull(FieldIndex.BSOP_DATE.idx),
            newMkopClsCode = parts.getOrNull(FieldIndex.NEW_MKOP_CLS_CODE.idx),
            trhtYn = parts.getOrNull(FieldIndex.TRHT_YN.idx),
            askpRsqn1 = parts.getOrNull(FieldIndex.ASKP_RSQN1.idx),
            bidpRsqn1 = parts.getOrNull(FieldIndex.BIDP_RSQN1.idx),
            totalAskpRsqn = parts.getOrNull(FieldIndex.TOTAL_ASKP_RSQN.idx),
            totalBidpRsqn = parts.getOrNull(FieldIndex.TOTAL_BIDP_RSQN.idx),
            volTnrt = parts.getOrNull(FieldIndex.VOL_TNRT.idx),
            prdySmnsHourAcmlVol = parts.getOrNull(FieldIndex.PRDY_SMNS_HOUR_ACML_VOL.idx),
            prdySmnsHourAcmlVolRate = parts.getOrNull(FieldIndex.PRDY_SMNS_HOUR_ACML_VOL_RATE.idx),
            hourClsCode = parts.getOrNull(FieldIndex.HOUR_CLS_CODE.idx),
            mrktTrtmClsCode = parts.getOrNull(FieldIndex.MRKT_TRTM_CLS_CODE.idx),
            viStndPrc = parts.getOrNull(FieldIndex.VI_STND_PRC.idx),
        )
    }
}