package com.leecoder.stockchart.util.parser

import android.util.Log
import com.leecoder.stockchart.model.stock.NasdaqTick
import com.leecoder.stockchart.model.stock.StockTick

object StockTickParser {

    private enum class KospiFieldIndex(val idx: Int) {
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

    private enum class NasdaqFieldIndex(val idx: Int) {
        RSYM(0),
        SYMB(1),
        ZDIV(2),
        TYMD(3),
        XYMD(4),
        XHMS(5),
        KYMD(6),
        KHMS(7),
        OPEN(8),
        HIGH(9),
        LOW(10),
        LAST(11),
        SIGN(12),
        DIFF(13),
        RATE(14),
        PBID(15),
        PASK(16),
        VBID(17),
        VASK(18),
        EVOL(19),
        TVOL(20),
        TAMT(21),
        BIVL(22),
        ASVL(23),
        STRN(24),
        MTYP(25)
    }

    // 안전한 변환 헬퍼들
    private fun String?.toIntSafe(): Int? = try { this?.toInt() } catch (e: NumberFormatException) { null }
    private fun String?.toLongSafe(): Long? = try { this?.toLong() } catch (e: NumberFormatException) { null }
    private fun String?.toDoubleSafe(): Double? = try { this?.toDouble() } catch (e: NumberFormatException) { null }

    fun parseKospi(raw: String, delimiter: String = "^"): List<StockTick> {
        val headPattern = "${raw.split(delimiter).first()}$delimiter"
        val parts = raw.split(headPattern.toRegex())

        return parts.map { part ->
            val spl = part.split(delimiter)
            StockTick(
                mkscShrnIscd = spl.getOrNull(KospiFieldIndex.MKSC_SHRN_ISCD.idx),
                stckCntgHour = spl.getOrNull(KospiFieldIndex.STCK_CNTG_HOUR.idx),
                stckPrpr = spl.getOrNull(KospiFieldIndex.STCK_PRPR.idx),
                prdyVrssSign = spl.getOrNull(KospiFieldIndex.PRDY_VRSS_SIGN.idx),
                prdyVrss = spl.getOrNull(KospiFieldIndex.PRDY_VRSS.idx),
                prdyCtrt = spl.getOrNull(KospiFieldIndex.PRDY_CTRT.idx),
                wghnAvrgStckPrc = spl.getOrNull(KospiFieldIndex.WGHN_AVRG_STCK_PRC.idx),
                stckOprc = spl.getOrNull(KospiFieldIndex.STCK_OPRC.idx),
                stckHgpr = spl.getOrNull(KospiFieldIndex.STCK_HGPR.idx),
                stckLwpr = spl.getOrNull(KospiFieldIndex.STCK_LWPR.idx),
                askp1 = spl.getOrNull(KospiFieldIndex.ASKP1.idx),
                bidp1 = spl.getOrNull(KospiFieldIndex.BIDP1.idx),
                cntgVol = spl.getOrNull(KospiFieldIndex.CNTG_VOL.idx),
                acmlVol = spl.getOrNull(KospiFieldIndex.ACML_VOL.idx),
                acmlTrPbmn = spl.getOrNull(KospiFieldIndex.ACML_TR_PBMN.idx),
                selnCntgCsnu = spl.getOrNull(KospiFieldIndex.SELN_CNTG_CSNU.idx),
                shnuCntgCsnu = spl.getOrNull(KospiFieldIndex.SHNU_CNTG_CSNU.idx),
                ntbyCntgCsnu = spl.getOrNull(KospiFieldIndex.NTBY_CNTG_CSNU.idx),
                cttr = spl.getOrNull(KospiFieldIndex.CTTR.idx),
                selnCntgSmtn = spl.getOrNull(KospiFieldIndex.SELN_CNTG_SMTN.idx),
                shnuCntgSmtn = spl.getOrNull(KospiFieldIndex.SHNU_CNTG_SMTN.idx),
                ccldDvsn = spl.getOrNull(KospiFieldIndex.CCLD_DVSN.idx),
                shnuRate = spl.getOrNull(KospiFieldIndex.SHNU_RATE.idx),
                prdyVolVrssAcmlVolRate = spl.getOrNull(KospiFieldIndex.PRDY_VOL_VRSS_ACML_VOL_RATE.idx),
                oprcHour = spl.getOrNull(KospiFieldIndex.OPRC_HOUR.idx),
                oprcVrssPrprSign = spl.getOrNull(KospiFieldIndex.OPRC_VRSS_PRPR_SIGN.idx),
                oprcVrssPrpr = spl.getOrNull(KospiFieldIndex.OPRC_VRSS_PRPR.idx),
                hgprHour = spl.getOrNull(KospiFieldIndex.HGPR_HOUR.idx),
                hgprVrssPrprSign = spl.getOrNull(KospiFieldIndex.HGPR_VRSS_PRPR_SIGN.idx),
                hgprVrssPrpr = spl.getOrNull(KospiFieldIndex.HGPR_VRSS_PRPR.idx),
                lwprHour = spl.getOrNull(KospiFieldIndex.LWPR_HOUR.idx),
                lwprVrssPrprSign = spl.getOrNull(KospiFieldIndex.LWPR_VRSS_PRPR_SIGN.idx),
                lwprVrssPrpr = spl.getOrNull(KospiFieldIndex.LWPR_VRSS_PRPR.idx),
                bsopDate = spl.getOrNull(KospiFieldIndex.BSOP_DATE.idx),
                newMkopClsCode = spl.getOrNull(KospiFieldIndex.NEW_MKOP_CLS_CODE.idx),
                trhtYn = spl.getOrNull(KospiFieldIndex.TRHT_YN.idx),
                askpRsqn1 = spl.getOrNull(KospiFieldIndex.ASKP_RSQN1.idx),
                bidpRsqn1 = spl.getOrNull(KospiFieldIndex.BIDP_RSQN1.idx),
                totalAskpRsqn = spl.getOrNull(KospiFieldIndex.TOTAL_ASKP_RSQN.idx),
                totalBidpRsqn = spl.getOrNull(KospiFieldIndex.TOTAL_BIDP_RSQN.idx),
                volTnrt = spl.getOrNull(KospiFieldIndex.VOL_TNRT.idx),
                prdySmnsHourAcmlVol = spl.getOrNull(KospiFieldIndex.PRDY_SMNS_HOUR_ACML_VOL.idx),
                prdySmnsHourAcmlVolRate = spl.getOrNull(KospiFieldIndex.PRDY_SMNS_HOUR_ACML_VOL_RATE.idx),
                hourClsCode = spl.getOrNull(KospiFieldIndex.HOUR_CLS_CODE.idx),
                mrktTrtmClsCode = spl.getOrNull(KospiFieldIndex.MRKT_TRTM_CLS_CODE.idx),
                viStndPrc = spl.getOrNull(KospiFieldIndex.VI_STND_PRC.idx),
            )
        }
    }

