package com.leecoder.stockchart.work.worker

import com.leecoder.stockchart.datastore.const.DataStoreConst
import org.junit.Assert.assertEquals
import org.junit.Test
import java.text.SimpleDateFormat
import java.util.Locale

/**
 * NasdaqTradeCodeWorker 테스트
 *
 * 시간대별 트레이드 코드 반환 테스트:
 * - 주간 (10:00 ~ 18:00): RBAQ
 * - 야간 (그 외 시간): DNAS
 */
class NasdaqTradeCodeWorkerTest {

    private val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.KOREA)

    /**
     * 가상 시간을 입력받아 해당 시간의 트레이드 코드를 확인하는 테스트
     */
    @Test
    fun `주간 시간 17시에는 RBAQ 코드 반환`() {
        // Given: 2025-12-18 17:00 (주간 시간대)
        val inputDateTime = "2025-12-18 17:00"
        val timeMillis = dateFormat.parse(inputDateTime)!!.time

        // When: Worker의 트레이드 코드 판단 로직 실행
        val resultCode = NasdaqTradeCodeWorker.getTradeCodeByTime(timeMillis)

        // Then: 주간 코드(RBAQ) 반환
        assertEquals(DataStoreConst.ValueConst.NASDAQ_DAY_CODE, resultCode)
        println("입력 시간: $inputDateTime -> 밀리초: $timeMillis -> 결과 코드: $resultCode")
    }

    @Test
    fun `주간 시작 10시에는 RBAQ 코드 반환`() {
        // Given: 2025-12-18 10:00 (주간 시작)
        val inputDateTime = "2025-12-18 10:00"
        val timeMillis = dateFormat.parse(inputDateTime)!!.time

        // When
        val resultCode = NasdaqTradeCodeWorker.getTradeCodeByTime(timeMillis)

        // Then
        assertEquals(DataStoreConst.ValueConst.NASDAQ_DAY_CODE, resultCode)
        println("입력 시간: $inputDateTime -> 밀리초: $timeMillis -> 결과 코드: $resultCode")
    }

    @Test
    fun `야간 시작 18시에는 DNAS 코드 반환`() {
        // Given: 2025-12-18 18:00 (야간 시작)
        val inputDateTime = "2025-12-18 18:00"
        val timeMillis = dateFormat.parse(inputDateTime)!!.time

        // When
        val resultCode = NasdaqTradeCodeWorker.getTradeCodeByTime(timeMillis)

        // Then: 야간 코드(DNAS) 반환
        assertEquals(DataStoreConst.ValueConst.NASDAQ_NIGHT_CODE, resultCode)
        println("입력 시간: $inputDateTime -> 밀리초: $timeMillis -> 결과 코드: $resultCode")
    }

    @Test
    fun `야간 시간 22시에는 DNAS 코드 반환`() {
        // Given: 2025-12-18 22:00 (야간)
        val inputDateTime = "2025-12-18 22:00"
        val timeMillis = dateFormat.parse(inputDateTime)!!.time

        // When
        val resultCode = NasdaqTradeCodeWorker.getTradeCodeByTime(timeMillis)

        // Then
        assertEquals(DataStoreConst.ValueConst.NASDAQ_NIGHT_CODE, resultCode)
        println("입력 시간: $inputDateTime -> 밀리초: $timeMillis -> 결과 코드: $resultCode")
    }

    @Test
    fun `새벽 3시에는 DNAS 코드 반환`() {
        // Given: 2025-12-18 03:00 (새벽 야간)
        val inputDateTime = "2025-12-18 03:00"
        val timeMillis = dateFormat.parse(inputDateTime)!!.time

        // When
        val resultCode = NasdaqTradeCodeWorker.getTradeCodeByTime(timeMillis)

        // Then
        assertEquals(DataStoreConst.ValueConst.NASDAQ_NIGHT_CODE, resultCode)
        println("입력 시간: $inputDateTime -> 밀리초: $timeMillis -> 결과 코드: $resultCode")
    }

    @Test
    fun `오전 9시 59분에는 DNAS 코드 반환`() {
        // Given: 2025-12-18 09:59 (주간 직전)
        val inputDateTime = "2025-12-18 09:59"
        val timeMillis = dateFormat.parse(inputDateTime)!!.time

        // When
        val resultCode = NasdaqTradeCodeWorker.getTradeCodeByTime(timeMillis)

        // Then: 아직 10시 전이므로 야간 코드
        assertEquals(DataStoreConst.ValueConst.NASDAQ_NIGHT_CODE, resultCode)
        println("입력 시간: $inputDateTime -> 밀리초: $timeMillis -> 결과 코드: $resultCode")
    }

    @Test
    fun `오후 17시 59분에는 RBAQ 코드 반환`() {
        // Given: 2025-12-18 17:59 (주간 마지막)
        val inputDateTime = "2025-12-18 17:59"
        val timeMillis = dateFormat.parse(inputDateTime)!!.time

        // When
        val resultCode = NasdaqTradeCodeWorker.getTradeCodeByTime(timeMillis)

        // Then: 아직 18시 전이므로 주간 코드
        assertEquals(DataStoreConst.ValueConst.NASDAQ_DAY_CODE, resultCode)
        println("입력 시간: $inputDateTime -> 밀리초: $timeMillis -> 결과 코드: $resultCode")
    }

    /**
     * 사용자 요청대로 가상 시간을 설정하여 테스트하는 예시 함수
     * 1. 가상의 시간을 입력 ex) 2025-12-18 17:00
     * 2. milliseconds 로 변환
     * 3. worker 로직 실행
     * 4. 실행결과 저장되는 코드 확인
     */
    @Test
    fun `사용자 지정 시간 테스트`() {
        // 1. 가상의 시간 입력
        val inputDateTime = "2025-12-18 17:00"

        // 2. milliseconds 로 변환
        val timeMillis = dateFormat.parse(inputDateTime)!!.time

        // 3. Worker 로직 실행 (트레이드 코드 판단)
        val resultCode = NasdaqTradeCodeWorker.getTradeCodeByTime(timeMillis)

        // 4. 결과 확인
        println("==========================================")
        println("1. 입력 시간: $inputDateTime")
        println("2. 밀리초 변환: $timeMillis")
        println("3. Worker 실행 결과")
        println("4. 저장되는 코드: $resultCode")
        println("==========================================")

        // 17:00은 주간(10:00~18:00)이므로 RBAQ 코드
        assertEquals("RBAQ", resultCode)
    }
}
