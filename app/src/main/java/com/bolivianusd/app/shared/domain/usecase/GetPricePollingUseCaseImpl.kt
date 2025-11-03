package com.bolivianusd.app.shared.domain.usecase

import com.bolivianusd.app.core.extensions.toUiStateError
import com.bolivianusd.app.shared.domain.usecase.GetPricePollingUseCase
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
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.isActive
import javax.inject.Inject

class GetPricePollingUseCaseImpl @Inject constructor(
    private val priceRepository: PriceRepository
) : GetPricePollingUseCase {

    @OptIn(ExperimentalCoroutinesApi::class)
    override operator fun invoke(
        dollarType: DollarType,
        tradeType: TradeType,
        hasUserFocusFlow: Flow<Boolean>,
        interval: Long
    ): Flow<UiState<Price>> {
        return try {
            hasUserFocusFlow
                .flatMapLatest { hasFocus ->
                    if (!hasFocus) {
                        println("naty getPrice stopppp")
                        emptyFlow()
                    } else {
                        flow {
                            while (currentCoroutineContext().isActive) {
                                priceRepository.getPrice(dollarType, tradeType)
                                    .collect { dataState ->
                                        println("naty getPrice $dataState")
                                        val uiState = when (dataState) {
                                            is DataState.Success -> UiState.Success(dataState.data)
                                            is DataState.Error -> dataState.toUiStateError<PriceRange>()
                                        }
                                        emit(uiState)
                                    }
                                delay(interval)
                            }
                        }.onStart { emit(UiState.Loading) }
                    }
                }
        } catch (_: Exception) {
            emptyFlow()
        }
    }
}