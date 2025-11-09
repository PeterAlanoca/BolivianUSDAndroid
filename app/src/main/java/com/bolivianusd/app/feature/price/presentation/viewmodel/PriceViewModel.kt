package com.bolivianusd.app.feature.price.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bolivianusd.app.core.extensions.StateHolder
import com.bolivianusd.app.shared.domain.usecase.GetPricePollingUseCase
import com.bolivianusd.app.shared.domain.usecase.GetPriceRangePollingUseCase
import com.bolivianusd.app.feature.price.domain.model.DailyCandle
import com.bolivianusd.app.feature.price.domain.usecase.GetLatestCandlesUseCase
import com.bolivianusd.app.feature.price.domain.usecase.ObservePriceRangeUseCase
import com.bolivianusd.app.feature.price.domain.usecase.ObservePriceUseCase
import com.bolivianusd.app.shared.domain.model.DollarType
import com.bolivianusd.app.shared.domain.model.TradeType
import com.bolivianusd.app.shared.domain.model.Price
import com.bolivianusd.app.shared.domain.model.PriceRange
import com.bolivianusd.app.shared.domain.state.UiState
import com.bolivianusd.app.shared.domain.usecase.IsEnabledSwitchDollarUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PriceViewModel @Inject constructor(
    private val observePriceUseCase: ObservePriceUseCase,
    private val observePriceRangeUseCase: ObservePriceRangeUseCase,
    private val getPricePollingUseCase: GetPricePollingUseCase,
    private val getPriceRangePollingUseCase: GetPriceRangePollingUseCase,
    private val getLatestCandlesUseCase: GetLatestCandlesUseCase,
    private val isEnabledSwitchDollarUseCase: IsEnabledSwitchDollarUseCase
) : ViewModel() {

    val currentTradeType = StateHolder(TradeType.BUY)
    private val currentDollarTypes = mutableMapOf<TradeType, MutableStateFlow<DollarType>>()
    private val priceStates = mutableMapOf<TradeType, StateHolder<UiState<Price>>>()
    private val priceRangeStates = mutableMapOf<TradeType, StateHolder<UiState<PriceRange>>>()
    private val dailyCandlesStates = mutableMapOf<TradeType, StateHolder<UiState<List<DailyCandle>>>>()
    private val hasUserFocusFlow = MutableStateFlow(false)
    private val isObserving = mutableMapOf<TradeType, Boolean>()

    fun isEnabledSwitchDollar() = isEnabledSwitchDollarUseCase.invoke()

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

    fun getPriceState(tradeType: TradeType): StateFlow<UiState<Price>> {
        return priceStates.getOrPut(tradeType) {
            StateHolder(UiState.Loading)
        }.state
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    fun observePrice(tradeType: TradeType) {
        viewModelScope.launch {
            getDollarTypeFlow(tradeType).flatMapLatest { dollarType ->
                getPricePollingUseCase.invoke(
                    dollarType = dollarType,
                    tradeType = tradeType,
                    hasUserFocusFlow = hasUserFocusFlow,
                )
                //observePriceUseCase.invoke(dollarType, tradeType)
            }.collect { state ->
                getPriceStateHolder(tradeType).setValue(state)
            }
        }
    }

    private fun getPriceStateHolder(tradeType: TradeType): StateHolder<UiState<Price>> {
        return priceStates.getOrPut(tradeType) {
            StateHolder(UiState.Loading)
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
                getPriceRangePollingUseCase.invoke(
                    dollarType = dollarType,
                    tradeType = tradeType,
                    hasUserFocusFlow = hasUserFocusFlow,
                )
                //observePriceRangeUseCase.invoke(dollarType, tradeType)
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

    fun getDailyCandleState(tradeType: TradeType): StateFlow<UiState<List<DailyCandle>>> {
        return dailyCandlesStates.getOrPut(tradeType) {
            StateHolder(UiState.Loading)
        }.state
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    fun getLatestCandles(tradeType: TradeType) {
        viewModelScope.launch {
            getDollarTypeFlow(tradeType).flatMapLatest { dollarType ->
                getLatestCandlesUseCase.invoke(
                    dollarType = dollarType,
                    tradeType = tradeType,
                    hasUserFocusFlow = hasUserFocusFlow,
                )
            }.collect { state ->
                getLatestCandlesHolder(tradeType).setValue(state)
            }
        }
    }

    private fun getLatestCandlesHolder(tradeType: TradeType): StateHolder<UiState<List<DailyCandle>>> {
        return dailyCandlesStates.getOrPut(tradeType) {
            StateHolder(UiState.Loading)
        }
    }

    fun observePriceAndCandles(tradeType: TradeType) {
        if (isObserving[tradeType] == true) {
            return
        }
        isObserving[tradeType] = true
        observePrice(tradeType)
        observePriceRange(tradeType)
        getLatestCandles(tradeType)
    }

    fun refresh(tradeType: TradeType) {
        hasUserFocusFlow.value = false
        getPriceStateHolder(tradeType).setValue(UiState.Loading)
        getPriceRangeStateHolder(tradeType).setValue(UiState.Loading)
        getLatestCandlesHolder(tradeType).setValue(UiState.Loading)
        viewModelScope.launch {
            delay(REFRESH_DELAY)
            hasUserFocusFlow.value = true
        }
    }

    fun clearTradeType(tradeType: TradeType) {
        isObserving.remove(tradeType)
        priceStates.remove(tradeType)
        priceRangeStates.remove(tradeType)
        currentDollarTypes.remove(tradeType)
        dailyCandlesStates.remove(tradeType)
    }

    companion object {
        private const val REFRESH_DELAY = 100L
    }
}
