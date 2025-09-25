package com.bolivianusd.app.feature.price.data.remote.supabase.postgrest

import com.bolivianusd.app.feature.price.data.mappers.toDailyCandle
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
    private val postgrest: Postgrest
) {

    fun getCandles(
        dollarType: DollarType,
        tradeType: TradeType,
        limit: Long = 5
    ): Flow<List<DailyCandle>> = flow {
        try {
            val candlesDto = postgrest[TABLE_NAME]
                .select {
                    filter {
                        eq("asset", "USDT")
                        eq("fiat", "BOB")
                        eq("trade_type", tradeType.value)
                    }
                    order(column = "candle_date", order = Order.DESCENDING)
                    limit(limit)
                }
                .decodeList<DailyCandleDto>()
            val candles = candlesDto.map { it.toDailyCandle() }
            emit(candles)
        } catch (e: Exception) {
            throw PostgrestDataException.UnknownException(e)
        }
    }

    companion object {
        private const val TABLE_NAME = "daily_candles"
    }

}
