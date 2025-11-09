package com.bolivianusd.app.core.extensions

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class StateHolder<T>(initialValue: T) {
    private val _state = MutableStateFlow(initialValue)
    val state: StateFlow<T> = _state.asStateFlow()

    fun setValue(value: T) {
        _state.value = value
    }

    fun update(transform: (T) -> T) {
        _state.value = transform(_state.value)
    }

    val value: T
        get() = state.value
}