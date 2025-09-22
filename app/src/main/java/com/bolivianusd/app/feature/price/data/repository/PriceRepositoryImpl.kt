package com.bolivianusd.app.feature.price.data.repository

import com.bolivianusd.app.feature.price.data.remote.firebase.firestore.PriceUsdFirestoreDataSource
import com.bolivianusd.app.feature.price.data.remote.firebase.realtime.PriceUsdtRealtimeDataSource
import com.bolivianusd.app.feature.price.domain.model.Price
import com.bolivianusd.app.feature.price.domain.repository.PriceRepository
import kotlinx.coroutines.flow.Flow

class PriceRepositoryImpl(
    private val priceUsdtRealtimeDataSource: PriceUsdtRealtimeDataSource,
    private val priceUsdFirestoreDataSource: PriceUsdFirestoreDataSource
) : PriceRepository {

    override fun observePrice(): Flow<Price> {
        return priceUsdFirestoreDataSource.observePriceBuy()
        //return priceUsdtRealtimeDataSource.observePriceBuy()
    }

}
