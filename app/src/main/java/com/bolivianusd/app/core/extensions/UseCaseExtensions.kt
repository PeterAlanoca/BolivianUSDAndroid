package com.bolivianusd.app.core.extensions

import com.bolivianusd.app.shared.data.state.DataState
import com.bolivianusd.app.shared.domain.state.UiState

fun <T> DataState.Error.toUiStateError(): UiState.Error {
    return UiState.Error(
        throwable = this.throwable,
        message = this.message ?: "Error al obtener los datos"
    )
}