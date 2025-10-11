package com.bolivianusd.app.feature.calculator.presentation.view

import android.annotation.SuppressLint
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.RelativeLayout
import androidx.core.view.isVisible
import com.bolivianusd.app.R
import com.bolivianusd.app.core.extensions.getActivity
import com.bolivianusd.app.core.extensions.getMaxLength
import com.bolivianusd.app.core.extensions.gone
import com.bolivianusd.app.core.extensions.visible
import com.bolivianusd.app.core.listeners.SimpleAnimationListener
import com.bolivianusd.app.databinding.ViewDisplayBinding

class DisplayView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : RelativeLayout(context, attrs, defStyleAttr) {

    private val binding: ViewDisplayBinding by lazy {
        ViewDisplayBinding.inflate(LayoutInflater.from(context), this, true)
    }

    private var exchangeRateValue = 6.86
    private var usdValue = 1.00
    private var bobValue = exchangeRateValue * usdValue

    private var currentFocusField: EditText? = null

    init {
        initView()
        setListeners()
        setupTouchListeners()
        disableSystemKeyboard()
    }

    fun setData() = with(binding) {
        etExchangeRate.setAmountValue(exchangeRateValue)
        etUsd.setAmountValue(usdValue)
        etBob.setAmountValue(bobValue)

        currentFocusField = etUsd.editText
        Handler(Looper.getMainLooper()).postDelayed({
            binding.etUsd.editText.requestFocus()

        }, 500)
    }

    fun deleteNumberField() {
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

    fun appendNumberField(value: Int) {
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

    fun clearField() {
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

    fun showShimmerLoading() = with(binding) {
        displayShimmer.visible()
        shimmerLayout.priceRangeShimmerLayout.startShimmer()
        shimmerLayout.dateShimmerLayout.startShimmer()
        shimmerLayout.shimmerExchangeRateLayout.startShimmer()
        shimmerLayout.dollarTypeShimmerSwitch.startShimmer()
        shimmerLayout.usdShimmerLayout.startShimmer()
        shimmerLayout.lottieAnimationShimmerLayout.startShimmer()
        shimmerLayout.bobShimmerLayout.startShimmer()
        displayView.gone()
    }

    fun showContentView() = with(binding) {
        if (displayView.isVisible) {
            return@with
        }

        val fadeOut = AnimationUtils.loadAnimation(context, R.anim.anim_view_fade_out)
        fadeOut.setAnimationListener(object : SimpleAnimationListener() {
            override fun onAnimationEnd(animation: Animation?) {
                hideShimmerLoading()
                //setPriceRangeData(priceRange)
                val fadeIn =
                    AnimationUtils.loadAnimation(context, R.anim.anim_view_fade_in)
                displayView.visible()
                displayView.startAnimation(fadeIn)
            }
        })
        if (displayShimmer.isVisible) {
            shimmerLayout.root.startAnimation(fadeOut)
        }
    }

    private fun hideShimmerLoading() = with(binding) {
        displayShimmer.gone()
        shimmerLayout.priceRangeShimmerLayout.stopShimmer()
        shimmerLayout.dateShimmerLayout.stopShimmer()
        shimmerLayout.shimmerExchangeRateLayout.stopShimmer()
        shimmerLayout.dollarTypeShimmerSwitch.stopShimmer()
        shimmerLayout.usdShimmerLayout.stopShimmer()
        shimmerLayout.lottieAnimationShimmerLayout.stopShimmer()
        shimmerLayout.bobShimmerLayout.stopShimmer()
        displayView.visible()
    }

    private fun initView() = with(binding) {
        displayShimmer.visible()
        displayView.gone()
    }

    private fun setListeners() {
        binding.etExchangeRate.setOnAmountChangeListener { amount ->
            if (binding.etExchangeRate.editText.hasFocus()) {
                exchangeRateValue = amount
                calculateFromExchangeRate()
            }
        }

        binding.etUsd.setOnAmountChangeListener { amount ->
            if (binding.etUsd.editText.hasFocus()) {
                usdValue = amount
                calculateFromUSD()
            }
        }

        binding.etBob.setOnAmountChangeListener { amount ->
            if (binding.etBob.editText.hasFocus()) {
                bobValue = amount
                calculateFromBOB()
            }
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
        //clearField() //test
    }

    private fun calculateFromExchangeRate() {
        // Cuando cambia la tasa de cambio, recalcular BOB basado en USD
        bobValue = exchangeRateValue * usdValue
        updateBobField()
    }


    private fun calculateFromUSD() {
        // Cuando cambia USD, calcular BOB
        bobValue = exchangeRateValue * usdValue
        updateBobField()
    }

    private fun calculateFromBOB() {
        // Cuando cambia BOB, calcular USD
        usdValue = if (exchangeRateValue != 0.0) {
            bobValue / exchangeRateValue
        } else {
            0.0
        }
        updateUsdField()
    }

    private fun updateBobField() {
        // Actualizar campo BOB sin triggerear su listener
        if (!binding.etBob.editText.hasFocus()) {
            binding.etBob.setAmountValue(bobValue)
        }
    }

    private fun updateUsdField() {
        // Actualizar campo USD sin triggerear su listener
        if (!binding.etUsd.editText.hasFocus()) {
            binding.etUsd.setAmountValue(usdValue)
        }
    }

    private fun updateExchangeRateField() {
        // Actualizar campo tasa sin triggerear su listener
        if (!binding.etExchangeRate.editText.hasFocus()) {
            binding.etExchangeRate.setAmountValue(exchangeRateValue)
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

    private fun highlightActiveField() {
        /*binding.exchangeRate.strokeWidth = 0
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
        }*/
    }

    private fun disableSystemKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }

    private fun hideSystemKeyboard() {
        val imm =
            context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
        context.getActivity()?.let {
            imm.hideSoftInputFromWindow(it.currentFocus?.windowToken, 0)
        }
    }

}