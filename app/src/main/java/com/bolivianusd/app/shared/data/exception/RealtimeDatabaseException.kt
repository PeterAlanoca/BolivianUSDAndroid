package com.bolivianusd.app.shared.data.exception

sealed class RealtimeDatabaseException(message: String) : Exception(message) {
    class NullOrInvalidData(path: String) : RealtimeDatabaseException(
        "Firebase Realtime Database returned null or invalid data at path: '$path'. " +
                "Please check if the data exists and has the correct structure."
    )
    class NoConnection : RealtimeDatabaseException(
        "No internet connection detected. " +
                "Unable to reach Firebase Realtime Database. " +
                "Please check your network connection and try again."
    )
    class Cancelled(cause: Exception) : RealtimeDatabaseException(
        "Firebase operation was cancelled: ${cause.message ?: "Unknown reason"}"
    )
}
