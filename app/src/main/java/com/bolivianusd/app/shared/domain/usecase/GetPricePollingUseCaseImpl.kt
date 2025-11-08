package com.bolivianusd.app.shared.domain.usecase

import com.bolivianusd.app.core.extensions.toUiStateError
import com.bolivianusd.app.shared.data.exception.FirestoreDataException
import com.bolivianusd.app.shared.data.exception.RealtimeDatabaseException
import com.bolivianusd.app.shared.data.state.DataState
import com.bolivianusd.app.shared.domain.exception.NoConnectionWithOutDataException
import com.bolivianusd.app.shared.domain.exception.NoConnectionWithDataException
import com.bolivianusd.app.shared.domain.model.DollarType
import com.bolivianusd.app.shared.domain.model.Price
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

class GetPricePollingUseCaseImpl @Inject constructor(
    private val priceRepository: PriceRepository
) : GetPricePollingUseCase {

    @OptIn(ExperimentalCoroutinesApi::class)
    override operator fun invoke(
        dollarType: DollarType,
        tradeType: TradeType,
        hasUserFocusFlow: Flow<Boolean>,
        interval: Long
    ): Flow<UiState<Price>> = hasUserFocusFlow.flatMapLatest { hasFocus ->
        if (!hasFocus) {
            emptyFlow()
        } else {
            flow {
                var price: Price? = null
                val hasLocalData = priceRepository.hasLocalPriceData(dollarType, tradeType)
                    .firstOrNull() ?: false
                if (!hasLocalData) {
                    emit(UiState.Loading)
                }
                var polling = true
                while (polling) {
                    priceRepository.getPrice(dollarType, tradeType)
                        .collect { dataState ->
                            println("naty getPrice")
                            val uiState = when (dataState) {
                                is DataState.Success -> {
                                    price = dataState.data
                                    UiState.Success(price)
                                }
                                is DataState.Error -> {
                                    polling = false
                                    when (dataState.throwable) {
                                        is RealtimeDatabaseException.NoConnection,
                                        is FirestoreDataException.NoConnection -> {
                                            val exception = if (hasLocalData) {
                                                NoConnectionWithDataException()
                                                    .setData(price)
                                            } else {
                                                NoConnectionWithOutDataException()
                                            }
                                            UiState.Error(
                                                throwable = exception,
                                                message = exception.message
                                            )
                                        }
                                        else -> dataState.toUiStateError<Price>()
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