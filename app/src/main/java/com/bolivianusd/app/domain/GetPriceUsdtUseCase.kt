package com.bolivianusd.app.domain

import androidx.lifecycle.LiveData
import com.bolivianusd.app.data.model.PriceBuyModel
import com.bolivianusd.app.data.repository.state.State

interface GetPriceUsdtUseCase {
    fun execute(): LiveData<State<PriceBuyModel>>
}