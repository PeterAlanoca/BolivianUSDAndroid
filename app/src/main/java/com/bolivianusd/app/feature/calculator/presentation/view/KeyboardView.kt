package com.bolivianusd.app.feature.calculator.presentation.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.RelativeLayout
import androidx.core.view.isVisible
import com.bolivianusd.app.core.extensions.invisible
import com.bolivianusd.app.core.extensions.visible
import com.bolivianusd.app.core.util.ONE_F
import com.bolivianusd.app.core.util.ZERO_F
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

    fun showPriceRangeLoadingState() = with(binding) {
        keyboardView.apply {
            alpha = ZERO_F
            invisible()
        }
        shimmerLayout.root.apply {
            alpha = ONE_F
            visible()
        }
        shimmerLayout.shimmerLayout.startShimmer()
    }

    fun showContentView() = with(binding) {
        shimmerLayout.root.animate().cancel()
        keyboardView.animate().cancel()
        if (keyboardView.isVisible && !shimmerLayout.root.isVisible) {
            return@with
        }
        shimmerLayout.root.animate()
            .alpha(ZERO_F)
            .setDuration(DURATION_ANIMATION_FADE_IN_OUT)
            .withEndAction {
                hideShimmerLoading()
            }
            .start()
        keyboardView.apply {
            alpha = ZERO_F
            visible()
        }
        keyboardView.animate()
            .alpha(ONE_F)
            .setDuration(DURATION_ANIMATION_FADE_IN_OUT)
            .start()
    }

    fun resetUIComponents() = with(binding) {
        keyboardView.clearAnimation()
        shimmerLayout.root.clearAnimation()
        keyboardView.alpha = ONE_F
    }

    private fun hideShimmerLoading() = with(binding) {
        shimmerLayout.shimmerLayout.stopShimmer()
        shimmerLayout.root.invisible()
        shimmerLayout.root.alpha = ONE_F
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

    companion object {
        private const val DURATION_ANIMATION_FADE_IN_OUT = 300L
    }
}