package com.bolivianusd.app.feature.price.data.entity

import com.bolivianusd.app.core.util.emptyString
import com.google.firebase.database.PropertyName

data class RangePriceEntity(
    @get:PropertyName("avg")
    @set:PropertyName("avg")
    var avg: Price = Price(),
    @get:PropertyName("min")
    @set:PropertyName("min")
    var min: Price = Price(),
    @get:PropertyName("max")
    @set:PropertyName("max")
    var max: Price = Price(),
    @get:PropertyName("currency")
    @set:PropertyName("currency")
    var currency: String = emptyString
) {
    data class Price(
        @get:PropertyName("amount")
        @set:PropertyName("amount")
        var amount: String = emptyString,
        @get:PropertyName("label")
        @set:PropertyName("label")
        var label: String = emptyString
    )

}