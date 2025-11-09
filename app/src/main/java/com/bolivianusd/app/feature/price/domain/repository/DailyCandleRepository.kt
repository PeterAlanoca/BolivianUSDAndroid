package com.bolivianusd.app.feature.price.domain.repository

import com.bolivianusd.app.feature.price.domain.model.DailyCandle
import com.bolivianusd.app.shared.data.state.DataState
import com.bolivianusd.app.shared.domain.model.DollarType
import com.bolivianusd.app.shared.domain.model.TradeType
import kotlinx.coroutines.flow.Flow

interface DailyCandleRepository {
    fun getLatestCandles(
        dollarType: DollarType,
        tradeType: TradeType
    ): Flow<DataState<List<DailyCandle>>>

    fun hasLocalCandlesData(
        dollarType: DollarType,
        tradeType: TradeType
    ): Flow<Boolean>
}