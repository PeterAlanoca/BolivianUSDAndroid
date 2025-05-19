package com.bolivianusd.app.ui.price

import androidx.lifecycle.ViewModel
import com.bolivianusd.app.data.repository.entity.enum.OperationType
import com.bolivianusd.app.domain.GetPriceUsdtUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PriceViewModel @Inject constructor(
    private val getPriceUsdtUseCase: GetPriceUsdtUseCase
) : ViewModel() {

    fun getPriceBuy(operationType: OperationType) = getPriceUsdtUseCase.execute(operationType)

}
