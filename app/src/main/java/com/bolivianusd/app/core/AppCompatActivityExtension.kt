package com.bolivianusd.app.core

import androidx.activity.OnBackPressedCallback
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.bolivianusd.app.R

fun AppCompatActivity.exit() {
    finishAffinity()
}

fun AppCompatActivity.onBackPressed(backPressed: () -> Unit) {
    onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            backPressed()
        }
    })
}

fun AppCompatActivity.replaceFragment(fragment: Fragment, @IdRes containerViewId: Int, tag: String = "Fragment") {
    supportFragmentManager
        .beginTransaction()
        .replace(containerViewId, fragment, tag)
        .commitAllowingStateLoss()
}

fun AppCompatActivity.pushFragment(fragment: Fragment, @IdRes containerViewId: Int, tag: String = "Fragment") {
    supportFragmentManager
        .beginTransaction()
        .setCustomAnimations(
            R.anim.anim_fragment_slide_left_enter,
            R.anim.anim_fragment_slide_left_exit,
            R.anim.anim_fragment_slide_right_enter,
            R.anim.anim_fragment_slide_right_exit
        )
        .replace(R.id.frameLayout, fragment, tag)
        .addToBackStack(null)
        .commitAllowingStateLoss()
}
