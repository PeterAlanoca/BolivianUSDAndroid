package com.bolivianusd.app.core

import android.os.Build
import android.window.OnBackInvokedDispatcher
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity

fun AppCompatActivity.exit() {
    finishAffinity()
}

fun AppCompatActivity.onBackPressed(backPressed: (() -> Unit)? = null) {
    if (Build.VERSION.SDK_INT >= 33) {
        onBackInvokedDispatcher.registerOnBackInvokedCallback(OnBackInvokedDispatcher.PRIORITY_DEFAULT) {
            backPressed?.let { it() }
        }
    } else {
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                backPressed?.let { it() }
            }
        })
    }
}