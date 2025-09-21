package com.bolivianusd.app.feature.price.domain.usecase

import androidx.lifecycle.LiveData
import com.bolivianusd.app.feature.price.data.repository.entity.ChartData
import com.bolivianusd.app.feature.price.data.repository.entity.enum.OperationType
import com.bolivianusd.app.feature.price.data.repository.state.State

interface GetChartPriceUsdtUseCase {
    fun execute(operationType: OperationType): LiveData<State<ChartData>>
}