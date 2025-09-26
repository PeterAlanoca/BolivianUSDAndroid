package com.bolivianusd.app.feature.price.presentation.mapper

import com.bolivianusd.app.feature.price.domain.model.DailyCandle
import com.github.mikephil.charting.data.CandleEntry

fun List<DailyCandle>.toCandleEntries(): List<CandleEntry> {
    return this.map {
        CandleEntry(it.x, it.high, it.low, it.open, it.close)
    }
}

fun List<DailyCandle>.toXAxisValues(): List<String> {
    return this.map { it.date }
}


