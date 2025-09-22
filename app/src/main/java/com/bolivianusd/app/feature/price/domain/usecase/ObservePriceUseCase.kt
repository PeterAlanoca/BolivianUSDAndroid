package com.bolivianusd.app.feature.price.domain.usecase

import com.bolivianusd.app.feature.price.domain.model.Price
import com.bolivianusd.app.shared.data.state.UiState
import kotlinx.coroutines.flow.Flow

interface ObservePriceUseCase {
    operator fun invoke(): Flow<UiState<Price>>
}