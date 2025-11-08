package com.bolivianusd.app.shared.domain.usecase

import com.bolivianusd.app.core.extensions.toUiStateError
import com.bolivianusd.app.shared.data.exception.FirestoreDataException
import com.bolivianusd.app.shared.data.exception.RealtimeDatabaseException
import com.bolivianusd.app.shared.data.state.DataState
import com.bolivianusd.app.shared.domain.exception.NoConnectionWithDataException
import com.bolivianusd.app.shared.domain.exception.NoConnectionWithOutDataException
import com.bolivianusd.app.shared.domain.model.DollarType
import com.bolivianusd.app.shared.domain.model.PriceRange
import com.bolivianusd.app.shared.domain.model.TradeType
import com.bolivianusd.app.shared.domain.repository.PriceRepository
import com.bolivianusd.app.shared.domain.state.UiState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
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
                var priceRange: PriceRange? = null
                val hasLocalData = priceRepository.hasLocalPriceRangeData(dollarType, tradeType)
                    .firstOrNull() ?: false
                if (!hasLocalData) {
                    emit(UiState.Loading)
                }
                var polling = true
                while (polling) {
                    priceRepository.getPriceRange(dollarType, tradeType)
                        .collect { dataState ->
                            val uiState = when (dataState) {
                                is DataState.Success -> {
                                    priceRange = dataState.data
                                    UiState.Success(priceRange)
                                }
                                is DataState.Error -> {
                                    polling = false
                                    when (dataState.throwable) {
                                        is RealtimeDatabaseException.NoConnection,
                                        is FirestoreDataException.NoConnection -> {
                                            val exception = if (hasLocalData) {
                                                NoConnectionWithDataException()
                                                    .setData(priceRange)
                                            } else {
                                                NoConnectionWithOutDataException()
                                            }
                                            UiState.Error(
                                                throwable = exception,
                                                message = exception.message
                                            )
                                        }
                                        else -> dataState.toUiStateError<PriceRange>()
                                    }
                                }
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