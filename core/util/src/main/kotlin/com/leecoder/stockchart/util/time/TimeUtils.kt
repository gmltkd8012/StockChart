package com.leecoder.stockchart.util.time

import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId

object TimeUtils {

    fun millisToDate(millis: Long): LocalDate =
        Instant.ofEpochMilli(millis)
            .atZone(ZoneId.of("Asia/Seoul"))
            .toLocalDate()
}