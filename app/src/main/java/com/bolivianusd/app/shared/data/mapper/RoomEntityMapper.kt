package com.bolivianusd.app.shared.data.mapper

import com.bolivianusd.app.core.extensions.toFormatted
import com.bolivianusd.app.shared.data.local.room.entity.PriceEntity
import com.bolivianusd.app.shared.data.local.room.entity.PriceRangeEntity
import com.bolivianusd.app.shared.domain.model.Price
import com.bolivianusd.app.shared.domain.model.PriceRange
import com.bolivianusd.app.shared.domain.model.TradeType
import kotlin.toBigDecimal

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

fun PriceRangeEntity.toPriceRange(): PriceRange {
    return PriceRange(
        asset = this.asset,
        fiat = this.fiat,
        updatedAt = this.updatedAt,
        min = PriceRange.RangeValue(
            value = this.min.value.toBigDecimal(),
            valueLabel = this.min.valueLabel,
            description = this.min.description
        ),
        max = PriceRange.RangeValue(
            value = this.max.value.toBigDecimal(),
            valueLabel = this.max.valueLabel,
            description = this.max.description
        ),
        median = PriceRange.RangeValue(
            value = this.median.value.toBigDecimal(),
            valueLabel = this.median.valueLabel,
            description = this.median.description
        )
    )
}

fun PriceRange.toPriceRangeEntity(tradeType: TradeType): PriceRangeEntity {
    return PriceRangeEntity(
        asset = this.asset,
        fiat = this.fiat,
        type = tradeType.value,
        updatedAt = this.updatedAt,
        min = PriceRangeEntity.RangeValueEntity(
            value = this.min.value.toDouble(),
            valueLabel = this.min.valueLabel,
            description = this.min.description
        ),
        max = PriceRangeEntity.RangeValueEntity(
            value = this.max.value.toDouble(),
            valueLabel = this.max.valueLabel,
            description = this.max.description
        ),
        median = PriceRangeEntity.RangeValueEntity(
            value = this.median.value.toDouble(),
            valueLabel = this.median.valueLabel,
            description = this.median.description
        )
    )
}
