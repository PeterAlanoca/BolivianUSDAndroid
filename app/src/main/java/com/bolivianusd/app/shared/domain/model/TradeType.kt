package com.bolivianusd.app.shared.domain.model

import java.io.Serializable

enum class TradeType(val value: String) : Serializable {
    BUY("BUY"),
    SELL("SELL")
}