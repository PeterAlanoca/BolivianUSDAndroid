package com.bolivianusd.app.feature.calculator.presentation.view

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.RelativeLayout
import com.bolivianusd.app.R
import com.bolivianusd.app.core.extensions.getActivity
import com.bolivianusd.app.core.extensions.getMaxLength
import com.bolivianusd.app.core.extensions.gone
import com.bolivianusd.app.core.extensions.invisible
import com.bolivianusd.app.core.extensions.visible
import com.bolivianusd.app.core.util.ONE_D
import com.bolivianusd.app.core.util.ONE_F
import com.bolivianusd.app.core.util.ZERO_D
import com.bolivianusd.app.core.util.ZERO_F
import com.bolivianusd.app.databinding.ViewDisplayBinding
import com.bolivianusd.app.shared.domain.model.DollarType
import com.bolivianusd.app.shared.domain.model.PriceRange

class DisplayView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : RelativeLayout(context, attrs, defStyleAttr) {

    private val binding: ViewDisplayBinding by lazy {
        ViewDisplayBinding.inflate(LayoutInflater.from(context), this, true)
    }
    private var currentFocusField: EditText? = null
    private var onDollarTypeChanged: ((DollarType) -> Unit)? = null
    private var onFormatError: (() -> Unit)? = null
    private var onPriceRangeError: ((String) -> Unit)? = null
    private var exchangeRateValue = ZERO_D
    private var usdValue = ZERO_D
    private var bobValue = ZERO_D
    private var dollarType = DollarType.USDT
    private var priceRange: PriceRange? = null

    init {
        initView()
        setListeners()
        setupTouchListeners()
        disableSystemKeyboard()
    }

    fun setOnDollarTypeChanged(onDollarTypeChanged: ((DollarType) -> Unit)) {
        this.onDollarTypeChanged = onDollarTypeChanged
    }

    fun setOnFormatError(onFormatError: () -> Unit) {
        this.onFormatError = onFormatError
    }

    fun setOnPriceRangeError(onPriceRangeError: (String) -> Unit) {
       this.onPriceRangeError = onPriceRangeError
    }

    fun deleteNumberField() = with(binding) {
        when (currentFocusField) {
            etExchangeRate.editText -> {
                deleteNumber(etExchangeRate)
            }

            etUsd.editText -> {
                deleteNumber(etUsd)
            }

            etBob.editText -> {
                deleteNumber(etBob)
            }
        }
    }

    fun appendNumberField(value: Int) = with(binding) {
        when (currentFocusField) {
            etExchangeRate.editText -> {
                appendNumber(etExchangeRate, value)
            }

            etUsd.editText -> {
                appendNumber(etUsd, value)
            }

            etBob.editText -> {
                appendNumber(etBob, value)
            }
        }
    }

    fun clearField() = with(binding) {
        when (currentFocusField) {
            etExchangeRate.editText -> {
                clearNumber(etExchangeRate)
            }

            etUsd.editText -> {
                clearNumber(etUsd)
            }

            etBob.editText -> {
                clearNumber(etBob)
            }
        }
    }

    fun showPriceRangeLoadingState() = with(binding) {
        displayView.apply {
            alpha = ZERO_F
            gone()
        }
        shimmerLayout.priceRangeShimmerLayout.apply {
            alpha = ONE_F
            visible()
        }
        shimmerLayout.dateShimmerLayout.apply {
            alpha = ONE_F
            visible()
        }
        shimmerLayout.shimmerExchangeRateLayout.apply {
            alpha = ONE_F
            visible()
        }
        shimmerLayout.dollarTypeShimmerSwitch.apply {
            alpha = ONE_F
            visible()
        }
        shimmerLayout.usdShimmerLayout.apply {
            alpha = ONE_F
            visible()
        }
        shimmerLayout.lottieAnimationShimmerLayout.apply {
            alpha = ONE_F
            visible()
        }
        shimmerLayout.bobShimmerLayout.apply {
            alpha = ONE_F
            visible()
        }

        shimmerLayout.priceRangeShimmerLayout.startShimmer()
        shimmerLayout.dateShimmerLayout.startShimmer()
        shimmerLayout.shimmerExchangeRateLayout.startShimmer()
        shimmerLayout.dollarTypeShimmerSwitch.startShimmer()
        shimmerLayout.usdShimmerLayout.startShimmer()
        shimmerLayout.lottieAnimationShimmerLayout.startShimmer()
        shimmerLayout.bobShimmerLayout.startShimmer()
    }

