package com.bolivianusd.app.core.extensions

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat

fun Context.getActivity(): Activity? = when (this) {
    is Activity -> this
    is ContextWrapper -> baseContext.getActivity()
    else -> null
}

fun Context.getColorRes(id: Int) = ResourcesCompat.getColor(resources, id, null)

fun Context.getDrawableRes(id: Int) = ContextCompat.getDrawable(this, id)