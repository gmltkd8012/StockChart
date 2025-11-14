package com.leecoder.stockchart.util.time

import android.util.Log
import java.time.Duration
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZonedDateTime


object ScheduleUtil {
    private val ZONE: ZoneId = ZoneId.of("Asia/Seoul")

    private val KOS_TIME: LocalTime = LocalTime.of(9, 20)
    private val NAS_TIME: LocalTime = LocalTime.of(22, 20)

    fun currentTargetOption(): String {
        val now = ZonedDateTime.now(ZONE)

        val kosOpenTime = now
            .withHour(KOS_TIME.hour)
            .withMinute(KOS_TIME.minute)
            .withSecond(0)
            .withNano(0)

        val nasOpenTime = now
            .withHour(NAS_TIME.hour)
            .withMinute(NAS_TIME.minute)
            .withSecond(0)
            .withNano(0)

        return when {
            now.isBefore(kosOpenTime) -> "nasdaq"
            now.isBefore(nasOpenTime) -> "kospi"
            else -> "nasdaq"
        }
    }

    fun caculatorDelayMillisToNextTarget(): Pair<String, Long> {
        val now = ZonedDateTime.now(ZONE)
        val (targetName, targetMills) = nextTargetDateTimeNow()
        val duration = Duration.between(now, targetMills)
        return targetName to duration.toMillis().coerceAtLeast(0L)
    }

    private fun nextTargetDateTimeNow(): Pair<String, ZonedDateTime> {
        val now = ZonedDateTime.now(ZONE)

        val kosOpenTime = now
            .withHour(KOS_TIME.hour)
            .withMinute(KOS_TIME.minute)
            .withSecond(0)
            .withNano(0)

        val nasOpenTime = now
            .withHour(NAS_TIME.hour)
            .withMinute(NAS_TIME.minute)
            .withSecond(0)
            .withNano(0)

        return when {
            now.isBefore(kosOpenTime) -> "kospi" to kosOpenTime
            now.isBefore(nasOpenTime) -> "nasdaq" to nasOpenTime
            else -> "kospi" to (kosOpenTime.plusDays(1))
        }
    }
}