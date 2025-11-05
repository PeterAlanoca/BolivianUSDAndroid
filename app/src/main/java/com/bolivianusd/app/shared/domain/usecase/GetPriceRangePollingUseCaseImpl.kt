package com.bolivianusd.app.shared.domain.usecase

import com.bolivianusd.app.core.extensions.toUiStateError
import com.bolivianusd.app.shared.data.state.DataState
import com.bolivianusd.app.shared.domain.model.DollarType
import com.bolivianusd.app.shared.domain.model.Price
import com.bolivianusd.app.shared.domain.model.PriceRange
import com.bolivianusd.app.shared.domain.model.TradeType
import com.bolivianusd.app.shared.domain.repository.PriceRepository
import com.bolivianusd.app.shared.domain.state.UiState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.isActive
import javax.inject.Inject

class GetPriceRangePollingUseCaseImpl @Inject constructor(
    private val priceRepository: PriceRepository
) : GetPriceRangePollingUseCase {

    @OptIn(ExperimentalCoroutinesApi::class)
    override operator fun invoke(
        dollarType: DollarType,
        tradeType: TradeType,
        hasUserFocusFlow: Flow<Boolean>,
        interval: Long
    ): Flow<UiState<PriceRange>> = hasUserFocusFlow.flatMapLatest { hasFocus ->
        if (!hasFocus) {
            emptyFlow()
        } else {
            flow {
                val hasLocalData = priceRepository.hasLocalPriceRangeData(dollarType, tradeType)
                    .firstOrNull() ?: false
                if (!hasLocalData) {
                    emit(UiState.Loading)
                }
                while (currentCoroutineContext().isActive) {
                    priceRepository.getPriceRange(dollarType, tradeType)
                        .collect { dataState ->
                            val uiState = when (dataState) {
                                is DataState.Success -> UiState.Success(dataState.data)
                                is DataState.Error -> dataState.toUiStateError<Price>()
                            }
                            emit(uiState)
                        }
                    delay(interval)
                }
            }
        }
    }.catch {
        emitAll(emptyFlow())
    }

}