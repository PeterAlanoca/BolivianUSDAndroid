package com.bolivianusd.app.feature.price.data.repository

import com.bolivianusd.app.core.extensions.toDataStateFlow
import com.bolivianusd.app.feature.price.data.local.room.DailyCandleRoomDataSource
import com.bolivianusd.app.feature.price.data.remote.supabase.postgrest.DailyCandlePostgrestDataSource
import com.bolivianusd.app.feature.price.domain.model.DailyCandle
import com.bolivianusd.app.feature.price.domain.repository.DailyCandleRepository
import com.bolivianusd.app.shared.data.state.DataState
import com.bolivianusd.app.shared.domain.model.DollarType
import com.bolivianusd.app.shared.domain.model.TradeType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.take

class DailyCandleRepositoryImpl(
    private val dailyCandlePostgrestDataSource: DailyCandlePostgrestDataSource,
    private val dailyCandleRoomDataSource: DailyCandleRoomDataSource
) : DailyCandleRepository {

    override fun getLatestCandles(
        dollarType: DollarType,
        tradeType: TradeType
    ): Flow<DataState<List<DailyCandle>>> = flow {
        dailyCandleRoomDataSource.getCandles(
            dollarType = dollarType,
            tradeType = tradeType
        ).firstOrNull()?.let { localCandlesFlow ->
            if (localCandlesFlow.isNotEmpty()) {
                emit(DataState.Success(localCandlesFlow))
            }
        }
        val remotePriceFlow = dailyCandlePostgrestDataSource.getCandles(
            dollarType = dollarType,
            tradeType = tradeType
        ).toDataStateFlow()
        remotePriceFlow.collect { dataState ->
            if (dataState is DataState.Success) {
                dailyCandleRoomDataSource.saveCandles(
                    candles = dataState.data,
                    tradeType = tradeType
                )
            }
            emit(dataState)
        }
    }.catch {
        emitAll(emptyFlow())
    }

    override fun hasLocalCandlesData(
        dollarType: DollarType,
        tradeType: TradeType
    ): Flow<Boolean> {
        return dailyCandleRoomDataSource.getCandles(
            dollarType = dollarType,
            tradeType = tradeType
        ).map {
            it?.isNotEmpty() == true
        }.take(1)
    }
}