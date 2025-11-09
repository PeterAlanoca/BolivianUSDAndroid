package com.bolivianusd.app.shared.domain.exception

class NoConnectionWithOutDataException(
    message: String = "No hay conexión a Internet y no existen datos disponibles."
) : Exception( message)

class NoConnectionWithDataException(
    message: String = "No hay conexión a Internet, pero se mostrarán los datos locales disponibles."
) : Exception(message) {
    private var data: Any? = null

    fun <T> setData(dataValue: T): NoConnectionWithDataException {
        this.data = dataValue
        return this
    }

    @Suppress("UNCHECKED_CAST")
    fun <T> getData(): T? = data as? T
}