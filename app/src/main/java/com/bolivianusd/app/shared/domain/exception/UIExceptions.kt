package com.bolivianusd.app.shared.domain.exception

class NoConnectionWithOutDataException(
    message: String = "No hay conexión a Internet y no existen datos disponibles."
) : Exception( message)

class NoConnectionWithDataException(
    message: String = "No hay conexión a Internet, pero se mostrarán los datos locales disponibles."
) : Exception(message)
