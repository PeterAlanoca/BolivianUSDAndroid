package com.bolivianusd.app.feature.price.data.local.room.entity

import androidx.room.Entity

@Entity(tableName = "daily_candles", primaryKeys = ["x", "asset", "type"])
data class DailyCandleEntity(
    val x: Float,
    val asset: String,
    val fiat: String,
    val type: String,
    val high: Float,
    val low: Float,
    val open: Float,
    val close: Float,
    val shortDate: String,
    val readableDate: String
)
