package com.bolivianusd.app.feature.calculator.presentation.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.viewpager2.widget.ViewPager2
import com.bolivianusd.app.R
import com.bolivianusd.app.core.base.BaseFragment
import com.bolivianusd.app.core.extensions.collectFlow
import com.bolivianusd.app.core.extensions.hideSystemKeyboard
import com.bolivianusd.app.core.extensions.showToastError
import com.bolivianusd.app.core.extensions.showToastWarning
import com.bolivianusd.app.core.util.ZERO
import com.bolivianusd.app.databinding.FragmentCalculatorBinding
import com.bolivianusd.app.feature.calculator.presentation.adapter.CalculatorAdapter
import com.bolivianusd.app.feature.calculator.presentation.viewmodel.CalculatorViewModel
import com.bolivianusd.app.shared.domain.model.DollarType
import com.bolivianusd.app.shared.domain.model.TradeType
import com.bolivianusd.app.shared.domain.state.UiState
import com.google.android.material.tabs.TabLayoutMediator
import kotlin.getValue

class CalculatorFragment : BaseFragment<FragmentCalculatorBinding>() {

    private val viewModel: CalculatorViewModel by activityViewModels()
    private val calculatorAdapter = CalculatorAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isNotRecreate = false
    }

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentCalculatorBinding.inflate(inflater, container, false)

    override fun initViews() {
        setupViewPager()
        resetDataUIComponents()
    }

    override fun initData() = with(binding) {
        viewModel.setTradeType(TradeType.BUY)
        viewModel.setDollarType(TradeType.BUY, DollarType.USDT)
    }

    override fun setupObservers() {
        collectFlow(viewModel.currentTradeType.state) { tradeType ->
            resetDataUIComponents()
            viewModel.observePriceRange(tradeType)
            setupPriceRangeObserver(tradeType)
        }
    }

    private fun setupViewPager() = with(binding) {
        val titles = listOf(
            getString(R.string.calculator_view_pager_item_buy),
            getString(R.string.calculator_view_pager_item_sell)
        )
        calculatorAdapter.setOnDollarTypeChanged { dollarType ->
            viewModel.currentTradeType.value.let { tradeType ->
                viewModel.setDollarType(tradeType, dollarType)
            }
        }
        calculatorAdapter.setOnFormatError {
            showToastError(getString(R.string.calculator_view_pager_item_input_error))
        }
        calculatorAdapter.setOnPriceRangeError { message ->
            showToastWarning(message)
        }
        viewPager.adapter = calculatorAdapter
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                val newTradeType = when (position) {
                    ZERO -> TradeType.BUY
                    else -> TradeType.SELL
                }
                viewModel.setTradeType(newTradeType)
            }
        })

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = titles[position]
        }.attach()
    }

    private fun setupPriceRangeObserver(tradeType: TradeType) = with(binding) {
        collectFlow(viewModel.getPriceRangeState(tradeType)) { state ->
            when (state) {
                is UiState.Loading -> calculatorAdapter.showPriceRangeLoadingState(tradeType)
                is UiState.Success -> calculatorAdapter.showPriceRangeDataSuccess(tradeType, state.data)
                is UiState.Error -> Unit
            }
        }
    }

    private fun resetDataUIComponents() {
        viewModel.currentTradeType.value.let { tradeType ->
            calculatorAdapter.resetUIComponents(tradeType)
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
        const val TAG = "CalculatorFragment"
        fun newInstance() = CalculatorFragment()
    }
}
