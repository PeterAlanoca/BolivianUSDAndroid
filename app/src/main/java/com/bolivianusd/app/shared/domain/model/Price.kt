package com.bolivianusd.app.shared.domain.model

import com.bolivianusd.app.core.util.emptyString
import com.bolivianusd.app.core.util.formatterDate
import java.math.BigDecimal

data class Price(
    var asset: String = emptyString,
    var fiat: String = emptyString,
    var priceValue: BigDecimal = BigDecimal.ZERO,
    var priceLabel: String = emptyString,
    var label: String = emptyString,
    var updatedAt: String = emptyString,
) {
    val updatedAtFormat: String
        get() = formatterDate(updatedAt)
}
