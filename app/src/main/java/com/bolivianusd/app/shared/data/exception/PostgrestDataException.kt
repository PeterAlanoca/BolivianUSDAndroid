package com.bolivianusd.app.shared.data.exception

sealed class PostgrestDataException(message: String) : Exception(message) {
    class UnknownException(cause: Exception?) : PostgrestDataException("Unexpected error in database operation") {
        init {
            cause?.let { initCause(it) }
        }
    }
}

