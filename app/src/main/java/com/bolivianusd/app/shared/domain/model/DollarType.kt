package com.bolivianusd.app.shared.domain.model

import java.io.Serializable

enum class DollarType(val value: String) : Serializable {
    USD("USD"),
    USDT("USDT")
}
