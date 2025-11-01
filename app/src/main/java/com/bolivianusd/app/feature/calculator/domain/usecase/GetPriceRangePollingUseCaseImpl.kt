package com.bolivianusd.app.feature.calculator.domain.usecase

import com.bolivianusd.app.core.extensions.toUiStateError
import com.bolivianusd.app.shared.data.state.DataState
import com.bolivianusd.app.shared.domain.model.DollarType
import com.bolivianusd.app.shared.domain.model.PriceRange
import com.bolivianusd.app.shared.domain.model.TradeType
import com.bolivianusd.app.shared.domain.repository.PriceRepository
import com.bolivianusd.app.shared.domain.state.UiState
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.isActive
import javax.inject.Inject

class GetPriceRangePollingUseCaseImpl @Inject constructor(
    private val priceRepository: PriceRepository
) : GetPriceRangePollingUseCase {

    override operator fun invoke(
        dollarType: DollarType,
        tradeType: TradeType,
        interval: Long
    ): Flow<UiState<PriceRange>> {
        return flow {
            while (currentCoroutineContext().isActive) {
                priceRepository.getPriceRange(dollarType, tradeType)
                    .collect { dataState ->
                        println("naty setupPriceRangeObserver ${dataState.toString()}")
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