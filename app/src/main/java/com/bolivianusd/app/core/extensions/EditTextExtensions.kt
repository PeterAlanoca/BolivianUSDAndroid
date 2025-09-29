package com.bolivianusd.app.core.extensions

import android.text.InputFilter
import android.widget.EditText

fun EditText.getMaxLength(): Int {
    this.filters.forEach { filter ->
        if (filter is InputFilter.LengthFilter) {
            return filter.max
        }
    }
    return Integer.MAX_VALUE
}