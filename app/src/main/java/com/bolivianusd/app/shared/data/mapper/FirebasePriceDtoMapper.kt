package com.bolivianusd.app.shared.data.mapper

import com.bolivianusd.app.core.extensions.toFormatted
import com.bolivianusd.app.shared.data.remote.firebase.dto.PriceRealtimeDto
import com.bolivianusd.app.shared.data.remote.firebase.dto.PriceFirestoreDto
import com.bolivianusd.app.shared.data.remote.firebase.dto.PriceRangeFirestoreDto
import com.bolivianusd.app.shared.data.remote.firebase.dto.PriceRangeRealtimeDto
import com.bolivianusd.app.shared.domain.model.Price
import com.bolivianusd.app.shared.domain.model.PriceRange

fun PriceRealtimeDto.toPrice(): Price {
    return Price(
        asset = asset,
        fiat = fiat,
        priceValue = price.toBigDecimal(),
        priceLabel = price.toBigDecimal().toFormatted(),
        label = label,
        updatedAt = updatedAt
    )
}

fun PriceFirestoreDto.toPrice(): Price {
    return Price(
        asset = asset,
        fiat = fiat,
        priceValue = price.toBigDecimal(),
        priceLabel = price.toBigDecimal().toFormatted(),
        label = label,
        updatedAt = updatedAt
    )
}

fun PriceRangeRealtimeDto.toPriceRange(): PriceRange {
    return PriceRange(
        currency = this.currency,
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

fun PriceRangeFirestoreDto.toPriceRange(): PriceRange {
    return PriceRange(
        currency = this.currency,
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
