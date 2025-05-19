package com.bolivianusd.app.data.repository.datasource

import com.bolivianusd.app.data.model.PriceModel
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

}
