package com.bolivianusd.app.data.repository.mapper

import com.bolivianusd.app.core.extensions.toFormatted
import com.bolivianusd.app.data.model.PriceModel
import com.bolivianusd.app.data.repository.entity.Price
import com.bolivianusd.app.data.repository.entity.PriceValue

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