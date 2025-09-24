package com.bolivianusd.app.shared.data.state

sealed class DataState<out T> {
    data class Success<out T>(val data: T) : DataState<T>()
    data class Error(
        val throwable: Throwable? = null,
        val message: String? = null
    ) : DataState<Nothing>()
}
