package com.bolivianusd.app.feature.price.presentation.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.viewpager2.widget.ViewPager2
import com.bolivianusd.app.R
import com.bolivianusd.app.core.base.BaseFragment
import com.bolivianusd.app.core.extensions.collectFlow
import com.bolivianusd.app.core.extensions.distinctByPrevious
import com.bolivianusd.app.core.extensions.gone
import com.bolivianusd.app.core.extensions.showToastError
import com.bolivianusd.app.core.extensions.showToastWarning
import com.bolivianusd.app.core.extensions.visible
import com.bolivianusd.app.core.util.ZERO
import com.bolivianusd.app.databinding.FragmentPriceBinding
import com.bolivianusd.app.feature.price.domain.model.DailyCandle
import com.bolivianusd.app.feature.price.presentation.adapter.PriceAdapter
import com.bolivianusd.app.feature.price.presentation.viewmodel.PriceViewModel
import com.bolivianusd.app.shared.domain.exception.NoConnectionWithOutDataException
import com.bolivianusd.app.shared.domain.exception.NoConnectionWithDataException
import com.bolivianusd.app.shared.domain.model.DollarType
import com.bolivianusd.app.shared.domain.model.Price
import com.bolivianusd.app.shared.domain.model.PriceRange
import com.bolivianusd.app.shared.domain.model.TradeType
import com.bolivianusd.app.shared.domain.state.UiState
import com.google.android.material.tabs.TabLayoutMediator

class PriceFragment : BaseFragment<FragmentPriceBinding>() {

    private val viewModel: PriceViewModel by activityViewModels()
    private val priceAdapter = PriceAdapter()

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentPriceBinding.inflate(inflater, container, false)

    override fun initViews() {
        setupViewPager()
        resetDataUIComponents()
    }

    override fun setListeners() = with(binding) {
        errorView.retryButton.setOnClickListener {
            retry()
        }
    }

    override fun initData() {
        viewModel.setTradeType(TradeType.BUY)
        viewModel.setDollarType(TradeType.BUY, DollarType.USDT)
    }

    override fun setupObservers() {
        collectFlow(viewModel.currentTradeType.state.distinctByPrevious()) { tradeType ->
            resetDataUIComponents()
            viewModel.observePriceAndCandles(tradeType)
            setupPriceObserver(tradeType)
            setupPriceRangeObserver(tradeType)
            setupDailyCandleObserver(tradeType)
            setEnabledSwitchDollar()
        }
    }

    override fun onUserFocusChanged(hasFocus: Boolean) {
        viewModel.setUserFocus(hasFocus)
    }

    private fun setupViewPager() = with(binding) {
        val titles = listOf(
            getString(R.string.price_view_item_buy),
            getString(R.string.price_view_item_sell)
        )
        priceAdapter.setOnDollarTypeChanged { dollarType ->
            viewModel.currentTradeType.value.let { tradeType ->
                viewModel.setDollarType(tradeType, dollarType)
            }
        }
        priceAdapter.setOnRefresh {
            retry()
        }
        viewPager.adapter = priceAdapter
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

    private fun setupPriceObserver(tradeType: TradeType) {
        collectFlow(viewModel.getPriceState(tradeType)) { state ->
            when (state) {
                is UiState.Loading -> priceAdapter.showPriceLoadingState(tradeType)
                is UiState.Success -> priceAdapter.showPriceDataSuccess(tradeType, state.data)
                is UiState.Error -> {
                    when (state.throwable) {
                        is NoConnectionWithDataException -> {
                            state.throwable.getData<Price>()?.let {
                                priceAdapter.showPriceDataSuccess(tradeType, it)
                            }
                            showToastWarning(getString(R.string.error_no_connection_with_data_exception))
                        }
                        is NoConnectionWithOutDataException -> showNoConnectionWithOutData()
                        else -> showToastError(getString(R.string.error_generic_exception))
                    }
                }
            }
        }
    }

    private fun setupPriceRangeObserver(tradeType: TradeType) {
        collectFlow(viewModel.getPriceRangeState(tradeType)) { state ->
            when (state) {
                is UiState.Loading -> priceAdapter.showPriceRangeLoadingState(tradeType)
                is UiState.Success -> priceAdapter.showPriceRangeDataSuccess(tradeType, state.data)
                is UiState.Error -> {
                    when (state.throwable) {
                        is NoConnectionWithDataException -> {
                            state.throwable.getData<PriceRange>()?.let {
                                priceAdapter.showPriceRangeDataSuccess(tradeType, it)
                            }
                            showToastWarning(getString(R.string.error_no_connection_with_data_exception))
                        }
                        is NoConnectionWithOutDataException -> showNoConnectionWithOutData()
                        else -> showToastError(getString(R.string.error_generic_exception))
                    }
                }
            }
        }
    }

    private fun setupDailyCandleObserver(tradeType: TradeType) {
        collectFlow(viewModel.getDailyCandleState(tradeType)) { state ->
            when (state) {
                is UiState.Loading -> priceAdapter.showChartLoadingState(tradeType)
                is UiState.Success -> priceAdapter.showChartDataSuccess(tradeType, state.data)
                is UiState.Error -> {
                    when (state.throwable) {
                        is NoConnectionWithDataException -> {
                            state.throwable.getData<List<DailyCandle>>()?.let {
                                priceAdapter.showChartDataSuccess(tradeType, it)
                            }
                            showToastWarning(getString(R.string.error_no_connection_with_data_exception))
                        }
                        is NoConnectionWithOutDataException -> showNoConnectionWithOutData()
                        else -> showToastError(getString(R.string.error_generic_exception))
                    }
                }
            }
        }
    }

    private fun setEnabledSwitchDollar() {
        val isEnabledSwitchDollar = viewModel.isEnabledSwitchDollar()
        priceAdapter.setEnabledSwitchDollar(isEnabledSwitchDollar)
    }

    private fun resetDataUIComponents() {
        viewModel.currentTradeType.value.let { tradeType ->
            priceAdapter.resetDataUIComponents(tradeType)
        }
    }

    private fun showNoConnectionWithOutData() = with(binding) {
        errorView.root.visible()
        contentView.gone()
    }

    private fun retry() = with(binding) {
        errorView.root.gone()
        contentView.visible()
        viewModel.currentTradeType.value.let { tradeType ->
            viewModel.refresh(tradeType)
        }
    }

    companion object {
        const val TAG = "PriceFragment"
        fun newInstance() = PriceFragment()
    }
}
