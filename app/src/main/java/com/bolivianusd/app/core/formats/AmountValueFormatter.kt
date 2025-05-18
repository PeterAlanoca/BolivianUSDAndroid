package com.bolivianusd.app.core.formats

import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.formatter.ValueFormatter
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

class AmountValueFormatter : ValueFormatter() {
    private val format = DecimalFormat("#,##0.00", DecimalFormatSymbols(Locale.US))

    override fun getPointLabel(entry: Entry?): String {
        return if (entry != null) format.format(entry.y) else ""
    }
}