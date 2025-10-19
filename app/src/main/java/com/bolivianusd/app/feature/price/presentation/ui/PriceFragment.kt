package com.bolivianusd.app.feature.price.presentation.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.bolivianusd.app.R
import com.bolivianusd.app.core.base.BaseFragment
import com.bolivianusd.app.core.extensions.collectFlow
import com.bolivianusd.app.databinding.FragmentPriceBinding
import com.bolivianusd.app.feature.price.presentation.adapter.PriceAdapter
import com.bolivianusd.app.feature.price.presentation.viewmodel.PriceItemPagerViewModel
import com.bolivianusd.app.shared.domain.model.DollarType
import com.bolivianusd.app.shared.domain.model.TradeType
import com.bolivianusd.app.shared.domain.state.UiState
import com.google.android.material.tabs.TabLayoutMediator
import kotlin.getValue

class PriceFragment : BaseFragment<FragmentPriceBinding>() {

    private val viewModel: PriceItemPagerViewModel by activityViewModels()

    private val tradeType = TradeType.BUY
    private val priceAdapter = PriceAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isNotRecreate = false
    }

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentPriceBinding.inflate(inflater, container, false)

    override fun onStart() {
        super.onStart()
        viewModel.observePriceAndCandles(tradeType)
    }

    override fun initViews() {
        setupViewPager()
        resetDataUIComponents()
    }

    override fun initData() {
        viewModel.setDollarType(tradeType, DollarType.USDT)
    }

    override fun setupObservers() {
        setupPriceObserver()
        setupPriceRangeObserver()
        setupDailyCandleObserver()
    }

    private fun setupViewPager() = with(binding) {
        val titles = listOf(
            getString(R.string.price_view_pager_item_buy),
            getString(R.string.price_view_pager_item_sell)
        )

        viewPager.adapter = priceAdapter

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = titles[position]
        }.attach()
    }

    private fun setupPriceObserver() {
        collectFlow(viewModel.getPriceState(tradeType)) { state ->
            when (state) {
                is UiState.Loading -> priceAdapter.showPriceLoadingState(tradeType)
                is UiState.Success -> priceAdapter.showPriceDataSuccess(tradeType, state.data)
                is UiState.Error -> Unit
            }
        }
    }

    private fun setupPriceRangeObserver() {
        collectFlow(viewModel.getPriceRangeState(tradeType)) { state ->
            println("naty getPriceRangeState $state")
            when (state) {
                is UiState.Loading -> priceAdapter.showPriceRangeLoadingState(tradeType)
                is UiState.Success -> priceAdapter.showPriceRangeDataSuccess(tradeType, state.data)
                is UiState.Error -> Unit
            }
        }
    }

    private fun setupDailyCandleObserver() {
        collectFlow(viewModel.getDailyCandleState(tradeType)) { state ->
            when (state) {
                is UiState.Loading -> priceAdapter.showChartLoadingState(tradeType)
                is UiState.Success -> priceAdapter.showChartDataSuccess(tradeType, state.data)
                is UiState.Error -> Unit
            }
        }
    }

    private fun resetDataUIComponents() {
        priceAdapter.resetDataUIComponents(tradeType)
    }

    companion object {
        const val TAG = "PriceFragment"
        fun newInstance() = PriceFragment()
    }
}

