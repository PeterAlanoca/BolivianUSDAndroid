package com.bolivianusd.app.shared.data.exception

sealed class FirestoreDataException(message: String) : Exception(message) {
    class NullOrInvalidData : FirestoreDataException("Firestore data is null or invalid")
    class Cancelled(message: String) : FirestoreDataException("Firestore data Cancelled $message")
    class DocumentNotFound(path: String) : FirestoreDataException("Document does not exist: $path")
    class NoConnection : RealtimeDatabaseException(
        "No internet connection detected. " +
                "Unable to reach Firestore. " +
                "Please check your network connection and try again."
    )
}
