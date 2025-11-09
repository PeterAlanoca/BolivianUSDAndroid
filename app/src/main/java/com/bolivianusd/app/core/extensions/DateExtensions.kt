package com.bolivianusd.app.core.extensions

import com.bolivianusd.app.core.util.emptyString
import java.text.SimpleDateFormat
import java.util.Locale

fun String.toMonthDayFormat(): String {
    return try {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val outputFormat = SimpleDateFormat("MM/dd", Locale.getDefault())
        val date = inputFormat.parse(this)
        outputFormat.format(date)
    } catch (e: Exception) {
        emptyString
    }
}

fun String.toReadableDate(): String {
    return try {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val outputFormat = SimpleDateFormat("dd 'de' MMMM 'de' yyyy", Locale("es", "ES"))
        val date = inputFormat.parse(this)
        outputFormat.format(date)
    } catch (e: Exception) {
        emptyString
    }
}