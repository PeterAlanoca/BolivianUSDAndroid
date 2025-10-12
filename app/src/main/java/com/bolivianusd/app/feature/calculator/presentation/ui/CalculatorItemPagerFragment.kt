package com.bolivianusd.app.feature.calculator.presentation.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.bolivianusd.app.core.base.BaseFragment
import com.bolivianusd.app.core.extensions.collectFlow
import com.bolivianusd.app.core.extensions.hideSystemKeyboard
import com.bolivianusd.app.core.extensions.serializable
import com.bolivianusd.app.databinding.FragmentCalculatorItemPagerBinding
import com.bolivianusd.app.feature.calculator.presentation.viewmodel.CalculatorItemPagerViewModel
import com.bolivianusd.app.shared.domain.model.DollarType
import com.bolivianusd.app.shared.domain.model.PriceRange
import com.bolivianusd.app.shared.domain.model.TradeType
import com.bolivianusd.app.shared.domain.state.UiState
import kotlin.getValue

class CalculatorItemPagerFragment : BaseFragment<FragmentCalculatorItemPagerBinding>() {

    private val viewModel: CalculatorItemPagerViewModel by activityViewModels()

    private val tradeType: TradeType by lazy {
        requireNotNull(arguments?.serializable<TradeType>(TRADER_TYPE))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isNotRecreate = false
    }

    override fun onStart() {
        super.onStart()
        println("naty onStart ")
        viewModel.getPriceAndCandles(tradeType)
    }

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentCalculatorItemPagerBinding.inflate(inflater, container, false)

    override fun setListeners() = with(binding) {
        displayView.setOnDollarTypeChanged { dollarType ->
            viewModel.setDollarType(tradeType, dollarType)
            displayView.resetUIComponents()
            keyboardView.resetUIComponents()
        }
        keyboardView.setOnClearClickListener {
            displayView.clearField()
        }
        keyboardView.setOnDeleteClickListener {
            displayView.deleteNumberField()
        }
        keyboardView.setOnNumberClickListener {
            displayView.appendNumberField(it)
        }
    }

    override fun initData() = with(binding) {
        viewModel.setDollarType(tradeType,displayView.dollarType)
    }

    override fun setupObservers() {
        setupPriceRangeObserver()
    }

    private fun setupPriceRangeObserver() = with(binding) {
        collectFlow(viewModel.getPriceRangeState(tradeType)) { state ->
            when (state) {
                is UiState.Loading -> {
                    showShimmerLoading()
                    println("naty Loading")
                }
                is UiState.Success -> {
                    showContentView(state.data)
                    println("naty Success siuuuuuuuuuuuu")
                    println("naty Success ${state.data}")
                }
                is UiState.Error -> Unit
            }
        }
    }

    private fun showShimmerLoading() = with(binding) {
        displayView.showShimmerLoading()
        keyboardView.showShimmerLoading()
    }

    private fun showContentView(priceRange: PriceRange) = with(binding) {
        displayView.setData(priceRange)
        displayView.showContentView()
        keyboardView.showContentView()
    }

    override fun onResume() {
        super.onResume()
        println("naty onResume ")
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
