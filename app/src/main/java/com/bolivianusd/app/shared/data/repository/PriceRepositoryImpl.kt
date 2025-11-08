package com.bolivianusd.app.shared.data.repository

import com.bolivianusd.app.core.extensions.toDataStateFlow
import com.bolivianusd.app.shared.data.local.room.PriceRoomDataSource
import com.bolivianusd.app.shared.data.remote.firebase.config.PriceConfigDataSource
import com.bolivianusd.app.shared.data.remote.firebase.firestore.PriceUsdFirestoreDataSource
import com.bolivianusd.app.shared.data.remote.firebase.realtime.PriceUsdtRealtimeDataSource
import com.bolivianusd.app.shared.data.state.DataState
import com.bolivianusd.app.shared.domain.model.DollarType
import com.bolivianusd.app.shared.domain.model.Price
import com.bolivianusd.app.shared.domain.model.PriceRange
import com.bolivianusd.app.shared.domain.model.TradeType
import com.bolivianusd.app.shared.domain.repository.PriceRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.take

class PriceRepositoryImpl(
    private val priceUsdtRealtimeDataSource: PriceUsdtRealtimeDataSource,
    private val priceUsdFirestoreDataSource: PriceUsdFirestoreDataSource,
    private val priceRoomDataSource: PriceRoomDataSource,
    private val priceConfigDataSource: PriceConfigDataSource
) : PriceRepository {

    override fun observePrice(
        dollarType: DollarType,
        tradeType: TradeType
    ): Flow<DataState<Price>> {
        return when (dollarType) {
            DollarType.USD -> priceUsdFirestoreDataSource.observePrice(tradeType)
            DollarType.USDT -> priceUsdtRealtimeDataSource.observePrice(tradeType)
        }.toDataStateFlow()
    }

    override fun getPrice(dollarType: DollarType, tradeType: TradeType): Flow<DataState<Price>> = flow {
        priceRoomDataSource.getPrice(
            dollarType = dollarType,
            tradeType = tradeType
        ).firstOrNull()?.let { localPriceFlow ->
            emit(DataState.Success(localPriceFlow))
        }
        val remotePriceFlow = when (dollarType) {
            DollarType.USD -> priceUsdFirestoreDataSource.getPrice(tradeType)
            DollarType.USDT -> priceUsdtRealtimeDataSource.getPrice(tradeType)
        }.toDataStateFlow()

        remotePriceFlow.collect { dataState ->
            if (dataState is DataState.Success) {
                priceRoomDataSource.savePrice(
                    price = dataState.data,
                    tradeType = tradeType
                )
            }
            emit(dataState)
        }
    }.catch {
        emitAll(emptyFlow())
    }

    override fun hasLocalPriceData(
        dollarType: DollarType,
        tradeType: TradeType
    ): Flow<Boolean> {
        return priceRoomDataSource.getPrice(
            dollarType = dollarType,
            tradeType = tradeType
        ).map {
            it != null
        }.take(1)
    }

    override fun observePriceRange(
        dollarType: DollarType,
        tradeType: TradeType
    ): Flow<DataState<PriceRange>> {
        return when (dollarType) {
            DollarType.USD -> priceUsdFirestoreDataSource.observePriceRange(tradeType)
            DollarType.USDT -> priceUsdtRealtimeDataSource.observePriceRange(tradeType)
        }.toDataStateFlow()
    }

    override fun getPriceRange(dollarType: DollarType, tradeType: TradeType): Flow<DataState<PriceRange>> = flow {
        priceRoomDataSource.getPriceRange(
            dollarType = dollarType,
            tradeType = tradeType
        ).firstOrNull()?.let { localPriceRangeFlow ->
            emit(DataState.Success(localPriceRangeFlow))
        }
        val remotePriceFlow = when (dollarType) {
            DollarType.USD -> priceUsdFirestoreDataSource.getPriceRange(tradeType)
            DollarType.USDT -> priceUsdtRealtimeDataSource.getPriceRange(tradeType)
        }.toDataStateFlow()

        remotePriceFlow.collect { dataState ->
            if (dataState is DataState.Success) {
                priceRoomDataSource.savePriceRange(
                    priceRange = dataState.data,
                    tradeType = tradeType
                )
            }
            emit(dataState)
        }
    }.catch {
        emitAll(emptyFlow())
    }

    override fun hasLocalPriceRangeData(
        dollarType: DollarType,
        tradeType: TradeType
    ): Flow<Boolean>  {
        return priceRoomDataSource.getPriceRange(
            dollarType = dollarType,
            tradeType = tradeType
        ).map {
            it != null
        }.take(1)
    }

    override fun isEnabledSwitchDollar() = priceConfigDataSource.isEnabledSwitchDollar()

    override fun getDelayPolling() = priceConfigDataSource.getDelayPolling()
}