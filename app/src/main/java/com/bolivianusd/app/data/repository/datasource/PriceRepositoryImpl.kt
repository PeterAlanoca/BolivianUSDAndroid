package com.bolivianusd.app.data.repository.datasource

import com.bolivianusd.app.data.model.PriceBuyModel
import com.bolivianusd.app.data.repository.PriceRepository
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class PriceRepositoryImpl @Inject constructor(
    private val priceDataSource: PriceDataSource
) : PriceRepository {

    override fun getPriceBuy(): Flow<PriceBuyModel> = callbackFlow {
        priceDataSource.getPriceBuy(
            onSuccess = { model ->
                trySend(model).isSuccess
            },
            onError = { error ->
                close(error)
            }
        )
        awaitClose {
        }
    }

}