package com.bolivianusd.app.shared.data.local.room.entity

import androidx.room.Entity

@Entity(tableName = "price", primaryKeys = ["asset", "type"])
data class PriceEntity(
    val asset: String,
    val fiat: String,
    val type: String,
    val price: Double,
    val label: String,
    val updatedAt: String
)