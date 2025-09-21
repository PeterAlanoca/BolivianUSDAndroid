package com.bolivianusd.app.feature.price.data.repository.mapper

import android.content.Context
import com.bolivianusd.app.R
import com.bolivianusd.app.core.extensions.getColorRes
import com.bolivianusd.app.core.extensions.toFormatted
import com.bolivianusd.app.core.util.ZERO_F
import com.bolivianusd.app.core.util.minus
import com.bolivianusd.app.core.util.plus
import com.bolivianusd.app.feature.price.data.model.ChartDataModel
import com.bolivianusd.app.feature.price.data.model.PriceModel
import com.bolivianusd.app.feature.price.data.model.RangePriceModel
import com.bolivianusd.app.feature.price.data.repository.entity.ChartData
import com.bolivianusd.app.feature.price.data.repository.entity.Price
import com.bolivianusd.app.feature.price.data.repository.entity.PriceValue
import com.bolivianusd.app.feature.price.data.repository.entity.RangePrice
import com.github.mikephil.charting.data.Entry

fun PriceModel.toPrice(): Price {
    return Price(
        origin = PriceValue(
            currency = originCurrency,
            amount = originAmount.toBigDecimal(),
            amountLabel = originAmount.toBigDecimal().toFormatted()
        ),
        destination = PriceValue(
            currency = destinationCurrency,
            amount = destinationAmount.toBigDecimal(),
            amountLabel = destinationAmount.toBigDecimal().toFormatted()
        ),
        label = label,
        updatedAt = updatedAt
    )
}

fun ChartDataModel.toChartData(context: Context): ChartData {
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

fun RangePriceModel.toRangePrice(): RangePrice {
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