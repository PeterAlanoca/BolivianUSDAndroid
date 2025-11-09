package com.bolivianusd.app.shared.data.local.room.entity

import androidx.room.Embedded
import androidx.room.Entity

@Entity(tableName = "price_range", primaryKeys = ["asset", "type"])
data class PriceRangeEntity(
    val asset: String,
    val fiat: String,
    val type: String,
    val updatedAt: String,
    @Embedded(prefix = "median_") val median: RangeValueEntity,
    @Embedded(prefix = "min_") val min: RangeValueEntity,
    @Embedded(prefix = "max_") val max: RangeValueEntity
) {
    data class RangeValueEntity(
        val value: Double,
        val valueLabel: String,
        val description: String
    )
}

