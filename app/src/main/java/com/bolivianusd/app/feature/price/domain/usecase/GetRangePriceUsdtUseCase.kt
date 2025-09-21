package com.bolivianusd.app.feature.price.domain.usecase

import androidx.lifecycle.LiveData
import com.bolivianusd.app.feature.price.domain.model.RangePrice
import com.bolivianusd.app.feature.price.domain.model.enum.OperationType
import com.bolivianusd.app.shared.data.state.State

interface GetRangePriceUsdtUseCase {
    fun execute(operationType: OperationType): LiveData<State<RangePrice>>
}