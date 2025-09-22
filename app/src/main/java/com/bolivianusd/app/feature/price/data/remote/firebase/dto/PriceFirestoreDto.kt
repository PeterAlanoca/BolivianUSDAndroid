package com.bolivianusd.app.feature.price.data.remote.firebase.dto

import com.bolivianusd.app.core.util.ZERO_D
import com.bolivianusd.app.core.util.emptyString
import com.google.firebase.firestore.PropertyName

data class PriceFirestoreDto(

    @get:PropertyName("destination_amount")
    @set:PropertyName("destination_amount")
    var destinationAmount: Double = ZERO_D,
    @get:PropertyName("destination_currency")
    @set:PropertyName("destination_currency")
    var destinationCurrency: String = emptyString
)
