package com.bolivianusd.app.feature.price.domain.repository

import com.bolivianusd.app.feature.price.domain.model.Price
import kotlinx.coroutines.flow.Flow

interface PriceRepository {
    fun observePrice(): Flow<Price>
}