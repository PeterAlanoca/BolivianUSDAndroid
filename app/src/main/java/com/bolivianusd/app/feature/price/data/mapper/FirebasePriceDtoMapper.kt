package com.bolivianusd.app.feature.price.data.mapper

import android.content.Context
import com.bolivianusd.app.R
import com.bolivianusd.app.core.extensions.getColorRes
import com.bolivianusd.app.core.extensions.toFormatted
import com.bolivianusd.app.core.util.ZERO_F
import com.bolivianusd.app.core.util.minus
import com.bolivianusd.app.core.util.plus
import com.bolivianusd.app.feature.price.data.old.entity.ChartDataEntity
import com.bolivianusd.app.feature.price.data.remote.firebase.dto.PriceRealtimeDto
import com.bolivianusd.app.feature.price.data.old.entity.RangePriceEntity
import com.bolivianusd.app.feature.price.data.remote.firebase.dto.PriceFirestoreDto
import com.bolivianusd.app.feature.price.data.remote.firebase.dto.PriceRangeFirestoreDto
import com.bolivianusd.app.feature.price.data.remote.firebase.dto.PriceRangeRealtimeDto
import com.bolivianusd.app.feature.price.domain.model.old.model.ChartData
import com.bolivianusd.app.feature.price.domain.model.Price
import com.bolivianusd.app.feature.price.domain.model.PriceRange
import com.bolivianusd.app.feature.price.domain.model.old.model.RangePrice
import com.github.mikephil.charting.data.Entry

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





////////////////////////////////





fun ChartDataEntity.toChartData(context: Context): ChartData {
    val offset = 4f

    val variationColor = when {
        this.variation.contains(plus) -> context.getColorRes(R.color.green)
        this.variation.contains(minus) -> context.getColorRes(R.color.red)
        else -> context.getColorRes(R.color.green)
    }

    val labels = this.prices.map { it.date }

    val values = this.prices.mapIndexed { index, price ->
        Entry(index.toFloat(), price.amount.toFloat())
    }

    val colors = this.prices.map {
        when (it.marker) {
            plus -> context.getColorRes(R.color.green)
            minus -> context.getColorRes(R.color.red)
            else -> context.getColorRes(R.color.green)
        }
    }

    val axisMaximum = (this.prices.maxByOrNull { it.amount }?.amount ?: ZERO_F).toFloat() + offset
    val axisMinimum = (this.prices.minByOrNull { it.amount }?.amount ?: ZERO_F).toFloat() - offset

    return ChartData(
        updated = this.updated,
        label = this.label,
        description = this.description,
        variation = this.variation,
        variationColor = variationColor,
        price = this.price,
        labels = labels,
        values = values,
        colors = colors,
        axisMaximum = axisMaximum,
        axisMinimum = axisMinimum
    )
}

fun RangePriceEntity.toRangePrice(): RangePrice {
    return RangePrice(
        currency = this.currency,
        min = RangePrice.Price(
            amount = this.min.amount,
            label = this.min.label
        ),
        max = RangePrice.Price(
            amount = this.max.amount,
            label = this.max.label
        ),
        avg = RangePrice.Price(
            amount = this.avg.amount,
            label = this.avg.label
        )
    )
}