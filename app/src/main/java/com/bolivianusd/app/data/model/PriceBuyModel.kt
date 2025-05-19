package com.bolivianusd.app.data.model

import com.bolivianusd.app.core.util.ZERO_D
import com.bolivianusd.app.core.util.emptyString
import com.google.firebase.database.PropertyName

data class PriceBuyModel(
    @get:PropertyName("origin_currency")
    @set:PropertyName("origin_currency")
    var originCurrency: String = emptyString,
    @get:PropertyName("origin_amount")
    @set:PropertyName("origin_amount")
    var originAmount: Double = ZERO_D,
    @get:PropertyName("destination_currency")
    @set:PropertyName("destination_currency")
    var destinationCurrency: String = emptyString,
    @get:PropertyName("destination_amount")
    @set:PropertyName("destination_amount")
    var destinationAmount: Double = ZERO_D,
    @get:PropertyName("label")
    @set:PropertyName("label")
    var label: String = emptyString,
    @get:PropertyName("updated_at")
    @set:PropertyName("updated_at")
    var updatedAt: String = emptyString,
)