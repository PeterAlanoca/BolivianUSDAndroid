package com.bolivianusd.app.shared.data.exception

sealed class RealtimeDatabaseException(message: String) : Exception(message) {
    class NullOrInvalidData : RealtimeDatabaseException("Realtime DB data is null or invalid")
    class Cancelled(cause: Exception?) : RealtimeDatabaseException("Realtime DB operation was cancelled") {
        init {
            cause?.let { initCause(it) }
        }
    }
}
