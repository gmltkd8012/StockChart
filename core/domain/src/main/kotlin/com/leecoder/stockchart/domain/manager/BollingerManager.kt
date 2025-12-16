package com.leecoder.stockchart.domain.manager

import android.util.Log
import com.leecoder.data.repository.KisInvestmentOverseasRepository
import com.leecoder.stockchart.model.stock.ChartPriceData
import com.leecoder.stockchart.model.stock.NasdaqTick
import com.leecoder.stockchart.model.stock.TimeItemChartPriceData
import com.leecoder.stockchart.util.calculator.BollingerCalculator
import com.leecoder.stockchart.util.calculator.MinuteAggregator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import java.util.concurrent.ConcurrentHashMap
import javax.inject.Inject
import javax.inject.Singleton

/**
 * 종목별 실시간 볼린저 밴드 하한가 달성 여부를 관리하는 매니저
 *
 * - 종목 추가 시 서버에서 20분봉 데이터를 가져와 MinuteAggregator 초기화
 * - 실시간 tick 수신 시 볼린저 계산 및 하한가 달성 여부 판단
 * - 하한가 달성 종목 코드 리스트를 StateFlow로 제공
 */
@Singleton
class BollingerManager @Inject constructor(
    private val kisInvestmentOverseasRepository: KisInvestmentOverseasRepository,
) {
    companion object {
        private const val TAG = "BollingerManager"
        private const val WINDOW_SIZE = 20
        private const val EXCHANGE_CODE = "NAS" // 나스닥 거래소 코드
    }

    private val calculator = BollingerCalculator(period = WINDOW_SIZE)
    private val aggregators = ConcurrentHashMap<String, MinuteAggregator>()
    private val mutex = Mutex()

    // 볼린저 하한가 달성 종목 코드 리스트
    private val _bollingerLowerAlertCodes = MutableStateFlow<Set<String>>(emptySet())
    val bollingerLowerAlertCodes: StateFlow<Set<String>> = _bollingerLowerAlertCodes.asStateFlow()

    // 종목별 현재 볼린저 하한가 (UI 표시용)
    private val _bollingerLowerPrices = MutableStateFlow<Map<String, Int>>(emptyMap())
    val bollingerLowerPrices: StateFlow<Map<String, Int>> = _bollingerLowerPrices.asStateFlow()

    /**
     * 여러 종목을 한번에 초기화
     */
    suspend fun initializeStocks(codes: List<String>) = withContext(Dispatchers.IO) {
        codes.forEach { code ->
            addStock(code)
        }
    }

    /**
     * 단일 종목 추가 및 초기화
     * 서버에서 20분봉 데이터를 가져와 MinuteAggregator를 초기화
     */
    suspend fun addStock(code: String) = withContext(Dispatchers.IO) {
        if (aggregators.containsKey(code)) {
            Log.d(TAG, "Stock $code already initialized")
            return@withContext
        }

        try {
            val chartPriceList = kisInvestmentOverseasRepository
                .getChartPriceNasdaq(EXCHANGE_CODE, code)
                .first()

            val timeItemList = chartPriceList
                .take(WINDOW_SIZE)
                .map { it.toTimeItemChartPriceData() }

            val aggregator = MinuteAggregator(code, calculator, WINDOW_SIZE)
            aggregator.initWith(timeItemList)

            aggregators[code] = aggregator
            Log.d(TAG, "Stock $code initialized with ${timeItemList.size} minute data")
        } catch (e: Exception) {
            Log.e(TAG, "Failed to initialize stock $code: ${e.message}")
            // 초기화 실패해도 빈 상태로 aggregator 생성 (이후 실시간 데이터로 채움)
            val aggregator = MinuteAggregator(code, calculator, WINDOW_SIZE)
            aggregators[code] = aggregator
        }
    }

    /**
     * 종목 제거
     */
    suspend fun removeStock(code: String) = mutex.withLock {
        aggregators.remove(code)

        // 알림 목록에서도 제거
        _bollingerLowerAlertCodes.value = _bollingerLowerAlertCodes.value - code
        _bollingerLowerPrices.value = _bollingerLowerPrices.value - code

        Log.d(TAG, "Stock $code removed")
    }

    /**
     * 실시간 tick 처리
     * 매분 정각 변경 시 볼린저 계산 및 하한가 달성 여부 판단
     *
     * @return 볼린저 하한가 도달 시 종목코드, 미도달 시 null
     */
    suspend fun processTick(tick: NasdaqTick): String? {
        val code = tick.symb ?: return null
        val timeString = tick.khms ?: return null
        val priceString = tick.last ?: return null
        val name = "" // 이름은 별도 관리

        // 가격을 정수로 변환 (소수점 제거)
        val price = priceString.toDoubleOrNull()?.toInt() ?: return null

        val aggregator = aggregators[code] ?: return null

        val result = aggregator.onTick(timeString, name, price)

        result?.bollinger?.let { bollinger ->
            val lowerBand = bollinger.lower

            // 볼린저 하한가 정보 업데이트
            if (lowerBand > 0) {
                _bollingerLowerPrices.value = _bollingerLowerPrices.value + (code to lowerBand)
            }

            // 현재가가 볼린저 하한선 이하인지 확인
            if (lowerBand > 0 && price <= lowerBand) {
                Log.i(TAG, "[$code] Bollinger lower band reached! Price: $price, Lower: $lowerBand")

                // 알림 목록에 추가
                _bollingerLowerAlertCodes.value = _bollingerLowerAlertCodes.value + code
                return code
            }
        }

        return null
    }

    /**
     * 특정 종목의 알림 상태 초기화 (사용자가 확인 후)
     */
    fun clearAlert(code: String) {
        _bollingerLowerAlertCodes.value = _bollingerLowerAlertCodes.value - code
    }

    /**
     * 모든 알림 상태 초기화
     */
    fun clearAllAlerts() {
        _bollingerLowerAlertCodes.value = emptySet()
    }

    /**
     * 모든 종목 제거 및 초기화
     */
    suspend fun clear() = mutex.withLock {
        aggregators.clear()
        _bollingerLowerAlertCodes.value = emptySet()
        _bollingerLowerPrices.value = emptyMap()
    }

    /**
     * ChartPriceData를 TimeItemChartPriceData로 변환
     */
    private fun ChartPriceData.toTimeItemChartPriceData(): TimeItemChartPriceData {
        // date 형식: YYYYMMDDHHMMSS -> HHMMSS 추출
        val timeString = if (date.length >= 14) {
            date.substring(8, 14)
        } else {
            date
        }
        return TimeItemChartPriceData(
            stckCntgHour = timeString,
            stckPrpr = last // 종가를 체결가로 사용
        )
    }
}
