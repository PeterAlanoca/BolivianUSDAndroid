package com.bolivianusd.app.feature.price.data.mapper

import com.bolivianusd.app.core.extensions.toMonthDayFormat
import com.bolivianusd.app.core.extensions.toReadableDate
import com.bolivianusd.app.feature.price.data.remote.supabase.dto.DailyCandleDto
import com.bolivianusd.app.feature.price.domain.model.DailyCandle

fun List<DailyCandleDto>.toDailyCandles(): List<DailyCandle> {
    return this.asReversed().mapIndexed { index, dto ->
        DailyCandle(
            x = index.toFloat(),
            asset = dto.asset,
            fiat = dto.fiat,
            high = dto.highPrice.toFloat(),
            low = dto.lowPrice.toFloat(),
            open = dto.openPrice.toFloat(),
            close = dto.closePrice.toFloat(),
            shortDate = dto.candleDate.toMonthDayFormat(),
            readableDate = dto.candleDate.toReadableDate()
        )
    }
}
