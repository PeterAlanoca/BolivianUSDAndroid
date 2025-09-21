package com.bolivianusd.app.feature.price.data.repository.entity

import com.bolivianusd.app.core.util.emptyString

data class Price(
    var origin: PriceValue = PriceValue(),
    var destination: PriceValue = PriceValue(),
    var label: String = emptyString,
    var updatedAt: String = emptyString,
)
