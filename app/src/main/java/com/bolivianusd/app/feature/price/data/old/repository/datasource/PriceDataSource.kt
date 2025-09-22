package com.bolivianusd.app.feature.price.data.old.repository.datasource

import com.bolivianusd.app.feature.price.data.old.entity.ChartDataEntity
import com.bolivianusd.app.feature.price.data.remote.firebase.dto.PriceRealtimeDto
import com.bolivianusd.app.feature.price.data.old.entity.RangePriceEntity
import com.bolivianusd.app.feature.price.data.old.repository.datasource.realtime.database.PriceReference
import javax.inject.Inject

class PriceDataSource @Inject constructor(
    private val priceReference: PriceReference
) {

    fun getPriceBuy(onSuccess: (PriceRealtimeDto) -> Unit, onError: (Exception) -> Unit) {
        priceReference.getPriceBuy(onSuccess = onSuccess, onError = onError)
    }

    fun getPriceSell(onSuccess: (PriceRealtimeDto) -> Unit, onError: (Exception) -> Unit) {
        priceReference.getPriceSell(onSuccess = onSuccess, onError = onError)
    }

    fun getChartPriceBuy(onSuccess: (ChartDataEntity) -> Unit, onError: (Exception) -> Unit) {
        priceReference.getChartPriceBuy(onSuccess = onSuccess, onError = onError)
    }

    fun getChartPriceSell(onSuccess: (ChartDataEntity) -> Unit, onError: (Exception) -> Unit) {
        priceReference.getChartPriceSell(onSuccess = onSuccess, onError = onError)
    }

    fun getRangePriceBuy(onSuccess: (RangePriceEntity) -> Unit, onError: (Exception) -> Unit) {
        priceReference.getRangePriceBuy(onSuccess = onSuccess, onError = onError)
    }

    fun getRangePriceSell(onSuccess: (RangePriceEntity) -> Unit, onError: (Exception) -> Unit) {
        priceReference.getRangePriceSell(onSuccess = onSuccess, onError = onError)
    }

}