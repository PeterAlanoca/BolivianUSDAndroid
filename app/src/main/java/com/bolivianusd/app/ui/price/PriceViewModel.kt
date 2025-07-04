package com.bolivianusd.app.ui.price

import androidx.lifecycle.ViewModel
import com.bolivianusd.app.data.repository.entity.enum.OperationType
import com.bolivianusd.app.domain.GetChartPriceUsdtUseCase
import com.bolivianusd.app.domain.GetPriceUsdtUseCase
import com.bolivianusd.app.domain.GetRangePriceUsdtUseCase
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
