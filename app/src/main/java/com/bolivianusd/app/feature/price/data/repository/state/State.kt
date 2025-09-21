package com.bolivianusd.app.feature.price.data.repository.state

sealed class State<out T> {
    data object Loading : State<Nothing>()
    data class Success<out T>(val data: T) : State<T>()
    data class Error(private val rawMessage: String? = null) : State<Nothing>() {
        val message: String = rawMessage ?: DEFAULT_ERROR_MESSAGE
        companion object {
            private const val DEFAULT_ERROR_MESSAGE = "Ocurrió un error inesperado"
        }
    }
}