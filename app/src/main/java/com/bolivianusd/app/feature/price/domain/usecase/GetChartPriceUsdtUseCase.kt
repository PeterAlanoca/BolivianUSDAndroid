package com.bolivianusd.app.feature.price.domain.usecase

import androidx.lifecycle.LiveData
import com.bolivianusd.app.feature.price.domain.model.ChartData
import com.bolivianusd.app.feature.price.domain.model.enum.OperationType
import com.bolivianusd.app.shared.data.state.State

interface GetChartPriceUsdtUseCase {
    fun execute(operationType: OperationType): LiveData<State<ChartData>>
}