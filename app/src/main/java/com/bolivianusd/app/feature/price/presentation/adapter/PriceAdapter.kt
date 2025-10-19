package com.bolivianusd.app.feature.price.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bolivianusd.app.databinding.ItemPriceBinding
import com.bolivianusd.app.feature.price.domain.model.DailyCandle
import com.bolivianusd.app.shared.domain.model.Price
import com.bolivianusd.app.shared.domain.model.PriceRange
import com.bolivianusd.app.shared.domain.model.TradeType

class PriceAdapter : RecyclerView.Adapter<PriceAdapter.PriceHolder>() {

    private val items = listOf(TradeType.BUY, TradeType.SELL)
    private val viewHolders = mutableMapOf<TradeType, PriceHolder>()

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

    fun showPriceLoadingState(targetTradeType: TradeType) {
        viewHolders[targetTradeType]?.showPriceLoadingState()
    }

    fun showPriceDataSuccess(targetTradeType: TradeType, price: Price) {
        viewHolders[targetTradeType]?.showPriceDataSuccess(price)
    }

    fun showPriceRangeLoadingState(targetTradeType: TradeType) {
        println("naty showPriceRangeLoadingState11 $targetTradeType")

        viewHolders[targetTradeType]?.showPriceRangeLoadingState()
    }

    fun showPriceRangeDataSuccess(targetTradeType: TradeType, priceRange: PriceRange) {
        viewHolders[targetTradeType]?.showPriceRangeDataSuccess(priceRange)
    }

    fun showChartLoadingState(targetTradeType: TradeType) {
        viewHolders[targetTradeType]?.showChartLoadingState()
    }

    fun showChartDataSuccess(targetTradeType: TradeType, dailyCandles: List<DailyCandle>) {
        viewHolders[targetTradeType]?.showChartDataSuccess(dailyCandles)
    }

    fun resetDataUIComponents(targetTradeType: TradeType) {
        viewHolders[targetTradeType]?.resetDataUIComponents()
    }

    inner class PriceHolder(
        private val binding: ItemPriceBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(tradeType: TradeType) {
            setupViewForTradeType(tradeType)
            showPriceLoadingState()
            showPriceRangeLoadingState()
            showChartLoadingState()
        }

        private fun setupViewForTradeType(tradeType: TradeType) {
            when (tradeType) {
                TradeType.BUY -> {
                    //binding.root.setBackgroundColor(Color.GREEN)
                    // Configurar vista de compra
                }
                TradeType.SELL -> {
                    //binding.root.setBackgroundColor(Color.RED)
                    // Configurar vista de venta
                }
            }
        }

        // Métodos que llaman a las vistas específicas
        fun showPriceLoadingState() = with(binding) {
            priceView.showPriceLoadingState()
        }

        fun showPriceDataSuccess(price: Price) {
            binding.priceView.showPriceDataSuccess(price)
        }

        fun showPriceRangeLoadingState() = with(binding)  {
            println("naty showPriceRangeLoadingState")
            priceRangeView.showPriceRangeLoadingState()
        }

        fun showPriceRangeDataSuccess(priceRange: PriceRange) {
            binding.priceRangeView.showPriceRangeDataSuccess(priceRange)
        }

        fun showChartLoadingState() {
            binding.dailyCandleChartView.showChartLoadingState()
        }

        fun showChartDataSuccess(dailyCandles: List<DailyCandle>) {
            binding.dailyCandleChartView.showChartDataSuccess(dailyCandles)
        }

        fun resetDataUIComponents() {
            binding.priceView.resetDataUIComponents()
            binding.priceRangeView.resetDataUIComponents()
            binding.dailyCandleChartView.resetDataUIComponents()
        }
    }
}