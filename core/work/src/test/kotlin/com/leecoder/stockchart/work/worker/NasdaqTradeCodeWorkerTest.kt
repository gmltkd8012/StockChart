package com.leecoder.stockchart.work.worker

import com.leecoder.stockchart.datastore.const.DataStoreConst
import org.junit.Assert.assertEquals
import org.junit.Test
import java.text.SimpleDateFormat
import java.util.Locale

/**
 * NasdaqTradeCodeWorker 테스트
 *
 * 트레이드 코드 (WebSocket 연동용):
 * - 주간 (10:00 ~ 18:00): RBAQ
 * - 야간 (그 외 시간): DNAS
 *
 * 시장 세션 (UI 표시용):
 * - 주간거래: 10:00 ~ 18:00
 * - 프리마켓: 18:00 ~ 23:30
 * - 정규장: 23:30 ~ 06:00 (익일)
 * - 애프터마켓: 06:00 ~ 10:00
 */
class NasdaqTradeCodeWorkerTest {

    private val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.KOREA)

    // =============================================
    // 트레이드 코드 테스트 (WebSocket 연동용)
    // =============================================

    @Test
    fun `주간 시간 17시에는 RBAQ 코드 반환`() {
        val inputDateTime = "2025-12-18 17:00"
        val timeMillis = dateFormat.parse(inputDateTime)!!.time
        val resultCode = NasdaqTradeCodeWorker.getTradeCodeByTime(timeMillis)
        assertEquals(DataStoreConst.ValueConst.NASDAQ_DAY_CODE, resultCode)
        println("입력 시간: $inputDateTime -> 트레이드 코드: $resultCode")
    }

    @Test
    fun `주간 시작 10시에는 RBAQ 코드 반환`() {
        val inputDateTime = "2025-12-18 10:00"
        val timeMillis = dateFormat.parse(inputDateTime)!!.time
        val resultCode = NasdaqTradeCodeWorker.getTradeCodeByTime(timeMillis)
        assertEquals(DataStoreConst.ValueConst.NASDAQ_DAY_CODE, resultCode)
    }

    @Test
    fun `야간 시작 18시에는 DNAS 코드 반환`() {
        val inputDateTime = "2025-12-18 18:00"
        val timeMillis = dateFormat.parse(inputDateTime)!!.time
        val resultCode = NasdaqTradeCodeWorker.getTradeCodeByTime(timeMillis)
        assertEquals(DataStoreConst.ValueConst.NASDAQ_NIGHT_CODE, resultCode)
    }

    // =============================================
    // 시장 세션 테스트 (UI 표시용)
    // =============================================

    @Test
    fun `주간거래 시간 15시에는 DAY_TRADING 세션 반환`() {
        val inputDateTime = "2025-12-18 15:00"
        val timeMillis = dateFormat.parse(inputDateTime)!!.time
        val resultSession = NasdaqTradeCodeWorker.getMarketSessionByTime(timeMillis)
        assertEquals(DataStoreConst.ValueConst.SESSION_DAY_TRADING, resultSession)
        println("입력 시간: $inputDateTime -> 시장 세션: $resultSession")
    }

    @Test
    fun `주간거래 시작 10시에는 DAY_TRADING 세션 반환`() {
        val inputDateTime = "2025-12-18 10:00"
        val timeMillis = dateFormat.parse(inputDateTime)!!.time
        val resultSession = NasdaqTradeCodeWorker.getMarketSessionByTime(timeMillis)
        assertEquals(DataStoreConst.ValueConst.SESSION_DAY_TRADING, resultSession)
    }

    @Test
    fun `프리마켓 시작 18시에는 PRE_MARKET 세션 반환`() {
        val inputDateTime = "2025-12-18 18:00"
        val timeMillis = dateFormat.parse(inputDateTime)!!.time
        val resultSession = NasdaqTradeCodeWorker.getMarketSessionByTime(timeMillis)
        assertEquals(DataStoreConst.ValueConst.SESSION_PRE_MARKET, resultSession)
        println("입력 시간: $inputDateTime -> 시장 세션: $resultSession")
    }

    @Test
    fun `프리마켓 시간 21시에는 PRE_MARKET 세션 반환`() {
        val inputDateTime = "2025-12-18 21:00"
        val timeMillis = dateFormat.parse(inputDateTime)!!.time
        val resultSession = NasdaqTradeCodeWorker.getMarketSessionByTime(timeMillis)
        assertEquals(DataStoreConst.ValueConst.SESSION_PRE_MARKET, resultSession)
    }

    @Test
    fun `정규장 시작 23시 30분에는 REGULAR 세션 반환`() {
        val inputDateTime = "2025-12-18 23:30"
        val timeMillis = dateFormat.parse(inputDateTime)!!.time
        val resultSession = NasdaqTradeCodeWorker.getMarketSessionByTime(timeMillis)
        assertEquals(DataStoreConst.ValueConst.SESSION_REGULAR, resultSession)
        println("입력 시간: $inputDateTime -> 시장 세션: $resultSession")
    }

    @Test
    fun `정규장 새벽 2시에는 REGULAR 세션 반환`() {
        val inputDateTime = "2025-12-18 02:00"
        val timeMillis = dateFormat.parse(inputDateTime)!!.time
        val resultSession = NasdaqTradeCodeWorker.getMarketSessionByTime(timeMillis)
        assertEquals(DataStoreConst.ValueConst.SESSION_REGULAR, resultSession)
    }

    @Test
    fun `애프터마켓 시작 6시에는 AFTER_MARKET 세션 반환`() {
        val inputDateTime = "2025-12-18 06:00"
        val timeMillis = dateFormat.parse(inputDateTime)!!.time
        val resultSession = NasdaqTradeCodeWorker.getMarketSessionByTime(timeMillis)
        assertEquals(DataStoreConst.ValueConst.SESSION_AFTER_MARKET, resultSession)
        println("입력 시간: $inputDateTime -> 시장 세션: $resultSession")
    }

    @Test
    fun `애프터마켓 시간 8시에는 AFTER_MARKET 세션 반환`() {
        val inputDateTime = "2025-12-18 08:00"
        val timeMillis = dateFormat.parse(inputDateTime)!!.time
        val resultSession = NasdaqTradeCodeWorker.getMarketSessionByTime(timeMillis)
        assertEquals(DataStoreConst.ValueConst.SESSION_AFTER_MARKET, resultSession)
    }

    // =============================================
    // 경계값 테스트
    // =============================================

    @Test
    fun `주간거래 직전 9시 59분에는 AFTER_MARKET 세션 반환`() {
        val inputDateTime = "2025-12-18 09:59"
        val timeMillis = dateFormat.parse(inputDateTime)!!.time
        val resultSession = NasdaqTradeCodeWorker.getMarketSessionByTime(timeMillis)
        assertEquals(DataStoreConst.ValueConst.SESSION_AFTER_MARKET, resultSession)
    }

    @Test
    fun `프리마켓 직전 17시 59분에는 DAY_TRADING 세션 반환`() {
        val inputDateTime = "2025-12-18 17:59"
        val timeMillis = dateFormat.parse(inputDateTime)!!.time
        val resultSession = NasdaqTradeCodeWorker.getMarketSessionByTime(timeMillis)
        assertEquals(DataStoreConst.ValueConst.SESSION_DAY_TRADING, resultSession)
    }

    @Test
    fun `정규장 직전 23시 29분에는 PRE_MARKET 세션 반환`() {
        val inputDateTime = "2025-12-18 23:29"
        val timeMillis = dateFormat.parse(inputDateTime)!!.time
        val resultSession = NasdaqTradeCodeWorker.getMarketSessionByTime(timeMillis)
        assertEquals(DataStoreConst.ValueConst.SESSION_PRE_MARKET, resultSession)
    }

    @Test
    fun `애프터마켓 직전 5시 59분에는 REGULAR 세션 반환`() {
        val inputDateTime = "2025-12-18 05:59"
        val timeMillis = dateFormat.parse(inputDateTime)!!.time
        val resultSession = NasdaqTradeCodeWorker.getMarketSessionByTime(timeMillis)
        assertEquals(DataStoreConst.ValueConst.SESSION_REGULAR, resultSession)
    }

    // =============================================
    // 종합 테스트 (트레이드 코드 + 시장 세션)
    // =============================================

    /**
     * 사용자 요청대로 가상 시간을 설정하여 테스트하는 예시 함수
     * 1. 가상의 시간을 입력 ex) 2025-12-18 17:00
     * 2. milliseconds 로 변환
     * 3. worker 로직 실행
     * 4. 실행결과 저장되는 코드 확인
     */
    @Test
    fun `사용자 지정 시간 종합 테스트`() {
        // 1. 가상의 시간 입력
        val inputDateTime = "2025-12-18 17:00"

        // 2. milliseconds 로 변환
        val timeMillis = dateFormat.parse(inputDateTime)!!.time

        // 3. Worker 로직 실행
        val tradeCode = NasdaqTradeCodeWorker.getTradeCodeByTime(timeMillis)
        val marketSession = NasdaqTradeCodeWorker.getMarketSessionByTime(timeMillis)

        // 4. 결과 확인
        println("==========================================")
        println("1. 입력 시간: $inputDateTime")
        println("2. 밀리초 변환: $timeMillis")
        println("3. Worker 실행 결과")
        println("   - 트레이드 코드 (WebSocket): $tradeCode")
        println("   - 시장 세션 (UI): $marketSession")
        println("==========================================")

        // 17:00은 주간거래(10:00~18:00)
        assertEquals("RBAQ", tradeCode)
        assertEquals(DataStoreConst.ValueConst.SESSION_DAY_TRADING, marketSession)
    }

    @Test
    fun `야간 시간 종합 테스트 - 21시`() {
        val inputDateTime = "2025-12-18 21:00"
        val timeMillis = dateFormat.parse(inputDateTime)!!.time

        val tradeCode = NasdaqTradeCodeWorker.getTradeCodeByTime(timeMillis)
        val marketSession = NasdaqTradeCodeWorker.getMarketSessionByTime(timeMillis)

        println("==========================================")
        println("입력 시간: $inputDateTime")
        println("트레이드 코드: $tradeCode (WebSocket)")
        println("시장 세션: $marketSession (UI)")
        println("==========================================")

        // 21:00은 야간(DNAS) + 프리마켓
        assertEquals("DNAS", tradeCode)
        assertEquals(DataStoreConst.ValueConst.SESSION_PRE_MARKET, marketSession)
    }

    @Test
    fun `정규장 시간 종합 테스트 - 새벽 1시`() {
        val inputDateTime = "2025-12-18 01:00"
        val timeMillis = dateFormat.parse(inputDateTime)!!.time

        val tradeCode = NasdaqTradeCodeWorker.getTradeCodeByTime(timeMillis)
        val marketSession = NasdaqTradeCodeWorker.getMarketSessionByTime(timeMillis)

        println("==========================================")
        println("입력 시간: $inputDateTime")
        println("트레이드 코드: $tradeCode (WebSocket)")
        println("시장 세션: $marketSession (UI)")
        println("==========================================")

        // 01:00은 야간(DNAS) + 정규장
        assertEquals("DNAS", tradeCode)
        assertEquals(DataStoreConst.ValueConst.SESSION_REGULAR, marketSession)
    }
}
