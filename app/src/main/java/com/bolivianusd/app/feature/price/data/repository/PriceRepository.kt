package com.bolivianusd.app.feature.price.data.repository

import com.bolivianusd.app.feature.price.domain.model.ChartData
import com.bolivianusd.app.feature.price.domain.model.Price
import com.bolivianusd.app.feature.price.domain.model.RangePrice
import kotlinx.coroutines.flow.Flow

interface PriceRepository {
    fun getPriceBuy(): Flow<Price>
    fun getPriceSell(): Flow<Price>
    fun getChartPriceBuy(): Flow<ChartData>
    fun getChartPriceSell(): Flow<ChartData>
    fun getRangePriceBuy(): Flow<RangePrice>
    fun getRangePriceSell(): Flow<RangePrice>
}
