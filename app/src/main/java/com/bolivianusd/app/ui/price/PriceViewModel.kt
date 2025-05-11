package com.bolivianusd.app.ui.price

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bolivianusd.app.data.model.PriceBuyModel
import com.bolivianusd.app.data.repository.PriceRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class PriceViewModel(private val repository: PriceRepository) : ViewModel() {

    /*private val _price = MutableLiveData<PriceBuyModel>()
    val price: LiveData<PriceBuyModel> get() = _price

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error*/

    private val _price = MutableStateFlow(PriceBuyModel())
    val price: StateFlow<PriceBuyModel> = _price

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error
    init {
        observePrice()
    }

    /*private fun fetchPrice() {
        repository.observePriceData(
            onSuccess = { priceData ->
                _price.postValue(priceData)
            },
            onError = { exception ->
                _error.postValue("Error: ${exception.message}")
            }
        )
    }*/

    private fun observePrice() {
        viewModelScope.launch {
            repository.observePriceFlow()
                .catch { exception ->
                    _error.value = exception.message
                }
                .collect { price ->
                    println("naty ssss")
                }
        }
    }
}
