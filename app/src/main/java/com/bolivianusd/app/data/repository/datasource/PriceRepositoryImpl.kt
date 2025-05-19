package com.bolivianusd.app.data.repository.datasource

import com.bolivianusd.app.data.repository.PriceRepository
import com.bolivianusd.app.data.repository.entity.PriceBuy
import com.bolivianusd.app.data.repository.mapper.toPriceBuy
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class PriceRepositoryImpl @Inject constructor(
    private val priceDataSource: PriceDataSource
) : PriceRepository {

    override fun getPriceBuy(): Flow<PriceBuy> = callbackFlow {
        priceDataSource.getPriceBuy(
            onSuccess = { priceBuyModel ->
                val priceBuy = priceBuyModel.toPriceBuy()
                trySend(priceBuy).isSuccess
            },
            onError = { error ->
                close(error)
            }
        )
        awaitClose {
        }
    }

}