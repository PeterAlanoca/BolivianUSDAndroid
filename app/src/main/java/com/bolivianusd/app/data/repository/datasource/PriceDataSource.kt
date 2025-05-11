package com.bolivianusd.app.data.repository.datasource

import com.bolivianusd.app.data.model.PriceBuyModel
import com.bolivianusd.app.data.repository.datasource.realtime.database.PriceReference
import javax.inject.Inject

class PriceDataSource @Inject constructor(
    private val priceReference: PriceReference
) {

    fun getPriceBuy(onSuccess: (PriceBuyModel) -> Unit, onError: (Exception) -> Unit) {
        priceReference.getPriceBuy(onSuccess = onSuccess, onError = onError)
    }

}
