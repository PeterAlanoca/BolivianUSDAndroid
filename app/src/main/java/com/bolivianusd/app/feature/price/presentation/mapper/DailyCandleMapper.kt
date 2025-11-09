package com.bolivianusd.app.feature.price.presentation.mapper

import com.bolivianusd.app.R
import com.bolivianusd.app.core.util.emptyString
import com.bolivianusd.app.feature.price.domain.model.DailyCandle
import com.bolivianusd.app.shared.domain.model.DollarType
import com.github.mikephil.charting.data.CandleEntry

fun List<DailyCandle>.toCandleEntries(): List<CandleEntry> {
    return this.map {
        CandleEntry(
            it.x,
            it.high,
            it.low,
            it.open,
            it.close,
            it.readableDate
        )
    }
}

fun List<DailyCandle>.toXAxisValues(): List<String> {
    return this.map { it.shortDate }
}

fun List<DailyCandle>.toXAxisValuesReadable(): List<String> {
    return this.map { it.readableDate }
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
    val startDate = this.firstOrNull()?.shortDate ?: emptyString
    val endDate = this.lastOrNull()?.shortDate ?: emptyString
    return "$startDate - $endDate"
}

fun List<DailyCandle>.getSource(): Int {
    return try {
        this.firstOrNull()?.let {
            when (DollarType.valueOf(it.asset)) {
                DollarType.USDT -> R.string.price_view_item_source_binance
                DollarType.USD -> R.string.price_view_item_source_bcb
            }
        } ?: R.string.empty
    } catch (_: Exception) {
        R.string.empty
    }
}
