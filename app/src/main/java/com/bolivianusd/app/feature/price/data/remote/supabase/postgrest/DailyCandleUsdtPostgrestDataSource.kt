package com.bolivianusd.app.feature.price.data.remote.supabase.postgrest

import com.bolivianusd.app.feature.price.data.remote.supabase.dto.DailyCandleDto
import com.bolivianusd.app.feature.price.domain.model.Price
import io.github.jan.supabase.postgrest.Postgrest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class DailyCandleUsdtPostgrestDataSource @Inject constructor(
    private val postgrest: Postgrest
) {

    fun getAllCandles(): Flow<Price> = flow {//cambiar de nombre y retornar LISt
        try {
            val candles = postgrest[TABLE_NAME]
                .select()
                //.order("candle_date", ascending = false)
                .decodeList<DailyCandleDto>()

            println("naty getAllCandles ${candles.toList()}")
            //emit(candles) este es
            emit(Price())
        } catch (e: Exception) {
            println("naty Exception ${e.message}")
            // emit(emptyList())
            emit(Price())
        }
    }

    companion object {
        private const val TABLE_NAME = "daily_candles"
    }

}
