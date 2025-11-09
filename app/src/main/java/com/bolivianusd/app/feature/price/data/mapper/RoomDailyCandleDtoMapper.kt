package com.bolivianusd.app.feature.price.data.mapper

import com.bolivianusd.app.feature.price.data.local.room.entity.DailyCandleEntity
import com.bolivianusd.app.feature.price.domain.model.DailyCandle
import com.bolivianusd.app.shared.domain.model.TradeType

fun List<DailyCandleEntity>.toDailyCandles(): List<DailyCandle> {
    return this.map { e ->
        DailyCandle(
            x = e.x,
            asset = e.asset,
            fiat = e.fiat,
            high = e.high,
            low = e.low,
            open = e.open,
            close = e.close,
            shortDate = e.shortDate,
            readableDate = e.readableDate
        )
    }
}


fun List<DailyCandle>.toDailyCandlesEntity(tradeType: TradeType): List<DailyCandleEntity> {
    return this.map { e ->
        DailyCandleEntity(
            x = e.x,
            type = tradeType.value,
            asset = e.asset,
            fiat = e.fiat,
            high = e.high,
            low = e.low,
            open = e.open,
            close = e.close,
            shortDate = e.shortDate,
            readableDate = e.readableDate
        )
    }
}

