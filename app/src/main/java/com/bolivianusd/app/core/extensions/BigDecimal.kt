package com.bolivianusd.app.core.extensions

import java.math.BigDecimal
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

private val format = DecimalFormat("#,##0.00", DecimalFormatSymbols(Locale.US))

fun BigDecimal.toFormatted(): String = format.format(this)