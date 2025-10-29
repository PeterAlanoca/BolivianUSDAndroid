package com.bolivianusd.app.shared.data.remote.firebase.realtime

import com.bolivianusd.app.core.extensions.get
import com.bolivianusd.app.core.extensions.observeRealtime
import com.bolivianusd.app.core.managers.NetworkManager
import com.bolivianusd.app.shared.data.exception.RealtimeDatabaseException
import com.bolivianusd.app.shared.data.remote.firebase.dto.PriceRealtimeDto
import com.bolivianusd.app.shared.data.mapper.toPrice
import com.bolivianusd.app.shared.data.mapper.toPriceRange
import com.bolivianusd.app.shared.data.remote.firebase.dto.PriceRangeRealtimeDto
import com.bolivianusd.app.shared.domain.model.Price
import com.bolivianusd.app.shared.domain.model.PriceRange
import com.bolivianusd.app.shared.domain.model.TradeType
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PriceUsdtRealtimeDataSource @Inject constructor(
    private val firebaseDatabase: FirebaseDatabase,
    private val networkManager: NetworkManager
) {

    fun observePrice(tradeType: TradeType): Flow<Price> = flow {
        if (!networkManager.hasInternetAccess()) {
            throw RealtimeDatabaseException.NoConnection()
        }
        emitAll(
            flow = firebaseDatabase.observeRealtime<PriceRealtimeDto>(
                path = when (tradeType) {
                    TradeType.BUY -> PRICE_BUY_PATH
                    TradeType.SELL -> PRICE_SELL_PATH
                }
            ).map { dto -> dto.toPrice() }
        )
    }

    fun observePriceRange(tradeType: TradeType): Flow<PriceRange> = flow {
        if (!networkManager.hasInternetAccess()) {
            throw RealtimeDatabaseException.NoConnection()
        }
        emitAll(
            flow = firebaseDatabase.observeRealtime<PriceRangeRealtimeDto>(
                path = when (tradeType) {
                    TradeType.BUY -> PRICE_RANGE_BUY_PATH
                    TradeType.SELL -> PRICE_RANGE_SELL_PATH
                }
            ).map { dto -> dto.toPriceRange() }
        )
    }

    fun getPriceRange(tradeType: TradeType): Flow<PriceRange> = flow {
        if (!networkManager.hasInternetAccess()) {
            throw RealtimeDatabaseException.NoConnection()
        }
        emitAll(
            flow = firebaseDatabase.get<PriceRangeRealtimeDto>(
                path = when (tradeType) {
                    TradeType.BUY -> PRICE_RANGE_BUY_PATH
                    TradeType.SELL -> PRICE_RANGE_SELL_PATH
                }
            ).map { dto -> dto.toPriceRange() }
        )
    }

    companion object {
        private const val PRICE_BUY_PATH = "price_buy_usdt"
        private const val PRICE_SELL_PATH = "price_sell_usdt"
        private const val PRICE_RANGE_BUY_PATH = "price_range_buy_usdt"
        private const val PRICE_RANGE_SELL_PATH = "price_range_sell_usdt"
    }

}

