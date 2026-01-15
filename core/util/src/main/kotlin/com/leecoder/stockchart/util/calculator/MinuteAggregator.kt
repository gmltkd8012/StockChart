package com.leecoder.stockchart.util.calculator

import android.util.Log
import com.leecoder.stockchart.model.stock.MinuteBollingerResult
import com.leecoder.stockchart.model.stock.TimeItemChartPriceData
import com.leecoder.stockchart.util.log.Logger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import kotlin.math.min

class MinuteAggregator(
    val code: String,
    private val calculator: BollingerCalculator,
    private val windowSize: Int = 20,
): BollingerAggregator<TimeItemChartPriceData> {
    private val deque = ArrayDeque<TimeItemChartPriceData>(windowSize)
    private val mutex = Mutex()
    private val formatter = DateTimeFormatter.ofPattern("HHmmss")
    private var lastMinuteKey: String? = null // "HHmmss"
    private var lastMinutePrice: TimeItemChartPriceData? = null

    override suspend fun initWith(list: List<TimeItemChartPriceData>) = mutex.withLock {
        deque.clear()
        list.take(windowSize).forEach { deque.addFirst(it) }
        Logger.i("[$code] 서버에서 받은 list size: ${list.size}")
        Logger.i("[$code] deque 초기화 완료 - size: ${deque.size}, prices: ${deque.map { it.stckPrpr }}")
    }

    suspend fun onTick(
        timeString: String,
        name: String,
        price: Double
    ): MinuteBollingerResult? = mutex.withLock {
        val minuteKey = try {
            val t = LocalTime.parse(timeString, formatter)
            "%02d%02d".format(t.hour, t.minute)
        } catch (e: Exception) {
            return null
        }

        if (lastMinuteKey == null) {
            lastMinuteKey = minuteKey
            lastMinutePrice = TimeItemChartPriceData(timeString, price.toString())
        }

        if (minuteKey == lastMinuteKey) {
            lastMinutePrice = TimeItemChartPriceData(timeString, price.toString())
            return null
        } else {
            lastMinutePrice?.let {
                deque.addLast(it)
                if (deque.size > windowSize) deque.removeFirst()
            }

            lastMinuteKey = minuteKey
            lastMinutePrice = TimeItemChartPriceData(timeString, price.toString())

            // 소수점 가격도 처리 (예: "123.45" → 123)
            val parsed = deque.mapNotNull { it.stckPrpr.toDoubleOrNull() }
            val recentTimeString = minuteKey + "00"


            Logger.d("[Parsed]: $parsed")

            if (parsed.isEmpty()) {
                return MinuteBollingerResult(code, recentTimeString, null)
            }

            val bollinger = try {
                withContext(Dispatchers.Default) {
                    calculator.calculate(
                        code = code,
                        name = name,
                        prices = parsed
                    )
                }
            } catch (e: Exception) {
                null
            }

            Logger.d("[$code]: $bollinger")
            return MinuteBollingerResult(code, recentTimeString, bollinger)
        }
    }
}