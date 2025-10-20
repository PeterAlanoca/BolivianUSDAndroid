package com.bolivianusd.app.shared.domain.model

import com.bolivianusd.app.core.util.emptyString
import com.bolivianusd.app.core.util.formatterDate
import java.math.BigDecimal

data class PriceRange(
    var median: RangeValue = RangeValue(),
    var min: RangeValue = RangeValue(),
    var max: RangeValue = RangeValue(),
    var asset: String = emptyString,
    var fiat: String = emptyString,
    var updatedAt: String = emptyString
) {
    data class RangeValue(
        var value: BigDecimal = BigDecimal.ZERO,
        var valueLabel: String = emptyString,
        var description: String = emptyString
    )

    val descriptionLabel: String
        get() = "${min.description} ${min.valueLabel} " +
                "| ${median.description} ${median.valueLabel} " +
                "| ${max.description} ${max.valueLabel}"

    val updatedAtFormat: String
        get() = formatterDate(updatedAt)

}
