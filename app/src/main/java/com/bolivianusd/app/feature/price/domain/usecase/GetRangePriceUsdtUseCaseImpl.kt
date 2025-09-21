package com.bolivianusd.app.feature.price.domain.usecase

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.bolivianusd.app.feature.price.data.repository.PriceRepository
import com.bolivianusd.app.feature.price.domain.model.RangePrice
import com.bolivianusd.app.feature.price.domain.model.enum.OperationType
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject
import com.bolivianusd.app.shared.data.state.State

class GetRangePriceUsdtUseCaseImpl @Inject constructor(
    private val repository: PriceRepository
) : GetRangePriceUsdtUseCase {

    override fun execute(
        operationType: OperationType
    ): LiveData<State<RangePrice>> = liveData(Dispatchers.IO) {
        emit(State.Loading)
        try {
            when (operationType) {
                OperationType.BUY ->
                    repository.getRangePriceBuy().collect { rangePrice ->
                        emit(State.Success(rangePrice))
                    }

                OperationType.SELL ->
                    repository.getRangePriceSell().collect { rangePrice ->
                        emit(State.Success(rangePrice))
                    }
            }
        } catch (e: Exception) {
            emit(State.Error(e.message))
        }
    }
}
