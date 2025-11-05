package com.bolivianusd.app.feature.price.domain.usecase

import com.bolivianusd.app.core.extensions.toUiStateError
import com.bolivianusd.app.feature.price.domain.model.DailyCandle
import com.bolivianusd.app.feature.price.domain.repository.DailyCandleRepository
import com.bolivianusd.app.shared.data.state.DataState
import com.bolivianusd.app.shared.domain.model.DollarType
import com.bolivianusd.app.shared.domain.model.TradeType
import com.bolivianusd.app.shared.domain.state.UiState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetLatestCandlesUseCaseImpl @Inject constructor(
    private val dailyCandleRepository: DailyCandleRepository
) : GetLatestCandlesUseCase {

    @OptIn(ExperimentalCoroutinesApi::class)
    override operator fun invoke(
        dollarType: DollarType,
        tradeType: TradeType,
        hasUserFocusFlow: Flow<Boolean>,
        interval: Long
    ): Flow<UiState<List<DailyCandle>>> = hasUserFocusFlow.flatMapLatest { hasFocus ->
        if (!hasFocus) {
            emptyFlow()
        } else {
            flow {
                val hasLocalData = dailyCandleRepository.hasLocalCandlesData(dollarType, tradeType)
                    .firstOrNull() ?: false
                if (!hasLocalData) {
                    emit(UiState.Loading)
                }
                dailyCandleRepository.getLatestCandles(dollarType, tradeType)
                    .collect { dataState ->
                        val uiState = when (dataState) {
                            is DataState.Success -> UiState.Success(dataState.data)
                            is DataState.Error -> dataState.toUiStateError<List<DailyCandle>>()
                        }
                        emit(uiState)
                    }
            }
        }
    }
}
