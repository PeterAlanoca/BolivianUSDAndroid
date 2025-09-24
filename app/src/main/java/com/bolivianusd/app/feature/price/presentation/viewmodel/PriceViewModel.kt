package com.bolivianusd.app.feature.price.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bolivianusd.app.core.extensions.StateHolder
import com.bolivianusd.app.feature.price.domain.model.Price
import com.bolivianusd.app.feature.price.domain.model.old.enum.OperationType
import com.bolivianusd.app.feature.price.domain.usecase.old.GetChartPriceUsdtUseCase
import com.bolivianusd.app.feature.price.domain.usecase.old.GetPriceUsdtUseCase
import com.bolivianusd.app.feature.price.domain.usecase.old.GetRangePriceUsdtUseCase
import com.bolivianusd.app.feature.price.domain.usecase.ObservePriceUseCase
import com.bolivianusd.app.shared.domain.model.DollarType
import com.bolivianusd.app.shared.domain.model.TradeType
import com.bolivianusd.app.shared.domain.state.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PriceViewModel @Inject constructor(
    private val observePriceUseCase: ObservePriceUseCase
) : ViewModel() {

    private val priceStates = mutableMapOf<TradeType, StateHolder<UiState<Price>>>()
    private val currentDollarTypes = mutableMapOf<TradeType, MutableStateFlow<DollarType>>()

    // Flow separado para cada TradeType
    fun getPriceState(tradeType: TradeType): StateFlow<UiState<Price>> {
        return priceStates.getOrPut(tradeType) {
            StateHolder<UiState<Price>>(UiState.Loading)
        }.state
    }

    private fun getDollarTypeFlow(tradeType: TradeType): MutableStateFlow<DollarType> {
        return currentDollarTypes.getOrPut(tradeType) {
            MutableStateFlow(DollarType.ASSET_USDT)
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    fun observeTradeType(tradeType: TradeType) {
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

    fun setDollarType(tradeType: TradeType, dollarType: DollarType) {
        getDollarTypeFlow(tradeType).value = dollarType
    }

    fun refresh(tradeType: TradeType) {
        // Force refresh by updating the value
        val current = getDollarTypeFlow(tradeType).value
        getDollarTypeFlow(tradeType).value = current
    }

    // Limpiar estados cuando ya no se necesiten (opcional)
    fun clearTradeType(tradeType: TradeType) {
        priceStates.remove(tradeType)
        currentDollarTypes.remove(tradeType)
    }
}