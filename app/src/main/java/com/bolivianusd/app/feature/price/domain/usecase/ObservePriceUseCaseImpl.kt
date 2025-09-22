package com.bolivianusd.app.feature.price.domain.usecase

import com.bolivianusd.app.feature.price.domain.model.Price
import com.bolivianusd.app.feature.price.domain.repository.PriceRepository
import com.bolivianusd.app.shared.data.state.UiState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ObservePriceUseCaseImpl @Inject constructor(
    private val priceRepository: PriceRepository
) : ObservePriceUseCase {

    override operator fun invoke(): Flow<UiState<Price>> = flow {
        emit(UiState.Loading)
        try {
            priceRepository.observePrice().collect { price ->
                emit(UiState.Success(price))
            }
        } catch (e: Exception) {
            emit(UiState.Error(e))
        }
    }

}