    fun showPriceRangeDataSuccess(priceRange: PriceRange) = with(binding) {
        shimmerLayout.priceRangeShimmerLayout.animate().cancel()
        shimmerLayout.dateShimmerLayout.animate().cancel()
        shimmerLayout.shimmerExchangeRateLayout.animate().cancel()
        shimmerLayout.dollarTypeShimmerSwitch.animate().cancel()
        shimmerLayout.usdShimmerLayout.animate().cancel()
        shimmerLayout.lottieAnimationShimmerLayout.animate().cancel()
        shimmerLayout.bobShimmerLayout.animate().cancel()
        setPriceRangeData(priceRange)
        //encaso de que sea visible faltaria el reconociemitno facial
        shimmerLayout.priceRangeShimmerLayout.animate()
            .alpha(ZERO_F)
            .setDuration(DURATION_ANIMATION_FADE_IN_OUT)
            .start()
        shimmerLayout.dateShimmerLayout.animate()
            .alpha(ZERO_F)
            .setDuration(DURATION_ANIMATION_FADE_IN_OUT)
            .start()
        shimmerLayout.shimmerExchangeRateLayout.animate()
            .alpha(ZERO_F)
            .setDuration(DURATION_ANIMATION_FADE_IN_OUT)
            .start()
        shimmerLayout.dollarTypeShimmerSwitch.animate()
            .alpha(ZERO_F)
            .setDuration(DURATION_ANIMATION_FADE_IN_OUT)
            .start()
        shimmerLayout.usdShimmerLayout.animate()
            .alpha(ZERO_F)
            .setDuration(DURATION_ANIMATION_FADE_IN_OUT)
            .start()
        shimmerLayout.lottieAnimationShimmerLayout.animate()
            .alpha(ZERO_F)
            .setDuration(DURATION_ANIMATION_FADE_IN_OUT)
            .start()
        shimmerLayout.bobShimmerLayout.animate()
            .alpha(ZERO_F)
            .setDuration(DURATION_ANIMATION_FADE_IN_OUT)
            .withEndAction {
                hideShimmerLoading()
            }
            .start()

        displayView.apply {
            alpha = ZERO_F
            visible()
        }
        displayView.animate()
            .alpha(ONE_F)
            .setDuration(DURATION_ANIMATION_FADE_IN_OUT)
            .start()


        /*if (displayView.isVisible) {
            return@with
        }
        val fadeOut = AnimationUtils.loadAnimation(context, R.anim.anim_view_fade_out)
        fadeOut.setAnimationListener(object : SimpleAnimationListener() {
            override fun onAnimationEnd(animation: Animation?) {
                hideShimmerLoading()
                setPriceRangeData(priceRange)
                val fadeIn =
                    AnimationUtils.loadAnimation(context, R.anim.anim_view_fade_in)
                displayView.visible()
                displayView.startAnimation(fadeIn)
            }
        })
        if (displayShimmer.isVisible) {
            shimmerLayout.root.startAnimation(fadeOut)
        }*/
    }

    private fun setPriceRangeData(priceRange: PriceRange) = with(binding) {
        this@DisplayView.priceRange = priceRange
        exchangeRateValue = priceRange.median.value.toDouble()
        usdValue = ONE_D
        bobValue = exchangeRateValue * usdValue

        priceRangeTextView.text = priceRange.descriptionLabel
        updateAtTextView.text = priceRange.updatedAtFormat
        etExchangeRate.setAmountValue(exchangeRateValue)
        etUsd.setAmountValue(usdValue)
        usdLabel.text = priceRange.asset
        etBob.setAmountValue(bobValue)
        bobLabel.text = priceRange.fiat

        currentFocusField = etUsd.editText
        etUsd.editText.postDelayed({
            etUsd.editText.requestFocus()
        }, DELAY_REQUEST_FOCUS)
        updateExchangeRateLabel()
        calculateMaxAmount()
    }

