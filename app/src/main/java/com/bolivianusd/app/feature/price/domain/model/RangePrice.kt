package com.bolivianusd.app.feature.price.domain.model

import com.bolivianusd.app.core.util.emptyString

data class RangePrice(
    var avg: Price = Price(),
    var min: Price = Price(),
    var max: Price = Price(),
    var currency: String = emptyString
) {
    data class Price(
        var amount: String = emptyString,
        var label: String = emptyString
    )

}
