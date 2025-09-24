package com.bolivianusd.app.feature.price.domain.model.old.model

import com.bolivianusd.app.core.util.emptyString
import java.math.BigDecimal

data class PriceValue(
    var currency: String = emptyString,
    var amount: BigDecimal = BigDecimal.ZERO,
    var amountLabel: String = emptyString
)
