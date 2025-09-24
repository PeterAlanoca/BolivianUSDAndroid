package com.bolivianusd.app.feature.price.domain.exception

sealed class PriceFailure {
    object NotFound : PriceFailure()
    object InvalidData : PriceFailure()
    data class Unknown(val cause: Throwable) : PriceFailure()
}