package com.bolivianusd.app.feature.price.domain.usecase.old

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.bolivianusd.app.feature.price.data.old.repository.PriceRepository
import com.bolivianusd.app.feature.price.domain.model.Price
import com.bolivianusd.app.feature.price.domain.model.old.enum.OperationType
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject
import com.bolivianusd.app.shared.data.state.State

class GetPriceUsdtUseCaseImpl @Inject constructor(
    private val repository: PriceRepository
) : GetPriceUsdtUseCase {

    override fun execute(
        operationType: OperationType
    ): LiveData<State<Price>> = liveData(Dispatchers.IO) {
        emit(State.Loading)
        try {
            when (operationType) {
                OperationType.BUY ->
                    repository.getPriceBuy().collect { price ->
                        emit(State.Success(price))
                    }

                OperationType.SELL ->
                    repository.getPriceSell().collect { price ->
                        emit(State.Success(price))
                    }
            }
        } catch (e: Exception) {
            emit(State.Error(e.message))
        }
    }
}