    // raw 문자열을 delimiter로 분리해 NasdaqTick 리스트로 변환
    fun parseNasdaq(raw: String, delimiter: String = "^"): List<NasdaqTick> {
        // Kospi 쪽과 동일한 헤드 패턴 분리 방식 사용
        val headPattern = "${raw.split(delimiter).first()}$delimiter"
        val parts = raw.split(headPattern.toRegex())

        return parts.map { part ->
            val spl = part.split(delimiter)
            NasdaqTick(
                rsym = spl.getOrNull(NasdaqFieldIndex.RSYM.idx),
                symb = spl.getOrNull(NasdaqFieldIndex.SYMB.idx),
                zdiv = spl.getOrNull(NasdaqFieldIndex.ZDIV.idx),
                tymd = spl.getOrNull(NasdaqFieldIndex.TYMD.idx),
                xymd = spl.getOrNull(NasdaqFieldIndex.XYMD.idx),
                xhms = spl.getOrNull(NasdaqFieldIndex.XHMS.idx),
                kymd = spl.getOrNull(NasdaqFieldIndex.KYMD.idx),
                khms = spl.getOrNull(NasdaqFieldIndex.KHMS.idx),
                open = spl.getOrNull(NasdaqFieldIndex.OPEN.idx),
                high = spl.getOrNull(NasdaqFieldIndex.HIGH.idx),
                low = spl.getOrNull(NasdaqFieldIndex.LOW.idx),
                last = spl.getOrNull(NasdaqFieldIndex.LAST.idx),
                sign = spl.getOrNull(NasdaqFieldIndex.SIGN.idx),
                diff = spl.getOrNull(NasdaqFieldIndex.DIFF.idx),
                rate = spl.getOrNull(NasdaqFieldIndex.RATE.idx),
                pbid = spl.getOrNull(NasdaqFieldIndex.PBID.idx),
                pask = spl.getOrNull(NasdaqFieldIndex.PASK.idx),
                vbid = spl.getOrNull(NasdaqFieldIndex.VBID.idx),
                vask = spl.getOrNull(NasdaqFieldIndex.VASK.idx),
                evol = spl.getOrNull(NasdaqFieldIndex.EVOL.idx),
                tvol = spl.getOrNull(NasdaqFieldIndex.TVOL.idx),
                tamt = spl.getOrNull(NasdaqFieldIndex.TAMT.idx),
                bivl = spl.getOrNull(NasdaqFieldIndex.BIVL.idx),
                asvl = spl.getOrNull(NasdaqFieldIndex.ASVL.idx),
                strn = spl.getOrNull(NasdaqFieldIndex.STRN.idx),
                mtyp = spl.getOrNull(NasdaqFieldIndex.MTYP.idx)
            )
        }
    }
}