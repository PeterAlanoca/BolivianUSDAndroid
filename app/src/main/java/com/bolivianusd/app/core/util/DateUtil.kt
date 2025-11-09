package com.bolivianusd.app.core.util

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone

object DateUtil {

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

    fun getCurrentDateIso(): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US)
        dateFormat.timeZone = TimeZone.getTimeZone("UTC")
        return dateFormat.format(Date())
    }

    fun getCurrentDateString(): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        return dateFormat.format(Date())
    }

    fun getDateForIndex(index: Int, totalItems: Int = 10): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        val calendar = Calendar.getInstance()
        val cal = calendar.clone() as Calendar
        cal.add(Calendar.DAY_OF_MONTH, index - totalItems)
        return dateFormat.format(cal.time)
    }
}