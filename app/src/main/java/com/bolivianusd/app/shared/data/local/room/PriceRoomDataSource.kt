package com.bolivianusd.app.shared.data.local.room

import com.bolivianusd.app.shared.data.local.room.dao.PriceDao
import com.bolivianusd.app.shared.data.local.room.dao.PriceRangeDao
import com.bolivianusd.app.shared.data.mapper.toPrice
import com.bolivianusd.app.shared.data.mapper.toPriceEntity
import com.bolivianusd.app.shared.data.mapper.toPriceRange
import com.bolivianusd.app.shared.data.mapper.toPriceRangeEntity
import com.bolivianusd.app.shared.domain.model.DollarType
import com.bolivianusd.app.shared.domain.model.Price
import com.bolivianusd.app.shared.domain.model.PriceRange
import com.bolivianusd.app.shared.domain.model.TradeType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PriceRoomDataSource(
    private val priceDao: PriceDao,
    private val priceRangeDao: PriceRangeDao
) {

    fun getPrice(
        dollarType: DollarType,
        tradeType: TradeType
    ): Flow<Price?> = priceDao.getPrice(
        asset = dollarType.value,
        type = tradeType.value
    ).map { it?.toPrice() }

    suspend fun savePrice(price: Price, tradeType: TradeType) =
        priceDao.insert(price.toPriceEntity(tradeType))

    fun getPriceRange(
        dollarType: DollarType,
        tradeType: TradeType
    ): Flow<PriceRange?> = priceRangeDao.getPriceRange(
        asset = dollarType.value,
        type = tradeType.value
    ).map { it?.toPriceRange() }

    suspend fun savePriceRange(priceRange: PriceRange, tradeType: TradeType) =
        priceRangeDao.insert(priceRange.toPriceRangeEntity(tradeType))
}
