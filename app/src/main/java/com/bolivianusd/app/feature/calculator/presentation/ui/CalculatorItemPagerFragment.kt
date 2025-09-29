package com.bolivianusd.app.feature.calculator.presentation.ui

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import com.bolivianusd.app.R
import com.bolivianusd.app.core.base.BaseFragment
import com.bolivianusd.app.core.extensions.getMaxLength
import com.bolivianusd.app.core.extensions.serializable
import com.bolivianusd.app.core.util.emptyString
import com.bolivianusd.app.databinding.FragmentCalculatorItemPagerBinding
import com.bolivianusd.app.feature.calculator.presentation.view.AmountEditText
import com.bolivianusd.app.shared.domain.model.TradeType
import com.google.android.material.textfield.TextInputEditText

class CalculatorItemPagerFragment : BaseFragment<FragmentCalculatorItemPagerBinding>() {

    private val tradeType: TradeType by lazy {
        requireNotNull(arguments?.serializable<TradeType>(TRADER_TYPE))
    }

    private var exchangeRateValue = 6.86
    private var usdValue = 1.00
    private var bobValue = 6.86
    private var currentFocusField: EditText? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isNotRecreate = false
    }

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentCalculatorItemPagerBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
        setupClickListeners()
        setupTouchListeners()
        disableSystemKeyboard()
    }

    override fun setListeners() {
        binding.keyboardView.setOnClearClickListener {
            clearField()
        }
        binding.keyboardView.setOnDeleteClickListener {
            deleteNumberField()
        }
        binding.keyboardView.setOnNumberClickListener {
            appendNumberField( it)
        }
    }

    private fun deleteNumberField() {
        when (currentFocusField) {
            binding.etExchangeRate.editText -> {
                deleteNumber(binding.etExchangeRate)
            }
            binding.etUsd.editText -> {
                deleteNumber(binding.etUsd)
            }
            binding.etBob.editText -> {
                deleteNumber(binding.etBob)
            }
        }
    }

    private fun appendNumberField(value: Int) {
        when (currentFocusField) {
            binding.etExchangeRate.editText -> {
                appendNumber(binding.etExchangeRate, value)
            }
            binding.etUsd.editText -> {
                appendNumber(binding.etUsd, value)
            }
            binding.etBob.editText -> {
                appendNumber(binding.etBob, value)
            }
        }
    }

    private fun clearField() {
        when (currentFocusField) {
            binding.etExchangeRate.editText -> {
                clearNumber(binding.etExchangeRate)
            }
            binding.etUsd.editText -> {
                clearNumber(binding.etUsd)
            }
            binding.etBob.editText -> {
                clearNumber(binding.etBob)
            }
        }
    }

    private fun appendNumber(amountEditText: AmountEditText, value: Int) {
        val currentText = amountEditText.editText.text.toString()
        if (currentText.length >= amountEditText.editText.getMaxLength()) return
        val cleanText = currentText.replace("[^\\d]".toRegex(), "")
        if (cleanText == "000" || currentText == "0.00") {
            val newAmount = value / 100.0
            amountEditText.setAmountValue(newAmount)
            return
        }
        val newCleanText = cleanText + value.toString()
        val paddedText = newCleanText.padStart(3, '0')
        val integerPart = paddedText.dropLast(2)
        val decimalPart = paddedText.takeLast(2)
        val newAmount = "$integerPart.$decimalPart".toDouble()
        amountEditText.setAmountValue(newAmount)
    }

    private fun deleteNumber(amountEditText: AmountEditText) {
        val currentText = amountEditText.editText.text.toString()
        val cleanText = currentText.replace("[^\\d]".toRegex(), "")
        if (cleanText.length <= 1) {
            amountEditText.setAmountValue(0.0)
            return
        }
        val newCleanText = cleanText.dropLast(1)
        val paddedText = newCleanText.padStart(3, '0')
        val integerPart = paddedText.dropLast(2)
        val decimalPart = paddedText.takeLast(2)
        val newAmount = "$integerPart.$decimalPart".toDouble()
        amountEditText.setAmountValue(newAmount)
    }

    private fun clearNumber(amountEditText: AmountEditText) {
        amountEditText.clearAmount()
    }

    private fun setupUI() {
        /*binding.etExchangeRate.isFocusable = true
        binding.etExchangeRate.isFocusableInTouchMode = true
        binding.etUsd.isFocusable = true
        binding.etUsd.isFocusableInTouchMode = true
        binding.etBob.isFocusable = true
        binding.etBob.isFocusableInTouchMode = true*/

        // Establecer valores iniciales
    }

    private fun setupClickListeners() {
        binding.etExchangeRate.editText.setOnClickListener {
            setCurrentFocus(binding.etExchangeRate.editText)
        }

        binding.etUsd.setOnClickListener {
            setCurrentFocus(binding.etUsd.editText)
        }

        binding.etBob.setOnClickListener {
            setCurrentFocus(binding.etBob.editText)
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setupTouchListeners() {
        val blockingTouchListener = View.OnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                v.requestFocus()
                hideSystemKeyboard()
                setCurrentFocus(v as EditText)
            }
            true
        }

        binding.etExchangeRate.editText.setOnTouchListener(blockingTouchListener)
        binding.etUsd.editText.setOnTouchListener(blockingTouchListener)
        binding.etBob.editText.setOnTouchListener(blockingTouchListener)
    }

    private fun setCurrentFocus(field: EditText) {
        currentFocusField = field
        field.requestFocus()
        hideSystemKeyboard()
        highlightActiveField()
    }

    private fun highlightActiveField() {
        binding.exchangeRate.strokeWidth = 0
        binding.usd.strokeWidth = 0
        binding.bob.strokeWidth = 0

        when (currentFocusField) {
            binding.etExchangeRate.editText -> {
                binding.exchangeRate.strokeWidth = 2
                binding.exchangeRate.strokeColor = requireContext().getColor(R.color.white)
            }
            binding.etUsd.editText -> {
                binding.usd.strokeWidth = 2
                binding.usd.strokeColor = requireContext().getColor(R.color.white)
            }
            binding.etBob.editText -> {
                binding.bob.strokeWidth = 2
                binding.bob.strokeColor = requireContext().getColor(R.color.white)
            }
        }
    }

    private fun disableSystemKeyboard() {
        val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view?.windowToken, 0)
    }

    private fun hideSystemKeyboard() {
        val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view?.windowToken, 0)
        activity?.let {
            imm.hideSoftInputFromWindow(it.currentFocus?.windowToken, 0)
        }
    }

    override fun onResume() {
        super.onResume()
        hideSystemKeyboard()
    }

    override fun onPause() {
        super.onPause()
        hideSystemKeyboard()
    }

    companion object {
        private const val TRADER_TYPE = "TRADER_TYPE"


        fun newInstance(tradeType: TradeType) = CalculatorItemPagerFragment().apply {
            arguments = Bundle().apply {
                putSerializable(TRADER_TYPE, tradeType)
            }
        }
    }
}
