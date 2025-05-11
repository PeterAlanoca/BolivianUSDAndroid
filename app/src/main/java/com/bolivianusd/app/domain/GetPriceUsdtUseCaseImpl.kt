package com.bolivianusd.app.domain

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.bolivianusd.app.data.model.PriceBuyModel
import com.bolivianusd.app.data.repository.PriceRepository
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject
import com.bolivianusd.app.data.repository.state.State

class GetPriceUsdtUseCaseImpl @Inject constructor(
    private val repository: PriceRepository
) : GetPriceUsdtUseCase {

    override fun execute(): LiveData<State<PriceBuyModel>> = liveData(Dispatchers.IO) {
        emit(State.Loading)
        try {
            repository.getPriceBuy().collect { price ->
                emit(State.Success(price))
            }
        } catch (e: Exception) {
            emit(State.Error(e.message ?: "Ocurrió un error"))
        }
    }
}
