package com.bolivianusd.app.feature.price.presentation.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.bolivianusd.app.core.base.BaseFragment
import com.bolivianusd.app.core.extensions.collectFlow
import com.bolivianusd.app.core.extensions.serializable
import com.bolivianusd.app.databinding.FragmentPriceItemPagerBinding
import com.bolivianusd.app.feature.price.presentation.viewmodel.PriceViewModel
import com.bolivianusd.app.shared.domain.model.DollarType
import com.bolivianusd.app.shared.domain.model.TradeType
import com.bolivianusd.app.shared.domain.state.UiState

class PriceItemPagerFragment : BaseFragment<FragmentPriceItemPagerBinding>() {

    private val viewModel: PriceViewModel by activityViewModels()

    private val tradeType: TradeType by lazy {
        requireNotNull(arguments?.serializable<TradeType>(TRADER_TYPE))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isNotRecreate = false
    }

    override fun onStart() {
        super.onStart()
        // Iniciar la observación cuando el fragment se hace visible
        viewModel.observePriceAndCandles(tradeType)
    }

    override fun onStop() {
        super.onStop()
        // Opcional: limpiar cuando el fragment no es visible
        // viewModel.clearTradeType(tradeType)
    }

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentPriceItemPagerBinding.inflate(inflater, container, false)

    override fun initViews() {
        resetDataUIComponents()
    }

    override fun setListeners() = with(binding) {
        priceView.setOnDollarTypeChanged { dollarType ->
            resetDataUIComponents()
            viewModel.setDollarType(tradeType, dollarType)
        }
    }

    override fun initData() {
        viewModel.setDollarType(tradeType, DollarType.USDT)
    }

    override fun setupObservers() {
        setupPriceObserver()
        setupPriceRangeObserver()
        setupDailyCandleObserver()
    }

    private fun setupPriceObserver() = with(binding) {
        collectFlow(viewModel.getPriceState(tradeType)) { state ->
            when (state) {
                is UiState.Loading -> priceView.showPriceLoadingState()
                is UiState.Success -> priceView.showPriceDataSuccess(state.data)
                is UiState.Error -> Unit
            }
        }
    }

    private fun setupPriceRangeObserver() = with(binding) {
        collectFlow(viewModel.getPriceRangeState(tradeType)) { state ->
            when (state) {
                is UiState.Loading -> priceRangeView.showPriceRangeLoadingState()
                is UiState.Success -> priceRangeView.showPriceRangeDataSuccess(state.data)
                is UiState.Error -> Unit
            }
        }
    }

    private fun setupDailyCandleObserver() = with(binding) {
        collectFlow(viewModel.getDailyCandleState(tradeType)) { state ->
            when (state) {
                is UiState.Loading -> dailyCandleChartView.showChartLoadingState()
                is UiState.Success -> dailyCandleChartView.showChartDataSuccess(state.data)
                is UiState.Error -> Unit
            }
        }
    }

    private fun resetDataUIComponents() = with(binding) {
        priceView.resetDataUIComponents()
        priceRangeView.resetDataUIComponents()
        dailyCandleChartView.resetDataUIComponents()
    }

    companion object {
        private const val TRADER_TYPE = "TRADER_TYPE"

        fun newInstance(tradeType: TradeType) = PriceItemPagerFragment().apply {
            arguments = Bundle().apply {
                putSerializable(TRADER_TYPE, tradeType)
            }
        }
    }
}