package com.bolivianusd.app.feature.price.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.bolivianusd.app.feature.price.data.repository.entity.enum.OperationType
import com.bolivianusd.app.feature.price.domain.usecase.GetChartPriceUsdtUseCase
import com.bolivianusd.app.feature.price.domain.usecase.GetPriceUsdtUseCase
import com.bolivianusd.app.feature.price.domain.usecase.GetRangePriceUsdtUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PriceViewModel @Inject constructor(
    private val getPriceUsdtUseCase: GetPriceUsdtUseCase,
    private val getChartPriceUsdtUseCase: GetChartPriceUsdtUseCase,
    private val getRangePriceUsdtUseCase: GetRangePriceUsdtUseCase
) : ViewModel() {

    fun getPriceBuy(operationType: OperationType) = getPriceUsdtUseCase.execute(operationType)

    fun getChartPrice(operationType: OperationType) = getChartPriceUsdtUseCase.execute(operationType)

    fun getRangePrice(operationType: OperationType) = getRangePriceUsdtUseCase.execute(operationType)

}