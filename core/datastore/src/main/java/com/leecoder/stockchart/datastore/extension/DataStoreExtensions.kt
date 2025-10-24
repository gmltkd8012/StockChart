package com.leecoder.stockchart.datastore.extension

import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

fun String.toMilliseconds(): Long {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
    val localDateTime = LocalDateTime.parse(this, formatter)

    return localDateTime
        .atZone(ZoneId.systemDefault())
        .toInstant()
        .toEpochMilli()
}