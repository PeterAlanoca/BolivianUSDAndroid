package com.bolivianusd.app.feature.price.data.local.room

import com.bolivianusd.app.feature.price.data.local.room.dao.DailyCandleDao
import com.bolivianusd.app.feature.price.data.mapper.toDailyCandles
import com.bolivianusd.app.feature.price.data.mapper.toDailyCandlesEntity
import com.bolivianusd.app.feature.price.domain.model.DailyCandle
import com.bolivianusd.app.shared.domain.model.DollarType
import com.bolivianusd.app.shared.domain.model.TradeType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DailyCandleRoomDataSource(
    private val dailyCandleDao: DailyCandleDao
) {

    fun getCandles(
        dollarType: DollarType,
        tradeType: TradeType
    ): Flow<List<DailyCandle>?> = dailyCandleDao.getCandles(
        asset = dollarType.value,
        type = tradeType.value
    ).map { it.toDailyCandles() }

    suspend fun saveCandles(candles: List<DailyCandle>, tradeType: TradeType) =
        dailyCandleDao.insertAll(candles.toDailyCandlesEntity(tradeType))

}
