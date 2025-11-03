package com.bolivianusd.app.feature.calculator.domain.usecase

import com.bolivianusd.app.shared.domain.model.DollarType
import com.bolivianusd.app.shared.domain.model.Price
import com.bolivianusd.app.shared.domain.model.TradeType
import com.bolivianusd.app.shared.domain.state.UiState
import kotlinx.coroutines.flow.Flow

interface GetPricePollingUseCase {
    operator fun invoke(
        dollarType: DollarType,
        tradeType: TradeType,
        hasUserFocusFlow: Flow<Boolean>,
        interval: Long = 3000L
    ): Flow<UiState<Price>>
}