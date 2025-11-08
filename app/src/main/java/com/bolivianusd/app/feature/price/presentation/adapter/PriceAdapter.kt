package com.bolivianusd.app.feature.price.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bolivianusd.app.databinding.ItemPriceBinding
import com.bolivianusd.app.feature.price.domain.model.DailyCandle
import com.bolivianusd.app.shared.domain.model.DollarType
import com.bolivianusd.app.shared.domain.model.Price
import com.bolivianusd.app.shared.domain.model.PriceRange
import com.bolivianusd.app.shared.domain.model.TradeType

class PriceAdapter : RecyclerView.Adapter<PriceAdapter.PriceHolder>() {

    private val items = listOf(TradeType.BUY, TradeType.SELL)
    private val viewHolders = mutableMapOf<TradeType, PriceHolder>()
    private var actionDollarType: ((DollarType) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PriceHolder {
        val binding = ItemPriceBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return PriceHolder(binding)
    }

    override fun onBindViewHolder(holder: PriceHolder, position: Int) {
        val tradeType = items[position]
        holder.bind(tradeType)
        viewHolders[tradeType] = holder
    }

    override fun getItemCount(): Int = items.size

    fun setOnDollarTypeChanged(actionDollarType: (DollarType) -> Unit) {
        this.actionDollarType = actionDollarType
    }

    fun setEnabledSwitchDollar(isEnabled: Boolean) {
        viewHolders.forEach {
            it.value.setEnabledSwitchDollar(isEnabled)
        }
    }

    fun showPriceLoadingState(tradeType: TradeType) {
        viewHolders[tradeType]?.showPriceLoadingState()
    }

    fun showPriceDataSuccess(tradeType: TradeType, price: Price) {
        viewHolders[tradeType]?.showPriceDataSuccess(price)
    }

    fun showPriceRangeLoadingState(tradeType: TradeType) {
        viewHolders[tradeType]?.showPriceRangeLoadingState()
    }

    fun showPriceRangeDataSuccess(tradeType: TradeType, priceRange: PriceRange) {
        viewHolders[tradeType]?.showPriceRangeDataSuccess(priceRange)
    }

    fun showChartLoadingState(tradeType: TradeType) {
        viewHolders[tradeType]?.showChartLoadingState()
    }

    fun showChartDataSuccess(tradeType: TradeType, dailyCandles: List<DailyCandle>) {
        viewHolders[tradeType]?.showChartDataSuccess(dailyCandles)
    }

    fun resetDataUIComponents(tradeType: TradeType) {
        viewHolders[tradeType]?.resetDataUIComponents()
    }

    inner class PriceHolder(
        private val binding: ItemPriceBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(tradeType: TradeType) {
            setListeners()
            resetDataUIComponents()
        }

        private fun setListeners() = with(binding) {
            priceView.setOnDollarTypeChanged { dollarType ->
                resetDataUIComponents()
                actionDollarType?.invoke(dollarType)
            }
        }

        fun setEnabledSwitchDollar(isEnabled: Boolean) = with(binding) {
            priceView.setEnabledSwitchDollar(isEnabled)
        }

        fun showPriceLoadingState() = with(binding) {
            priceView.showPriceLoadingState()
        }

        fun showPriceDataSuccess(price: Price) = with(binding) {
            priceView.showPriceDataSuccess(price)
        }

        fun showPriceRangeLoadingState() = with(binding)  {
            priceRangeView.showPriceRangeLoadingState()
        }

        fun showPriceRangeDataSuccess(priceRange: PriceRange) = with(binding) {
            priceRangeView.showPriceRangeDataSuccess(priceRange)
        }

        fun showChartLoadingState() = with(binding) {
            dailyCandleChartView.showChartLoadingState()
        }

        fun showChartDataSuccess(dailyCandles: List<DailyCandle>) = with(binding) {
            dailyCandleChartView.showChartDataSuccess(dailyCandles)
        }

        fun resetDataUIComponents() = with(binding) {
            priceView.resetDataUIComponents()
            priceRangeView.resetDataUIComponents()
            dailyCandleChartView.resetDataUIComponents()
        }
    }
}