package com.bolivianusd.app.feature.price.data.remote.supabase.postgrest

import com.bolivianusd.app.core.managers.NetworkManager
import com.bolivianusd.app.feature.price.data.mapper.toDailyCandles
import com.bolivianusd.app.feature.price.data.remote.supabase.dto.DailyCandleDto
import com.bolivianusd.app.feature.price.domain.model.DailyCandle
import com.bolivianusd.app.shared.data.exception.PostgrestDataException
import com.bolivianusd.app.shared.domain.model.DollarType
import com.bolivianusd.app.shared.domain.model.TradeType
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.query.Order
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class DailyCandlePostgrestDataSource @Inject constructor(
    private val postgrest: Postgrest,
    private val networkManager: NetworkManager
) {

    fun getCandles(
        dollarType: DollarType,
        tradeType: TradeType,
        limit: Long = 10
    ): Flow<List<DailyCandle>> = flow {
        if (!networkManager.hasInternetAccess()) {
            throw PostgrestDataException.NoConnection()
        }
        try {
            val candlesDto = postgrest[TABLE_NAME]
                .select {
                    filter {
                        eq("asset", dollarType.value)
                        eq("fiat", "BOB")
                        eq("trade_type", tradeType.value)
                    }
                    order(column = "candle_date", order = Order.DESCENDING)
                    limit(limit)
                }
                .decodeList<DailyCandleDto>()
            val candles = candlesDto.toDailyCandles(dollarType)
            emit(candles)
        } catch (e: Exception) {
            throw PostgrestDataException.UnknownException(e)
        }
    }

    companion object {
        private const val TABLE_NAME = "daily_candles"
    }

}
