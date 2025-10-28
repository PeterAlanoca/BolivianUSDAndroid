package com.bolivianusd.app.core.view

import android.content.Context
import android.graphics.Color
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.bolivianusd.app.R

object CustomToast {

    private var isToastShowing = false
    private var currentToast: Toast? = null

    /*fun showTopSuccess(context: Context, message: String) {
        showTopToast(
            context = context,
            message = message,
            iconRes = R.drawable.ic_success,
            backgroundColor = Color.parseColor("#4CAF50")
        )
    }*/

    fun showTopError(context: Context, message: String) {
        showTopToast(
            context = context,
            message = message,
            iconRes = R.drawable.ic_error,
            backgroundColor = Color.parseColor("#FF5252")
        )
    }

    fun showTopWarning(context: Context, message: String) {
        showTopToast(
            context = context,
            message = message,
            iconRes = R.drawable.ic_warning,
            backgroundColor = Color.parseColor("#FF9800")
        )
    }

    /*fun showTopInfo(context: Context, message: String) {
        showTopToast(
            context = context,
            message = message,
            iconRes = R.drawable.ic_info,
            backgroundColor = Color.parseColor("#2196F3")
        )
    }*/

    private fun showTopToast(
        context: Context,
        message: String,
        iconRes: Int,
        backgroundColor: Int,
        duration: Int = Toast.LENGTH_SHORT
    ) {
        if (isToastShowing) {
            return
        }

        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.layout_custom_toast, null)

        val textView = view.findViewById<TextView>(R.id.toast_text)
        val iconView = view.findViewById<ImageView>(R.id.toast_icon)
        val container = view.findViewById<LinearLayout>(R.id.toast_container)

        textView.text = message
        iconView.setImageResource(iconRes)
        container.background.setTint(backgroundColor)

        view.alpha = 0f

        currentToast?.cancel()

        val toast = Toast(context).apply {
            setDuration(duration)
            setView(view)
            setGravity(Gravity.TOP or Gravity.CENTER_HORIZONTAL, 0, 100)
        }

        currentToast = toast
        isToastShowing = true

        toast.show()

        view.animate().alpha(1f).setDuration(300).start()

        val totalDisplayTime = when (duration) {
            Toast.LENGTH_LONG -> 3500L
            else -> 2000L
        }

        view.postDelayed({
            view.animate().alpha(0f).setDuration(300).withEndAction {
                isToastShowing = false
                currentToast = null
            }.start()
        }, totalDisplayTime - 300)
    }

    fun cancelCurrentToast() {
        currentToast?.cancel()
        isToastShowing = false
        currentToast = null
    }
}