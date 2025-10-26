package com.bolivianusd.app.feature.price.data.mapper

import com.bolivianusd.app.core.extensions.toMonthDayFormat
import com.bolivianusd.app.core.extensions.toReadableDate
import com.bolivianusd.app.core.util.DateUtil
import com.bolivianusd.app.feature.price.data.remote.supabase.dto.DailyCandleDto
import com.bolivianusd.app.feature.price.domain.model.DailyCandle
import com.bolivianusd.app.shared.domain.model.DollarType

fun List<DailyCandleDto>.toDailyCandles(dollarType: DollarType): List<DailyCandle> {
    return this.asReversed().mapIndexed { index, dto ->
        val shortDate = when (dollarType) {
            DollarType.USDT -> dto.candleDate
            DollarType.USD -> DateUtil.getDateForIndex(
                index = index,
                totalItems = this.size
            )
        }
        val readableDate = when (dollarType) {
            DollarType.USDT -> dto.candleDate
            DollarType.USD -> DateUtil.getDateForIndex(
                index = index,
                totalItems = this.size
            )
        }
        DailyCandle(
            x = index.toFloat(),
            asset = dto.asset,
            fiat = dto.fiat,
            high = dto.highPrice.toFloat(),
            low = dto.lowPrice.toFloat(),
            open = dto.openPrice.toFloat(),
            close = dto.closePrice.toFloat(),
            shortDate = shortDate.toMonthDayFormat(),
            readableDate = readableDate.toReadableDate()
        )
    }
}
