package com.bolivianusd.app.data.repository

import com.bolivianusd.app.data.model.PriceBuyModel
import kotlinx.coroutines.flow.Flow

interface PriceRepository {

    fun getPriceBuy(): Flow<PriceBuyModel>
}
