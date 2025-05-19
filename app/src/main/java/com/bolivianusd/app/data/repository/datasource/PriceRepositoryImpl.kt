package com.bolivianusd.app.data.repository.datasource

import com.bolivianusd.app.data.repository.PriceRepository
import com.bolivianusd.app.data.repository.entity.Price
import com.bolivianusd.app.data.repository.mapper.toPrice
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class PriceRepositoryImpl @Inject constructor(
    private val priceDataSource: PriceDataSource
) : PriceRepository {

    override fun getPriceBuy(): Flow<Price> = callbackFlow {
        priceDataSource.getPriceBuy(
            onSuccess = { priceModel ->
                val price = priceModel.toPrice()
                trySend(price).isSuccess
            },
            onError = { error ->
                close(error)
            }
        )
        awaitClose { }
    }

    override fun getPriceSell(): Flow<Price> = callbackFlow {
        priceDataSource.getPriceSell(
            onSuccess = { priceModel ->
                val price = priceModel.toPrice()
                trySend(price).isSuccess
            },
            onError = { error ->
                close(error)
            }
        )
        awaitClose { }
    }

}