    fun resetUIComponents() = with(binding) {
        displayView.alpha = ONE_F
    }

    private fun hideShimmerLoading() = with(binding) {
        shimmerLayout.priceRangeShimmerLayout.stopShimmer()
        shimmerLayout.dateShimmerLayout.stopShimmer()
        shimmerLayout.shimmerExchangeRateLayout.stopShimmer()
        shimmerLayout.dollarTypeShimmerSwitch.stopShimmer()
        shimmerLayout.usdShimmerLayout.stopShimmer()
        shimmerLayout.lottieAnimationShimmerLayout.stopShimmer()
        shimmerLayout.bobShimmerLayout.stopShimmer()
        shimmerLayout.priceRangeShimmerLayout.alpha = ONE_F
        shimmerLayout.priceRangeShimmerLayout.invisible()
        shimmerLayout.dateShimmerLayout.alpha = ONE_F
        shimmerLayout.dateShimmerLayout.invisible()
        shimmerLayout.shimmerExchangeRateLayout.alpha = ONE_F
        shimmerLayout.shimmerExchangeRateLayout.invisible()
        shimmerLayout.dollarTypeShimmerSwitch.alpha = ONE_F
        shimmerLayout.dollarTypeShimmerSwitch.invisible()
        shimmerLayout.usdShimmerLayout.alpha = ONE_F
        shimmerLayout.usdShimmerLayout.invisible()
        shimmerLayout.lottieAnimationShimmerLayout.alpha = ONE_F
        shimmerLayout.lottieAnimationShimmerLayout.invisible()
        shimmerLayout.bobShimmerLayout.alpha = ONE_F
        shimmerLayout.bobShimmerLayout.invisible()
    }

    private fun initView() = with(binding) {
        displayShimmer.visible()
        displayView.gone()
    }

