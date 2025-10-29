package com.bolivianusd.app.core.extensions

import com.bolivianusd.app.shared.data.exception.FirestoreDataException
import com.bolivianusd.app.shared.data.exception.RealtimeDatabaseException
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await

inline fun <reified T> FirebaseDatabase.observeRealtime(
    path: String
): Flow<T> = callbackFlow {
    val reference = getReference(path)
    val valueListener = object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            val dto = snapshot.getValue(T::class.java)
            if (dto == null) {
                close(RealtimeDatabaseException.NullOrInvalidData(path))
            } else {
                trySend(dto).isSuccess
            }
        }

        override fun onCancelled(error: DatabaseError) {
            close(RealtimeDatabaseException.Cancelled(error.toException()))
        }
    }
    reference.addValueEventListener(valueListener)
    awaitClose {
        reference.removeEventListener(valueListener)
    }
}

inline fun <reified T> FirebaseDatabase.get(
    path: String
): Flow<T> = flow {
    try {
        val reference = getReference(path)
        val snapshot = reference.get().await()
        val dto = snapshot.getValue(T::class.java)
            ?: throw RealtimeDatabaseException.NullOrInvalidData(path)
        emit(dto)
    } catch (e: Exception) {
        throw RealtimeDatabaseException.Cancelled(e)
    }
}

inline fun <reified T : Any, R> FirebaseFirestore.observeDocument(
    collectionPath: String,
    documentPath: String,
    crossinline mapper: (T) -> R
): Flow<R> = callbackFlow {
    val listener = this@observeDocument.collection(collectionPath)
        .document(documentPath)
        .addSnapshotListener { snapshot, error ->
            if (error != null) {
                close(FirestoreDataException.Cancelled(error.message.orEmpty()))
                return@addSnapshotListener
            }

            if (snapshot != null && snapshot.exists()) {
                val dto = snapshot.toObject(T::class.java)
                dto?.let { trySend(mapper(it)).isSuccess }
                    ?: close(FirestoreDataException.NullOrInvalidData())
            } else {
                close(FirestoreDataException.DocumentNotFound(documentPath))
            }
        }
    awaitClose { listener.remove() }
}

