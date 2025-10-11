package com.bolivianusd.app.feature.price.domain.usecase

import com.bolivianusd.app.core.extensions.toUiStateError
import com.bolivianusd.app.feature.price.domain.model.DailyCandle
import com.bolivianusd.app.shared.domain.model.Price
import com.bolivianusd.app.feature.price.domain.repository.DailyCandleRepository
import com.bolivianusd.app.shared.data.state.DataState
import com.bolivianusd.app.shared.domain.model.DollarType
import com.bolivianusd.app.shared.domain.model.TradeType
import com.bolivianusd.app.shared.domain.state.UiState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

class GetLatestCandlesUseCaseImpl @Inject constructor(
    private val dailyCandleRepository: DailyCandleRepository
) : GetLatestCandlesUseCase {

    override operator fun invoke(
        dollarType: DollarType,
        tradeType: TradeType
    ): Flow<UiState<List<DailyCandle>>> {
        return dailyCandleRepository.getLatestCandles(dollarType, tradeType)
            .map { dataState ->
                when (dataState) {
                    is DataState.Success -> UiState.Success(dataState.data)
                    is DataState.Error -> dataState.toUiStateError<Price>()
                }
            }
            .onStart { emit(UiState.Loading) }
    }

}
