package com.bolivianusd.app.core.extensions

import android.content.Context
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment

fun Fragment.hideSystemKeyboard() {
    val imm =
        requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(view?.windowToken, 0)
    activity?.let {
        imm.hideSoftInputFromWindow(it.currentFocus?.windowToken, 0)
    }
}
