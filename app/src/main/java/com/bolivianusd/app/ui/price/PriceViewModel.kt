package com.bolivianusd.app.ui.price

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.bolivianusd.app.data.model.PriceBuyModel
import com.bolivianusd.app.data.repository.state.State
import com.bolivianusd.app.domain.GetPriceUsdtUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PriceViewModel @Inject constructor(getPriceUsdtUseCase: GetPriceUsdtUseCase) : ViewModel() {

    val priceBuy: LiveData<State<PriceBuyModel>> = getPriceUsdtUseCase.execute()

}
