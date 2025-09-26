package com.bolivianusd.app.feature.price.presentation.mapper

import com.bolivianusd.app.core.util.emptyString
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

fun List<DailyCandle>.toDataSetLabel(): String {
    return this.firstOrNull()?.let {
        "${ it.asset } - ${ it.fiat }"
    } ?: emptyString
}

fun List<DailyCandle>.toFiat(): String {
    return this.firstOrNull()?.fiat ?: emptyString
}

fun List<DailyCandle>.getDateRangeLabel(): String {
    val startDate = this.firstOrNull()?.date ?: emptyString
    val endDate = this.lastOrNull()?.date ?: emptyString
    return "$startDate - $endDate"
}
