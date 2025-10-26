package com.bolivianusd.app.feature.calculator.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bolivianusd.app.core.extensions.StateHolder
import com.bolivianusd.app.feature.calculator.domain.usecase.GetPriceRangePollingUseCase
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
        viewModelScope.launch {
            getDollarTypeFlow(tradeType).flatMapLatest { dollarType ->
                getPriceRangePollingUseCase.invoke(dollarType, tradeType)
            }.collect { state ->
                priceRangeStates[tradeType]?.setValue(state)
            }
        }
    }

    fun refresh(tradeType: TradeType) {
        val current = getDollarTypeFlow(tradeType).value
        getDollarTypeFlow(tradeType).value = current
    }

    fun clearTradeType(tradeType: TradeType) {
        priceRangeStates.remove(tradeType)
        currentDollarTypes.remove(tradeType)
    }
}