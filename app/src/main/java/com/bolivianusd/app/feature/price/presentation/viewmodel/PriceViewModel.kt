package com.bolivianusd.app.feature.price.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bolivianusd.app.core.extensions.StateHolder
import com.bolivianusd.app.feature.price.domain.model.Price
import com.bolivianusd.app.feature.price.domain.model.PriceRange
import com.bolivianusd.app.feature.price.domain.usecase.ObservePriceRangeUseCase
import com.bolivianusd.app.feature.price.domain.usecase.ObservePriceUseCase
import com.bolivianusd.app.shared.domain.model.DollarType
import com.bolivianusd.app.shared.domain.model.TradeType
import com.bolivianusd.app.shared.domain.state.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PriceViewModel @Inject constructor(
    private val observePriceUseCase: ObservePriceUseCase,
    private val observePriceRangeUseCase: ObservePriceRangeUseCase
) : ViewModel() {

    private val currentDollarTypes = mutableMapOf<TradeType, MutableStateFlow<DollarType>>()
    private val priceStates = mutableMapOf<TradeType, StateHolder<UiState<Price>>>()
    private val priceRangeStates = mutableMapOf<TradeType, StateHolder<UiState<PriceRange>>>()

    private fun getDollarTypeFlow(tradeType: TradeType): MutableStateFlow<DollarType> {
        return currentDollarTypes.getOrPut(tradeType) {
            MutableStateFlow(DollarType.ASSET_USDT)
        }
    }

    fun setDollarType(tradeType: TradeType, dollarType: DollarType) {
        if (getDollarTypeFlow(tradeType).value != dollarType) {
            getDollarTypeFlow(tradeType).value = dollarType
        }
    }

    fun getPriceState(tradeType: TradeType): StateFlow<UiState<Price>> {
        return priceStates.getOrPut(tradeType) {
            StateHolder(UiState.Loading)
        }.state
    }

    fun getPriceRangeState(tradeType: TradeType): StateFlow<UiState<PriceRange>> {
        return priceRangeStates.getOrPut(tradeType) {
            StateHolder(UiState.Loading)
        }.state
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    fun observePrice(tradeType: TradeType) {
        viewModelScope.launch {
            getDollarTypeFlow(tradeType).flatMapLatest { dollarType ->
                observePriceUseCase.invoke(
                    dollarType = dollarType,
                    tradeType = tradeType
                )
            }.collect { state ->
                priceStates[tradeType]?.setValue(state)
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    fun observePriceRange(tradeType: TradeType) {
        viewModelScope.launch {
            getDollarTypeFlow(tradeType).flatMapLatest { dollarType ->
                observePriceRangeUseCase.invoke(
                    dollarType = dollarType,
                    tradeType = tradeType
                )
            }.collect { state ->
                priceRangeStates[tradeType]?.setValue(state)
            }
        }
    }

    fun observePriceAndRange(tradeType: TradeType) {
        observePrice(tradeType)
        observePriceRange(tradeType)
    }

    fun refresh(tradeType: TradeType) {
        val current = getDollarTypeFlow(tradeType).value
        getDollarTypeFlow(tradeType).value = current
    }

    fun clearTradeType(tradeType: TradeType) {
        priceStates.remove(tradeType)
        priceRangeStates.remove(tradeType)
        currentDollarTypes.remove(tradeType)
    }
}