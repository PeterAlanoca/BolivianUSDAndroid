package com.bolivianusd.app.feature.calculator.domain.usecase

import com.bolivianusd.app.shared.domain.model.DollarType
import com.bolivianusd.app.shared.domain.model.PriceRange
import com.bolivianusd.app.shared.domain.model.TradeType
import com.bolivianusd.app.shared.domain.state.UiState
import kotlinx.coroutines.flow.Flow

interface GetPriceRangePollingUseCase {
    operator fun invoke(
        dollarType: DollarType,
        tradeType: TradeType,
        interval: Long = 5000L
    ): Flow<UiState<PriceRange>>
}