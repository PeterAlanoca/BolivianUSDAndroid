package com.bolivianusd.app.domain

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.bolivianusd.app.data.repository.PriceRepository
import com.bolivianusd.app.data.repository.entity.ChartData
import com.bolivianusd.app.data.repository.entity.enum.OperationType
import com.bolivianusd.app.data.repository.state.State
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class GetChartPriceUsdtUseCaseImpl @Inject constructor(
    private val repository: PriceRepository
) : GetChartPriceUsdtUseCase {

    override fun execute(
        operationType: OperationType
    ): LiveData<State<ChartData>> = liveData(Dispatchers.IO) {
        emit(State.Loading)
        try {
            when (operationType) {
                OperationType.BUY ->
                    repository.getChartPriceBuy().collect { chartData ->
                        emit(State.Success(chartData))
                    }

                OperationType.SELL ->
                    repository.getChartPriceSell().collect { chartData ->
                        emit(State.Success(chartData))
                    }
            }
        } catch (e: Exception) {
            emit(State.Error(e.message))
        }
    }
}
