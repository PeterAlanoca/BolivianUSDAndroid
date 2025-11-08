package com.bolivianusd.app.shared.data.exception

sealed class PostgrestDataException(message: String) : Exception(message) {
    class UnknownException(cause: Exception?) : PostgrestDataException("Unexpected error in database operation") {
        init {
            cause?.let { initCause(it) }
        }
    }

    class NoConnection : PostgrestDataException(
        "No internet connection detected. " +
                "Unable to reach Supabase Database. " +
                "Please check your network connection and try again."
    )
}

