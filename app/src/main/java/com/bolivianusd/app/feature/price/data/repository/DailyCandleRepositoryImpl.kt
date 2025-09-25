package com.bolivianusd.app.feature.price.data.repository

import com.bolivianusd.app.core.extensions.toDataStateFlow
import com.bolivianusd.app.feature.price.data.remote.supabase.postgrest.DailyCandlePostgrestDataSource
import com.bolivianusd.app.feature.price.domain.model.DailyCandle
import com.bolivianusd.app.feature.price.domain.repository.DailyCandleRepository
import com.bolivianusd.app.shared.data.state.DataState
import com.bolivianusd.app.shared.domain.model.DollarType
import com.bolivianusd.app.shared.domain.model.TradeType
import kotlinx.coroutines.flow.Flow

class DailyCandleRepositoryImpl(
    private val dailyCandlePostgrestDataSource: DailyCandlePostgrestDataSource
) : DailyCandleRepository {

    override fun getLatestCandles(
        dollarType: DollarType,
        tradeType: TradeType
    ): Flow<DataState<List<DailyCandle>>> {
        return dailyCandlePostgrestDataSource.getCandles(dollarType, tradeType).toDataStateFlow()
    }

}