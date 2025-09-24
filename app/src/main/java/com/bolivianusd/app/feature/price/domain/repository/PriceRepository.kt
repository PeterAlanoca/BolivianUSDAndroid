package com.bolivianusd.app.feature.price.domain.repository

import com.bolivianusd.app.feature.price.domain.model.Price
import com.bolivianusd.app.shared.data.state.DataState
import com.bolivianusd.app.shared.domain.model.DollarType
import com.bolivianusd.app.shared.domain.model.TradeType
import kotlinx.coroutines.flow.Flow

interface PriceRepository {
    fun observePrice(dollarType: DollarType, tradeType: TradeType): Flow<DataState<Price>>
}