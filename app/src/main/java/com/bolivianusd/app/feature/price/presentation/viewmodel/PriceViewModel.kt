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
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PriceViewModel @Inject constructor(
    private val observePriceUseCase: ObservePriceUseCase,



    private val getPriceUsdtUseCase: GetPriceUsdtUseCase,
    private val getChartPriceUsdtUseCase: GetChartPriceUsdtUseCase,
    private val getRangePriceUsdtUseCase: GetRangePriceUsdtUseCase
) : ViewModel() {


    private val priceStateHolder = StateHolder<UiState<Price>>(UiState.Loading)

    val priceState: StateFlow<UiState<Price>> = priceStateHolder.state

    private val currentType = MutableStateFlow("buy") // "buy" o "sell"

    init {
        observePriceChanges()
    }

    private fun observePriceChanges() {
        viewModelScope.launch {
            observePriceUseCase.invoke(
                dollarType = DollarType.ASSET_USDT,
                tradeType = TradeType.BUY
            ).collect { state ->
                priceStateHolder.setValue(state)
            }
        }
    }

    fun setPriceType(type: String) {
        if (type == "buy" || type == "sell") {
            currentType.value = type
        }
    }

    fun refresh() {
        // Forzar recarga reiniciando la recolección
        val current = currentType.value
        currentType.value = ""
        viewModelScope.launch {
            delay(100) // Pequeño delay para reiniciar
            currentType.value = current
        }
    }

    fun getPriceBuy(operationType: OperationType) = getPriceUsdtUseCase.execute(operationType)

    fun getChartPrice(operationType: OperationType) = getChartPriceUsdtUseCase.execute(operationType)

    fun getRangePrice(operationType: OperationType) = getRangePriceUsdtUseCase.execute(operationType)

}