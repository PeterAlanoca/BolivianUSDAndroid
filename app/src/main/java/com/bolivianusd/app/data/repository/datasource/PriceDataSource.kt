package com.bolivianusd.app.data.repository.datasource

import com.bolivianusd.app.data.model.ChartDataModel
import com.bolivianusd.app.data.model.PriceModel
import com.bolivianusd.app.data.model.RangePriceModel
import com.bolivianusd.app.data.repository.datasource.realtime.database.PriceReference
import javax.inject.Inject

class PriceDataSource @Inject constructor(
    private val priceReference: PriceReference
) {

    fun getPriceBuy(onSuccess: (PriceModel) -> Unit, onError: (Exception) -> Unit) {
        priceReference.getPriceBuy(onSuccess = onSuccess, onError = onError)
    }

    fun getPriceSell(onSuccess: (PriceModel) -> Unit, onError: (Exception) -> Unit) {
        priceReference.getPriceSell(onSuccess = onSuccess, onError = onError)
    }

    fun getChartPriceBuy(onSuccess: (ChartDataModel) -> Unit, onError: (Exception) -> Unit) {
        priceReference.getChartPriceBuy(onSuccess = onSuccess, onError = onError)
    }

    fun getChartPriceSell(onSuccess: (ChartDataModel) -> Unit, onError: (Exception) -> Unit) {
        priceReference.getChartPriceSell(onSuccess = onSuccess, onError = onError)
    }

    fun getRangePriceBuy(onSuccess: (RangePriceModel) -> Unit, onError: (Exception) -> Unit) {
        priceReference.getRangePriceBuy(onSuccess = onSuccess, onError = onError)
    }

    fun getRangePriceSell(onSuccess: (RangePriceModel) -> Unit, onError: (Exception) -> Unit) {
        priceReference.getRangePriceSell(onSuccess = onSuccess, onError = onError)
    }

}