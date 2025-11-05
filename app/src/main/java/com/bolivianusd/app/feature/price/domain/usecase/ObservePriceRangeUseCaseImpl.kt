package com.bolivianusd.app.feature.price.domain.usecase

import com.bolivianusd.app.core.extensions.toUiStateError
import com.bolivianusd.app.shared.domain.model.Price
import com.bolivianusd.app.shared.domain.model.PriceRange
import com.bolivianusd.app.shared.domain.repository.PriceRepository
import com.bolivianusd.app.shared.data.state.DataState
import com.bolivianusd.app.shared.domain.model.DollarType
import com.bolivianusd.app.shared.domain.model.TradeType
import com.bolivianusd.app.shared.domain.state.UiState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

class ObservePriceRangeUseCaseImpl @Inject constructor(
    private val priceRepository: PriceRepository
) : ObservePriceRangeUseCase {

    override operator fun invoke(
        dollarType: DollarType,
        tradeType: TradeType
    ): Flow<UiState<PriceRange>> {
        return priceRepository.observePriceRange(dollarType, tradeType)
            .map { dataState ->
                when (dataState) {
                    is DataState.Success -> UiState.Success(dataState.data)
                    is DataState.Error -> dataState.toUiStateError<Price>()
                }
            }.onStart { emit(UiState.Loading) }
    }

}