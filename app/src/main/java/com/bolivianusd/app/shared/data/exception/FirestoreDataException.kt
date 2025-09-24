package com.bolivianusd.app.shared.data.exception

sealed class FirestoreDataException(message: String) : Exception(message) {
    class NullOrInvalidData : FirestoreDataException("Firestore data is null or invalid")
    class DocumentNotFound(path: String) : FirestoreDataException("Document does not exist: $path")
}
