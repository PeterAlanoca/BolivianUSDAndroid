package com.bolivianusd.app.core.util

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun formatterDate(value: String): String {
    return try {
        var safe = value.replace(Regex(":(\\d\\d)\$"), "$1")
        safe = safe.replace(Regex("\\.(\\d{3})\\d*"), ".$1")
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.getDefault())
        val outputFormat = SimpleDateFormat("dd/MM/yy HH:mm:ss a", Locale.getDefault())
        val date = inputFormat.parse(safe)
        outputFormat.format(date)
    } catch (e: Exception) {
        e.printStackTrace()
        SimpleDateFormat("dd/MM/yy HH:mm:ss a", Locale.getDefault()).format(Date())
    }
}