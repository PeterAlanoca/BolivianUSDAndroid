package com.bolivianusd.app.feature.price.domain.usecase

import com.bolivianusd.app.feature.price.domain.model.DailyCandle
import com.bolivianusd.app.shared.domain.model.DollarType
import com.bolivianusd.app.shared.domain.model.TradeType
import com.bolivianusd.app.shared.domain.state.UiState
import kotlinx.coroutines.flow.Flow

interface GetLatestCandlesUseCase {
    operator fun invoke(
        dollarType: DollarType,
        tradeType: TradeType
    ): Flow<UiState<List<DailyCandle>>>
}