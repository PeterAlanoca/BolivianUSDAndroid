package com.bolivianusd.app.shared.domain.model

import com.bolivianusd.app.core.util.emptyString
import java.math.BigDecimal

data class PriceRange(
    var median: RangeValue = RangeValue(),
    var min: RangeValue = RangeValue(),
    var max: RangeValue = RangeValue(),
    var currency: String = emptyString
) {
    data class RangeValue(
        var value: BigDecimal = BigDecimal.ZERO,
        var valueLabel: String = emptyString,
        var description: String = emptyString
    )
}
