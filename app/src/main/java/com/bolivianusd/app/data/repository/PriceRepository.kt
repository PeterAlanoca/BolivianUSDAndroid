package com.bolivianusd.app.data.repository

import com.bolivianusd.app.data.repository.entity.PriceBuy
import kotlinx.coroutines.flow.Flow

interface PriceRepository {
    fun getPriceBuy(): Flow<PriceBuy>
}
