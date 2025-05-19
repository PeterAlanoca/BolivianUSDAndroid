package com.bolivianusd.app.domain

import androidx.lifecycle.LiveData
import com.bolivianusd.app.data.repository.entity.PriceBuy
import com.bolivianusd.app.data.repository.state.State

interface GetPriceUsdtUseCase {
    fun execute(): LiveData<State<PriceBuy>>
}