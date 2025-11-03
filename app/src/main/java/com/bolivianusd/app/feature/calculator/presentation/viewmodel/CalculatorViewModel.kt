package com.bolivianusd.app.feature.calculator.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bolivianusd.app.core.extensions.StateHolder
import com.bolivianusd.app.shared.domain.usecase.GetPriceRangePollingUseCase
import com.bolivianusd.app.shared.domain.model.DollarType
import com.bolivianusd.app.shared.domain.model.PriceRange
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
class CalculatorViewModel @Inject constructor(
    private val getPriceRangePollingUseCase: GetPriceRangePollingUseCase
) : ViewModel() {

    val currentTradeType = StateHolder(TradeType.BUY)
    private val currentDollarTypes = mutableMapOf<TradeType, MutableStateFlow<DollarType>>()
    private val priceRangeStates = mutableMapOf<TradeType, StateHolder<UiState<PriceRange>>>()
    private val hasUserFocusFlow = MutableStateFlow(false)
    private val isObserving = mutableMapOf<TradeType, Boolean>()

    fun setUserFocus(hasFocus: Boolean) {
        hasUserFocusFlow.value = hasFocus
    }

    fun setTradeType(tradeType: TradeType) {
        if (currentTradeType.value != tradeType) {
            currentTradeType.setValue(tradeType)
        }
    }

    fun setDollarType(tradeType: TradeType, dollarType: DollarType) {
        if (getDollarTypeFlow(tradeType).value != dollarType) {
            getDollarTypeFlow(tradeType).value = dollarType
        }
    }

    private fun getDollarTypeFlow(tradeType: TradeType): MutableStateFlow<DollarType> {
        return currentDollarTypes.getOrPut(tradeType) {
            MutableStateFlow(DollarType.USDT)
        }
    }

    fun getPriceRangeState(tradeType: TradeType): StateFlow<UiState<PriceRange>> {
        return priceRangeStates.getOrPut(tradeType) {
            StateHolder(UiState.Loading)
        }.state
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    fun observePriceRange(tradeType: TradeType) {
        if (isObserving[tradeType] == true) {
            return
        }
        isObserving[tradeType] = true
        viewModelScope.launch {
            getDollarTypeFlow(tradeType).flatMapLatest { dollarType ->
                getPriceRangePollingUseCase.invoke(
                    dollarType = dollarType,
                    tradeType = tradeType,
                    hasUserFocusFlow = hasUserFocusFlow,
                    //interval = 5000L
                )
            }.collect { state ->
                getPriceRangeStateHolder(tradeType).setValue(state)
            }
        }
    }

    private fun getPriceRangeStateHolder(tradeType: TradeType): StateHolder<UiState<PriceRange>> {
        return priceRangeStates.getOrPut(tradeType) {
            StateHolder(UiState.Loading)
        }
    }

    fun refresh(tradeType: TradeType) {
        val current = getDollarTypeFlow(tradeType).value
        getDollarTypeFlow(tradeType).value = current
    }

    fun clearTradeType(tradeType: TradeType) {
        isObserving.remove(tradeType)
        priceRangeStates.remove(tradeType)
        currentDollarTypes.remove(tradeType)
    }
}