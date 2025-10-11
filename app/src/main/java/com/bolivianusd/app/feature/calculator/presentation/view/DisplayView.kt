package com.bolivianusd.app.feature.calculator.presentation.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.RelativeLayout
import com.bolivianusd.app.core.extensions.visible
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

    init {
        initView()
        setListeners()
    }

    private fun initView() = with(binding) {
        displayView.visible()
    }

    private fun setListeners() {
        // Usar el listener personalizado de AmountEditText
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




}