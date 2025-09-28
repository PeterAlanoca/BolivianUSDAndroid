package com.bolivianusd.app.feature.calculator.presentation.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.text.Editable
import android.text.TextWatcher
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.google.android.material.textfield.TextInputEditText
import com.bolivianusd.app.R
import com.bolivianusd.app.core.base.BaseFragment
import com.bolivianusd.app.core.extensions.serializable
import com.bolivianusd.app.databinding.FragmentCalculatorItemPagerBinding
import com.bolivianusd.app.shared.domain.model.TradeType

class CalculatorItemPagerFragment : BaseFragment<FragmentCalculatorItemPagerBinding>() {

    private val tradeType: TradeType by lazy {
        requireNotNull(arguments?.serializable<TradeType>(TRADER_TYPE))
    }

    private var exchangeRateValue = 6.86
    private var usdValue = 1.00
    private var bobValue = 6.86
    private var currentFocusField: TextInputEditText? = null
    private var isUpdating = false

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
        setupTextWatchers()
        setupTouchListeners()
        disableSystemKeyboard()
        setInitialFocus()
    }

    private fun disableSystemKeyboard() {
        // Método para deshabilitar completamente el teclado del sistema
        val imm =
            requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        // Ocultar teclado inmediatamente
        imm.hideSoftInputFromWindow(view?.windowToken, 0)

        // Prevenir que se muestre el teclado
        binding.etExchangeRate.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                hideSystemKeyboard()
                setCurrentFocus(binding.etExchangeRate)
            }
            true
        }

        binding.etUsd.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                hideSystemKeyboard()
                setCurrentFocus(binding.etUsd)
            }
            true
        }

        binding.etBob.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                hideSystemKeyboard()
                setCurrentFocus(binding.etBob)
            }
            true
        }
    }

    private fun hideSystemKeyboard() {
        val imm =
            requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view?.windowToken, 0)

        // También ocultar teclado para la actividad
        activity?.let {
            imm.hideSoftInputFromWindow(it.currentFocus?.windowToken, 0)
        }
    }

    private fun setupUI() {
        // Configurar valores iniciales
        binding.etExchangeRate.setText(exchangeRateValue.toString())
        binding.etUsd.setText(usdValue.toString())
        binding.etBob.setText(bobValue.toString())

        // Hacer que los EditText no sean enfocables por el sistema
        binding.etExchangeRate.isFocusable = true
        binding.etExchangeRate.isFocusableInTouchMode = true
        binding.etUsd.isFocusable = true
        binding.etUsd.isFocusableInTouchMode = true
        binding.etBob.isFocusable = true
        binding.etBob.isFocusableInTouchMode = true
    }

    private fun setupClickListeners() {
        // Configurar clics para los campos de entrada
        binding.etExchangeRate.setOnClickListener {
            setCurrentFocus(binding.etExchangeRate)
        }

        binding.etUsd.setOnClickListener {
            setCurrentFocus(binding.etUsd)
        }

        binding.etBob.setOnClickListener {
            setCurrentFocus(binding.etBob)
        }

        // Configurar clics para los botones numéricos
        setupNumberButtonClicks()

        // Configurar clic para el botón de refresh
        binding.refreshButton.setOnClickListener {
            refreshExchangeRate()
        }
    }

    private fun setupTouchListeners() {
        // Touch listener adicional para prevenir cualquier aparición del teclado
        val blockingTouchListener = View.OnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                v.requestFocus()
                hideSystemKeyboard()
                setCurrentFocus(v as TextInputEditText)
            }
            true // Consumir el evento
        }

        binding.etExchangeRate.setOnTouchListener(blockingTouchListener)
        binding.etUsd.setOnTouchListener(blockingTouchListener)
        binding.etBob.setOnTouchListener(blockingTouchListener)
    }

    private fun setupNumberButtonClicks() {
        // Configurar listeners para cada botón del teclado
        binding.btn0.setOnClickListener { appendNumber("0") }
        binding.btn1.setOnClickListener { appendNumber("1") }
        binding.btn2.setOnClickListener { appendNumber("2") }
        binding.btn3.setOnClickListener { appendNumber("3") }
        binding.btn4.setOnClickListener { appendNumber("4") }
        binding.btn5.setOnClickListener { appendNumber("5") }
        binding.btn6.setOnClickListener { appendNumber("6") }
        binding.btn7.setOnClickListener { appendNumber("7") }
        binding.btn8.setOnClickListener { appendNumber("8") }
        binding.btn9.setOnClickListener { appendNumber("9") }
        binding.btnDecimal.setOnClickListener { addDecimalPoint() }
        binding.btnClear.setOnClickListener { clearInput() }
    }

    private fun setupTextWatchers() {
        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                if (isUpdating) return

                when (currentFocusField) {
                    binding.etExchangeRate -> {
                        val newRate = s.toString().toDoubleOrNull() ?: exchangeRateValue
                        if (newRate != exchangeRateValue) {
                            exchangeRateValue = newRate
                            updateBobFromUsd()
                        }
                    }

                    binding.etUsd -> {
                        val newUsd = s.toString().toDoubleOrNull() ?: 0.0
                        if (newUsd != usdValue) {
                            usdValue = newUsd
                            updateBobFromUsd()
                        }
                    }

                    binding.etBob -> {
                        val newBob = s.toString().toDoubleOrNull() ?: 0.0
                        if (newBob != bobValue) {
                            bobValue = newBob
                            updateUsdFromBob()
                        }
                    }
                }
            }
        }

        binding.etExchangeRate.addTextChangedListener(textWatcher)
        binding.etUsd.addTextChangedListener(textWatcher)
        binding.etBob.addTextChangedListener(textWatcher)
    }

    private fun setCurrentFocus(field: TextInputEditText) {
        currentFocusField = field
        field.requestFocus()
        hideSystemKeyboard()

        // Resaltar el campo activo
        highlightActiveField()
    }

    private fun highlightActiveField() {
        // Resetear todos los campos
        binding.exchangeRate.strokeWidth = 0
        binding.usd.strokeWidth = 0
        binding.bob.strokeWidth = 0

        // Resaltar el campo activo
        when (currentFocusField) {
            binding.etExchangeRate -> {
                binding.exchangeRate.strokeWidth = 2
                binding.exchangeRate.strokeColor = requireContext().getColor(R.color.white)
            }

            binding.etUsd -> {
                binding.usd.strokeWidth = 2
                binding.usd.strokeColor = requireContext().getColor(R.color.white)
            }

            binding.etBob -> {
                binding.bob.strokeWidth = 2
                binding.bob.strokeColor = requireContext().getColor(R.color.white)
            }
        }
    }

    private fun appendNumber(number: String) {
        val currentField = currentFocusField ?: return
        val currentText = currentField.text?.toString() ?: ""

        // Evitar múltiples ceros al inicio
        if (currentText == "0" && number == "0") return

        // Si el texto actual es "0", reemplazarlo con el nuevo número
        val newText = if (currentText == "0") number else currentText + number

        isUpdating = true
        currentField.setText(newText)
        currentField.setSelection(newText.length)
        isUpdating = false

        // Forzar la actualización de valores
        updateValuesFromField(currentField, newText)
    }

    private fun addDecimalPoint() {
        val currentField = currentFocusField ?: return
        val currentText = currentField.text?.toString() ?: ""

        if (!currentText.contains(".")) {
            val newText = if (currentText.isEmpty()) "0." else "$currentText."

            isUpdating = true
            currentField.setText(newText)
            currentField.setSelection(newText.length)
            isUpdating = false

            updateValuesFromField(currentField, newText)
        }
    }

    private fun clearInput() {
        val currentField = currentFocusField ?: return

        isUpdating = true
        currentField.setText("0")
        currentField.setSelection(1)
        isUpdating = false

        updateValuesFromField(currentField, "0")
    }

    private fun updateValuesFromField(field: TextInputEditText, text: String) {
        val value = text.toDoubleOrNull() ?: 0.0

        when (field) {
            binding.etExchangeRate -> {
                exchangeRateValue = value
                updateBobFromUsd()
            }

            binding.etUsd -> {
                usdValue = value
                updateBobFromUsd()
            }

            binding.etBob -> {
                bobValue = value
                updateUsdFromBob()
            }
        }
    }

    private fun updateBobFromUsd() {
        isUpdating = true
        bobValue = usdValue * exchangeRateValue
        binding.etBob.setText(formatNumber(bobValue))
        isUpdating = false
    }

    private fun updateUsdFromBob() {
        isUpdating = true
        if (exchangeRateValue != 0.0) {
            usdValue = bobValue / exchangeRateValue
            binding.etUsd.setText(formatNumber(usdValue))
        }
        isUpdating = false
    }

    private fun formatNumber(value: Double): String {
        return if (value % 1 == 0.0) {
            value.toInt().toString()
        } else {
            String.format("%.2f", value).trimEnd('0').trimEnd('.')
        }
    }

    private fun setInitialFocus() {
        setCurrentFocus(binding.etUsd)
    }

    private fun refreshExchangeRate() {
        // Animación del botón refresh
        binding.lottieAnimationView.playAnimation()

        // Simular actualización del tipo de cambio
        val newRate = 6.85 + (Math.random() * 0.1)
        exchangeRateValue = newRate

        isUpdating = true
        binding.etExchangeRate.setText(formatNumber(exchangeRateValue))
        updateBobFromUsd()
        isUpdating = false

        Toast.makeText(requireContext(), "Tipo de cambio actualizado", Toast.LENGTH_SHORT).show()
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