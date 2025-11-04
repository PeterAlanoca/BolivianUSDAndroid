package com.bolivianusd.app.shared.data.mapper

import com.bolivianusd.app.core.extensions.toFormatted
import com.bolivianusd.app.shared.data.local.room.entity.PriceEntity
import com.bolivianusd.app.shared.domain.model.Price
import com.bolivianusd.app.shared.domain.model.TradeType

fun PriceEntity.toPrice(): Price {
    return Price(
        asset = asset,
        fiat = fiat,
        priceValue = price.toBigDecimal(),
        priceLabel = price.toBigDecimal().toFormatted(),
        label = label,
        updatedAt = updatedAt
    )
}

fun Price.toPriceEntity(tradeType: TradeType): PriceEntity {
    return PriceEntity(
        asset = asset,
        fiat = fiat,
        type = tradeType.value,
        price = priceValue.toDouble(),
        label = label,
        updatedAt = updatedAt
    )
}
