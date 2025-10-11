package com.bolivianusd.app.shared.data.remote.firebase.dto

import com.bolivianusd.app.core.util.ZERO_D
import com.bolivianusd.app.core.util.emptyString
import com.google.firebase.database.PropertyName

data class PriceRealtimeDto(
    @get:PropertyName("asset")
    @set:PropertyName("asset")
    var asset: String = emptyString,
    @get:PropertyName("fiat")
    @set:PropertyName("fiat")
    var fiat: String = emptyString,
    @get:PropertyName("label")
    @set:PropertyName("label")
    var label: String = emptyString,
    @get:PropertyName("price")
    @set:PropertyName("price")
    var price: Double = ZERO_D,
    @get:PropertyName("updated_at")
    @set:PropertyName("updated_at")
    var updatedAt: String = emptyString,
)