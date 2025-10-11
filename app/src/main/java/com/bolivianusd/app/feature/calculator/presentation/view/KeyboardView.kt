package com.bolivianusd.app.feature.calculator.presentation.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.RelativeLayout
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