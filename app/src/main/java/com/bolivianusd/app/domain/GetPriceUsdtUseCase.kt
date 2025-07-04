package com.bolivianusd.app.domain

import androidx.lifecycle.LiveData
import com.bolivianusd.app.data.repository.entity.Price
import com.bolivianusd.app.data.repository.entity.enum.OperationType
import com.bolivianusd.app.data.repository.state.State

interface GetPriceUsdtUseCase {
    fun execute(operationType: OperationType): LiveData<State<Price>>
}