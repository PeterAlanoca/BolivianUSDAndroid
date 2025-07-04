package com.bolivianusd.app.core.extensions

import android.content.Context
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat

fun Context.getColorRes(id: Int) = ResourcesCompat.getColor(resources, id, null)

fun Context.getDrawableRes(id: Int) = ContextCompat.getDrawable(this, id)