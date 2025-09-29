package com.bolivianusd.app.feature.calculator.presentation.view

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.RelativeLayout
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
    private var isFormatting = false
    private val decimalFormat: DecimalFormat

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
        setAmountValue(1.00)//
    }

    private fun setupTextWatcher() {
        binding.editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(editable: Editable?) {
                if (isFormatting) return

                isFormatting = true

                try {
                    val originalString = editable.toString()

                    if (originalString.isEmpty() || originalString.replace("[^\\d]".toRegex(), "").isEmpty()) {
                        setAmountValue(0.0)
                        return
                    }

                    val cleanString = originalString.replace("[.,\\s]".toRegex(), "")

                    if (cleanString.isNotEmpty()) {
                        val number = cleanString.toDouble() / 100.0
                        setFormattedAmount(number)
                    } else {
                        setAmountValue(0.0)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    setAmountValue(0.0)
                } finally {
                    isFormatting = false
                }
            }
        })
    }

    private fun setFormattedAmount(amount: Double) {
        val formatted = decimalFormat.format(amount)
        if (binding.editText.text.toString() != formatted) {
            binding.editText.setText(formatted)
            binding.editText.setSelection(formatted.length)
        }
    }

    fun getAmountValue(): Double {
        val text = binding.editText.text.toString().replace(",", "")
        return try {
            text.toDouble()
        } catch (e: Exception) {
            0.0
        }
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
}