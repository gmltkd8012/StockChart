package com.leecoder.stockchart.util.extension

import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

fun String.isJsonHuristic(): Boolean =
    this.startsWith("{")|| this.startsWith("[")


fun String.convertToDouble(): Double =
    this.replace(",", "").toDouble()


fun String.convertDate(): String {
    val inputFormatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss")
    val outputFormatter= DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")

    return try {
        LocalDateTime.parse(this, inputFormatter)
            .format(outputFormatter)
    } catch (e: Exception) {
        val now = LocalDateTime.ofInstant(
            Instant.ofEpochMilli(System.currentTimeMillis()),
            ZoneId.systemDefault(),
        )
        now.format(outputFormatter)
    }
}