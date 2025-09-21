package com.bolivianusd.app.feature.price.domain.usecase

import androidx.lifecycle.LiveData
import com.bolivianusd.app.feature.price.data.repository.entity.RangePrice
import com.bolivianusd.app.feature.price.data.repository.entity.enum.OperationType
import com.bolivianusd.app.feature.price.data.repository.state.State

interface GetRangePriceUsdtUseCase {
    fun execute(operationType: OperationType): LiveData<State<RangePrice>>
}