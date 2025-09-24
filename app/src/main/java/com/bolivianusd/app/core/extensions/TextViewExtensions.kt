package com.bolivianusd.app.core.extensions

import android.widget.TextView
import com.bolivianusd.app.core.util.emptyString

fun TextView.clearText() {
    this.text = emptyString
}