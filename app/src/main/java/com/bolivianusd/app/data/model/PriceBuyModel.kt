package com.bolivianusd.app.data.model

import com.bolivianusd.app.core.util.emptyString
import com.google.firebase.database.PropertyName
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class PriceBuyModel(
    val currency: String = emptyString,
    val label: String = emptyString,
    @get:PropertyName("updated_at")
    @set:PropertyName("updated_at")
    var updatedAt: String = emptyString,
    val value: Double = 0.0
) {
    fun getUpdatedAtDate(): Date? {
        return try {
            val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS", Locale.getDefault())
            formatter.parse(updatedAt)
        } catch (e: Exception) {
            null
        }
    }
}