package com.bolivianusd.app.feature.price.domain.model

data class DailyCandle(
    val x: Float,
    val asset: String,
    val fiat: String,
    val high: Float,
    val low: Float,
    val open: Float,
    val close: Float,
    var shortDate: String,
    val readableDate: String
)
