package com.bolivianusd.app.feature.price.domain.model

import com.bolivianusd.app.core.util.emptyString

data class Price(
    var origin: PriceValue = PriceValue(),//ELIMINAR
    var destination: PriceValue = PriceValue(),//ELIMINAR


    var label: String = emptyString,
    var updatedAt: String = emptyString,
)
