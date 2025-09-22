package com.bolivianusd.app.feature.price.data.remote.supabase.dto

import com.bolivianusd.app.core.util.ZERO
import com.bolivianusd.app.core.util.ZERO_D
import com.bolivianusd.app.core.util.emptyString
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DailyCandleDto(
    val id: Int = ZERO,
    val asset: String = emptyString,
    val fiat: String = emptyString,
    @SerialName("trade_type")
    val tradeType: String = emptyString,
    @SerialName("open_price")
    val openPrice: Double = ZERO_D,
    @SerialName("close_price")
    val closePrice: Double = ZERO_D,
    @SerialName("high_price")
    val highPrice: Double = ZERO_D,
    @SerialName("low_price")
    val lowPrice: Double = ZERO_D,
    @SerialName("candle_date")
    val candleDate: String = emptyString, // o LocalDate si prefieres
    @SerialName("created_at")
    val createdAt: String = emptyString // o Instant si prefieres
)