package com.bolivianusd.app.shared.data.remote.firebase.dto

import com.bolivianusd.app.core.util.ZERO_D
import com.bolivianusd.app.core.util.emptyString
import com.google.firebase.database.PropertyName

data class PriceRangeRealtimeDto(
    @get:PropertyName("median")
    @set:PropertyName("median")
    var median: RangeValueDto = RangeValueDto(),
    @get:PropertyName("min")
    @set:PropertyName("min")
    var min: RangeValueDto = RangeValueDto(),
    @get:PropertyName("max")
    @set:PropertyName("max")
    var max: RangeValueDto = RangeValueDto(),
    @get:PropertyName("currency")
    @set:PropertyName("currency")
    var currency: String = emptyString
) {
    data class RangeValueDto(
        @get:PropertyName("value")
        @set:PropertyName("value")
        var value: Double = ZERO_D,
        @get:PropertyName("value_label")
        @set:PropertyName("value_label")
        var valueLabel: String = emptyString,
        @get:PropertyName("description")
        @set:PropertyName("description")
        var description: String = emptyString
    )
}
