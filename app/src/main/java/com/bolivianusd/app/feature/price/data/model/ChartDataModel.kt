package com.bolivianusd.app.feature.price.data.model

import com.bolivianusd.app.core.util.ZERO_D
import com.bolivianusd.app.core.util.emptyString
import com.google.firebase.database.PropertyName

data class ChartDataModel(
    @get:PropertyName("updated")
    @set:PropertyName("updated")
    var updated: String = emptyString,
    @get:PropertyName("label")
    @set:PropertyName("label")
    var label: String = emptyString,
    @get:PropertyName("description")
    @set:PropertyName("description")
    var description: String = emptyString,
    @get:PropertyName("variation")
    @set:PropertyName("variation")
    var variation: String = emptyString,
    @get:PropertyName("price")
    @set:PropertyName("price")
    var price: String = emptyString,
    @get:PropertyName("prices")
    @set:PropertyName("prices")
    var prices: List<Price> = emptyList()
) {
    data class Price(
        @get:PropertyName("amount")
        @set:PropertyName("amount")
        var amount: Double = ZERO_D,
        @get:PropertyName("date")
        @set:PropertyName("date")
        var date: String = emptyString,
        @get:PropertyName("marker")
        @set:PropertyName("marker")
        var marker: String = emptyString
    )

}