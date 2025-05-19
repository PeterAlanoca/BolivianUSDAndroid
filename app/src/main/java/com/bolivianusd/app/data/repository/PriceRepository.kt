package com.bolivianusd.app.data.repository

import com.bolivianusd.app.data.repository.entity.Price
import kotlinx.coroutines.flow.Flow

interface PriceRepository {
    fun getPriceBuy(): Flow<Price>
    fun getPriceSell(): Flow<Price>
}
