package com.bolivianusd.app.core.extensions

import com.bolivianusd.app.shared.data.exception.FirestoreDataException
import com.bolivianusd.app.shared.data.exception.RealtimeDatabaseException
import com.bolivianusd.app.shared.data.state.DataState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

fun <T> Flow<T>.toDataStateFlow(): Flow<DataState<T>> {
    return this
        .map<T, DataState<T>> { price -> DataState.Success(price) }
        .catch { e -> emit(createErrorState(e)) }
}

private fun createErrorState(throwable: Throwable): DataState.Error {
    return DataState.Error(
        throwable = throwable,
        message = when (throwable) {
            is FirestoreDataException.NullOrInvalidData -> "Datos de Firestore inválidos o nulos"
            is FirestoreDataException.DocumentNotFound -> "Documento no encontrado en Firestore"
            is RealtimeDatabaseException.NullOrInvalidData -> "Datos de Realtime Database inválidos"
            is RealtimeDatabaseException.Cancelled -> "Operación de base de datos cancelada"
            is IOException -> "Error de conexión de red"
            else -> "Error desconocido: ${throwable.message ?: "sin detalles"}"
        }
    )
}