package com.bolivianusd.app.feature.price.domain.usecase

import com.bolivianusd.app.core.extensions.toUiStateError
import com.bolivianusd.app.shared.domain.model.Price
import com.bolivianusd.app.shared.domain.repository.PriceRepository
import com.bolivianusd.app.shared.data.state.DataState
import com.bolivianusd.app.shared.domain.model.DollarType
import com.bolivianusd.app.shared.domain.model.TradeType
import com.bolivianusd.app.shared.domain.state.UiState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

class ObservePriceUseCaseImpl @Inject constructor(
    private val priceRepository: PriceRepository
) : ObservePriceUseCase {

    override operator fun invoke(
        dollarType: DollarType,
        tradeType: TradeType
    ): Flow<UiState<Price>> {
        return priceRepository.observePrice(dollarType, tradeType)
            .map { dataState ->
                when (dataState) {
                    is DataState.Success -> UiState.Success(dataState.data)
                    is DataState.Error -> dataState.toUiStateError<Price>()
                }
            }
            .onStart { emit(UiState.Loading) }

        /*return priceRepository.observePrice(dollarType, tradeType)
            .transform { dataState ->
                emit(UiState.Loading)
                when (dataState) {
                    is DataState.Success -> emit(UiState.Success(dataState.data))
                    is DataState.Error -> emit(
                        UiState.Error(
                            throwable = dataState.throwable,
                            message = dataState.message ?: "Error al obtener precio"
                        )
                    )
                }
            }*/
    }

    /*override operator fun invoke(): Flow<UiState<Price>> = flow {
        emit(UiState.Loading)
        try {
            priceRepository.observePrice().collect { price ->
                emit(UiState.Success(price))
            }
        } catch (e: Exception) {
            emit(UiState.Error(e))
        }
    }*/
}