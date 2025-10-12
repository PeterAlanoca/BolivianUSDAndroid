package com.bolivianusd.app.feature.calculator.presentation.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.RelativeLayout
import androidx.core.view.isVisible
import com.bolivianusd.app.R
import com.bolivianusd.app.core.extensions.gone
import com.bolivianusd.app.core.extensions.visible
import com.bolivianusd.app.core.listeners.SimpleAnimationListener
import com.google.android.material.textview.MaterialTextView
import com.bolivianusd.app.databinding.ViewKeyboardBinding

class KeyboardView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : RelativeLayout(context, attrs, defStyleAttr) {

    private val binding: ViewKeyboardBinding by lazy {
        ViewKeyboardBinding.inflate(LayoutInflater.from(context), this, true)
    }
    private var onNumberClickListener: ((Int) -> Unit)? = null
    private var onDeleteClickListener: (() -> Unit)? = null
    private var onClearClickListener: (() -> Unit)? = null

    private val numberButtons: List<MaterialTextView> by lazy {
        listOf(
            binding.btn0, binding.btn1, binding.btn2, binding.btn3,
            binding.btn4, binding.btn5, binding.btn6, binding.btn7,
            binding.btn8, binding.btn9
        )
    }

    init {
        setupClickListeners()
    }

    fun showShimmerLoading() = with(binding) {
        keyboardShimmer.visible()
        shimmerLayout.root.startShimmer()
        keyboardView.gone()
    }

    fun showContentView() = with(binding) {
        if (keyboardView.isVisible) {
            return@with
        }

        val fadeOut = AnimationUtils.loadAnimation(context, R.anim.anim_view_fade_out)
        fadeOut.setAnimationListener(object : SimpleAnimationListener() {
            override fun onAnimationEnd(animation: Animation?) {
                hideShimmerLoading()
                //setPriceRangeData(priceRange)
                val fadeIn =
                    AnimationUtils.loadAnimation(context, R.anim.anim_view_fade_in)
                keyboardView.visible()
                keyboardView.startAnimation(fadeIn)
            }
        })
        if (keyboardShimmer.isVisible) {
            shimmerLayout.root.startAnimation(fadeOut)
        }
    }

    private fun hideShimmerLoading() = with(binding) {
        keyboardShimmer.gone()
        shimmerLayout.root.stopShimmer()
        keyboardView.visible()
    }

    private fun setupClickListeners() {
        numberButtons.forEachIndexed { number, button ->
            button.setOnClickListener {
                onNumberClickListener?.invoke(number)
            }
        }
        binding.btnDelete.setOnClickListener {
            onDeleteClickListener?.invoke()
        }
        binding.btnClear.setOnClickListener {
            onClearClickListener?.invoke()
        }
    }

    fun setOnNumberClickListener(listener: (Int) -> Unit) {
        onNumberClickListener = listener
    }

    fun setOnDeleteClickListener(listener: () -> Unit) {
        onDeleteClickListener = listener
    }

    fun setOnClearClickListener(listener: () -> Unit) {
        onClearClickListener = listener
    }

}