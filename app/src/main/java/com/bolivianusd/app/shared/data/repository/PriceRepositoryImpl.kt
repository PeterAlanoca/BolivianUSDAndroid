package com.bolivianusd.app.shared.data.repository

import com.bolivianusd.app.core.extensions.toDataStateFlow
import com.bolivianusd.app.shared.data.remote.firebase.firestore.PriceUsdFirestoreDataSource
import com.bolivianusd.app.shared.data.remote.firebase.realtime.PriceUsdtRealtimeDataSource
import com.bolivianusd.app.shared.data.state.DataState
import com.bolivianusd.app.shared.domain.model.DollarType
import com.bolivianusd.app.shared.domain.model.Price
import com.bolivianusd.app.shared.domain.model.PriceRange
import com.bolivianusd.app.shared.domain.model.TradeType
import com.bolivianusd.app.shared.domain.repository.PriceRepository
import kotlinx.coroutines.flow.Flow

class PriceRepositoryImpl(
    private val priceUsdtRealtimeDataSource: PriceUsdtRealtimeDataSource,
    private val priceUsdFirestoreDataSource: PriceUsdFirestoreDataSource
) : PriceRepository {

    override fun observePrice(dollarType: DollarType, tradeType: TradeType): Flow<DataState<Price>> {
        return when(dollarType) {
            DollarType.USD -> priceUsdFirestoreDataSource.observePrice(tradeType)
            DollarType.USDT -> priceUsdtRealtimeDataSource.observePrice(tradeType)
        }.toDataStateFlow()
    }

    override fun observePriceRange(dollarType: DollarType, tradeType: TradeType): Flow<DataState<PriceRange>> {
        return when(dollarType) {
            DollarType.USD -> priceUsdFirestoreDataSource.observePriceRange(tradeType)
            DollarType.USDT -> priceUsdtRealtimeDataSource.observePriceRange(tradeType)
        }.toDataStateFlow()
    }

    override fun getPrice(dollarType: DollarType, tradeType: TradeType): Flow<DataState<Price>> {
        return when(dollarType) {
            DollarType.USD -> priceUsdFirestoreDataSource.getPrice(tradeType)
            DollarType.USDT -> priceUsdtRealtimeDataSource.getPrice(tradeType)
        }.toDataStateFlow()
    }

    override fun getPriceRange(dollarType: DollarType, tradeType: TradeType): Flow<DataState<PriceRange>> {
        return when(dollarType) {
            DollarType.USD -> priceUsdFirestoreDataSource.getPriceRange(tradeType)
            DollarType.USDT -> priceUsdtRealtimeDataSource.getPriceRange(tradeType)
        }.toDataStateFlow()
    }
}