    private fun setListeners() = with(binding) {
        dollarTypeSwitch.setOnCheckedChangeListener { _, isChecked ->
            dollarType = if (isChecked) DollarType.USD else DollarType.USDT
            onDollarTypeChanged?.invoke(dollarType)
        }
        etExchangeRate.apply {
            setOnAmountChangeListener { amount ->
                validateRequestFocus()
                post {
                    if (editText.hasFocus()) {
                        exchangeRateValue = amount
                        calculateFromExchangeRate()
                        calculateMaxAmount()
                        validatePriceRange()
                    }
                }
            }
            setOnFormatError {
                showFormatError(exchangeRate)
            }
        }
        etUsd.apply {
            setOnAmountChangeListener { amount ->
                validateRequestFocus()
                post {
                    if (editText.hasFocus()) {
                        usdValue = amount
                        calculateFromUSD()
                    }
                }
            }
            setOnFormatError {
                showFormatError(usd)
            }
        }
        etBob.apply {
            setOnAmountChangeListener { amount ->
                validateRequestFocus()
                post {
                    if (editText.hasFocus()) {
                        bobValue = amount
                        calculateFromBOB()
                    }
                }
            }
            setOnFormatError {
                showFormatError(bob)
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setupTouchListeners() = with(binding) {
        val blockingTouchListener = OnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                v.requestFocus()
                hideSystemKeyboard()
                setCurrentFocus(v as EditText)
            }
            true
        }

        etExchangeRate.editText.setOnTouchListener(blockingTouchListener)
        etUsd.editText.setOnTouchListener(blockingTouchListener)
        etBob.editText.setOnTouchListener(blockingTouchListener)
    }

    private fun setCurrentFocus(field: EditText) {
        currentFocusField = field
        field.requestFocus()
        hideSystemKeyboard()
        highlightActiveField()
        //clearField() //test
    }

    private fun calculateFromExchangeRate() {
        bobValue = exchangeRateValue * usdValue
        binding.etBob.setAmountValue(ZERO_D)
        updateBobField()
    }

    private fun calculateFromUSD() {
        bobValue = exchangeRateValue * usdValue
        updateBobField()
    }

    private fun calculateFromBOB() {
        usdValue = if (exchangeRateValue != ZERO_D) {
            bobValue / exchangeRateValue
        } else {
            ZERO_D
        }
        updateUsdField()
    }

    private fun calculateMaxAmount() = with(binding) {
        val maxUsd = USD_MAX_VALUE
        val maxBob = exchangeRateValue * maxUsd
        etExchangeRate.setMaxAmount(EXCHANGE_RATE_MAX_VALUE)
        etUsd.setMaxAmount(maxUsd)
        etBob.setMaxAmount(maxBob)
    }

    private fun validatePriceRange() {
        priceRange?.let {
            val minValue = it.min.value.toDouble()
            val maxValue = it.max.value.toDouble()
            val minLabel = it.min.valueLabel
            val maxLabel = it.max.valueLabel
            if (exchangeRateValue < minValue || exchangeRateValue > maxValue) {
                val message = context.getString(R.string.calculator_view_pager_item_price_range_error, minLabel, maxLabel)
                onPriceRangeError?.invoke(message)
            }
        }
    }

    private fun updateBobField() = with(binding) {
        if (!etBob.editText.hasFocus()) {
            etBob.setAmountValue(bobValue)
        }
    }

    private fun updateUsdField() = with(binding) {
        if (!etUsd.editText.hasFocus()) {
            etUsd.setAmountValue(usdValue)
        }
    }

    private fun updateExchangeRateField() = with(binding) {
        if (!etExchangeRate.editText.hasFocus()) {
            etExchangeRate.setAmountValue(exchangeRateValue)
        }
    }

    private fun clearValues() = with(binding) {
        usdValue = ZERO_D
        bobValue = ZERO_D
        etUsd.clearAmount()
        etBob.clearAmount()
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

    private fun updateExchangeRateLabel() = with(binding) {
        val exchangeRateDescriptionResId = when (dollarType) {
            DollarType.USDT -> R.string.calculator_view_pager_item_parallel_dollar
            DollarType.USD -> R.string.calculator_view_pager_item_official_dollar
        }
        exchangeRateLabel.text = context.getString(exchangeRateDescriptionResId)
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

    private fun validateRequestFocus() = with(binding) {
        when (currentFocusField) {
            etExchangeRate.editText -> {
                if (!etExchangeRate.editText.hasFocus()) {
                    etExchangeRate.editText.post {
                        etExchangeRate.editText.requestFocus()
                    }
                }
            }

            etUsd.editText -> {
                if (!etUsd.editText.hasFocus()) {
                    etUsd.editText.post {
                        etUsd.editText.requestFocus()
                    }
                }
            }

            etBob.editText -> {
                if (!etBob.editText.hasFocus()) {
                    etBob.editText.post {
                        etBob.editText.requestFocus()
                    }
                }
            }
        }
    }

    private fun disableSystemKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }

    private fun showFormatError(view: View) {
        val animation = AnimationUtils.loadAnimation(context, R.anim.anim_shake_error)
        animation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) {
                onFormatError?.invoke()
            }
            override fun onAnimationRepeat(animation: Animation?) {}

            override fun onAnimationEnd(animation: Animation?) {
                clearValues()
            }
        })
        view.startAnimation(animation)
    }

    private fun hideSystemKeyboard() {
        val imm =
            context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
        context.getActivity()?.let {
            imm.hideSoftInputFromWindow(it.currentFocus?.windowToken, 0)
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        validateRequestFocus()
    }

    companion object {
        private const val DELAY_REQUEST_FOCUS = 250L
        private const val USD_MAX_VALUE = 999999999.99
        private const val EXCHANGE_RATE_MAX_VALUE = 9999.99
        private const val DURATION_ANIMATION_FADE_IN_OUT = 300L
    }

}