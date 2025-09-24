package com.bolivianusd.app.feature.price.data.repository

import com.bolivianusd.app.core.extensions.toDataStateFlow
import com.bolivianusd.app.feature.price.data.remote.firebase.firestore.PriceUsdFirestoreDataSource
import com.bolivianusd.app.feature.price.data.remote.firebase.realtime.PriceUsdtRealtimeDataSource
import com.bolivianusd.app.feature.price.domain.model.Price
import com.bolivianusd.app.feature.price.domain.repository.PriceRepository
import com.bolivianusd.app.shared.data.state.DataState
import com.bolivianusd.app.shared.domain.model.DollarType
import com.bolivianusd.app.shared.domain.model.TradeType
import kotlinx.coroutines.flow.Flow

class PriceRepositoryImpl(
    private val priceUsdtRealtimeDataSource: PriceUsdtRealtimeDataSource,
    private val priceUsdFirestoreDataSource: PriceUsdFirestoreDataSource
) : PriceRepository {

    override fun observePrice(dollarType: DollarType, tradeType: TradeType): Flow<DataState<Price>> {
        return when(dollarType) {
            DollarType.FIAT_USD -> priceUsdFirestoreDataSource.observePrice(tradeType)
            DollarType.ASSET_USDT -> priceUsdtRealtimeDataSource.observePrice(tradeType)
        }.toDataStateFlow()
    }

}