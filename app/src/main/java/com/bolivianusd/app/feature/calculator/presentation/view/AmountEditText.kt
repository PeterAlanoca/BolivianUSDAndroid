package com.bolivianusd.app.feature.calculator.presentation.view

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.RelativeLayout
import com.bolivianusd.app.core.util.ZERO_D
import com.bolivianusd.app.databinding.ViewAmountEditTextBinding
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.*

class AmountEditText @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : RelativeLayout(context, attrs, defStyleAttr) {

    private val binding: ViewAmountEditTextBinding by lazy {
        ViewAmountEditTextBinding.inflate(LayoutInflater.from(context), this, true)
    }
    private var onFormatError: (() -> Unit)? = null
    private var isFormatting = false
    private val decimalFormat: DecimalFormat
    private var maxAmount = 100000.0

    val editText: EditText
        get() = binding.editText

    var text: String
        get() = binding.editText.text.toString()
        set(value) = setAmountValue(value.toDoubleOrNull() ?: 0.0)

    init {
        val symbols = DecimalFormatSymbols(Locale.US)
        symbols.decimalSeparator = '.'
        symbols.groupingSeparator = ','
        decimalFormat = DecimalFormat("#,###.##", symbols)
        decimalFormat.maximumFractionDigits = 2
        decimalFormat.minimumFractionDigits = 2
        initView()
    }

    private fun initView() {
        setupTextWatcher()
    }

    private fun setupTextWatcher() {
        binding.editText.addTextChangedListener(object : TextWatcher {
            private var previousText = ""

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                previousText = s?.toString() ?: ""
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (isFormatting) return

                val currentText = s?.toString() ?: ""
                val cleanString = currentText.replace("[.,\\s]".toRegex(), "")

                if (cleanString.isNotEmpty()) {
                    try {
                        val number = cleanString.toDouble() / 100.0
                        if (number > maxAmount) {
                            binding.editText.setText(previousText)
                            binding.editText.setSelection(previousText.length)
                            isFormatting = true
                        }
                    } catch (e: Exception) {
                    }
                }
            }

            override fun afterTextChanged(editable: Editable?) {
                if (isFormatting) {
                    isFormatting = false
                    return
                }

                isFormatting = true

                try {
                    val originalString = editable.toString()

                    if (originalString.isEmpty() || originalString.replace("[^\\d]".toRegex(), "").isEmpty()) {
                        setAmountValue(ZERO_D)
                        return
                    }

                    val cleanString = originalString.replace("[.,\\s]".toRegex(), "")

                    if (cleanString.isNotEmpty()) {
                        val number = cleanString.toDouble() / 100.0
                        if (number <= maxAmount) {
                            setFormattedAmount(number)
                        }
                    } else {
                        setAmountValue(ZERO_D)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    setAmountValue(ZERO_D)
                } finally {
                    isFormatting = false
                }
            }
        })
    }

    private fun setFormattedAmount(amount: Double) = with(binding) {
        try {
            val formatted = decimalFormat.format(amount)
            if (editText.text.toString() != formatted) {
                editText.setText(formatted)
                editText.setSelection(formatted.length)
            }
        } catch (e: Exception) {
            onFormatError?.invoke()
            editText.setSelection(editText.text.length)
        }
    }

    fun getAmountValue(): Double = with(binding) {
        val text = editText.text.toString().replace(",", "")
        return try {
            text.toDouble()
        } catch (e: Exception) {
            ZERO_D
        }
    }

    fun setMaxAmount(maxAmount: Double) {
        this.maxAmount = maxAmount
    }

    fun setAmountValue(amount: Double) {
        setFormattedAmount(amount)
    }

    fun clearAmount() {
        setAmountValue(0.0)
    }

    fun setOnAmountChangeListener(listener: (Double) -> Unit) {
        binding.editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                if (!isFormatting) {
                    listener(getAmountValue())
                }
            }
        })
    }

    fun setOnFormatError(listener: () -> Unit) {
        onFormatError = listener
    }
}