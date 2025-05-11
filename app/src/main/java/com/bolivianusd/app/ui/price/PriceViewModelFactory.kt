package com.bolivianusd.app.ui.price

import com.bolivianusd.app.data.repository.PriceRepository


import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class PriceViewModelFactory(
    private val repository: PriceRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return PriceViewModel(repository) as T
    }
}
