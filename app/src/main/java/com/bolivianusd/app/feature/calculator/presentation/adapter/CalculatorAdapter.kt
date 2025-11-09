package com.bolivianusd.app.feature.calculator.presentation.adapter

import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bolivianusd.app.R
import com.bolivianusd.app.core.extensions.getColorRes
import com.bolivianusd.app.databinding.ItemCalculatorBinding
import com.bolivianusd.app.shared.domain.model.DollarType
import com.bolivianusd.app.shared.domain.model.PriceRange
import com.bolivianusd.app.shared.domain.model.TradeType

class CalculatorAdapter : RecyclerView.Adapter<CalculatorAdapter.CalculatorHolder>() {

    private val items = listOf(TradeType.BUY, TradeType.SELL)
    private val viewHolders = mutableMapOf<TradeType, CalculatorHolder>()
    private var actionRefresh: (() -> Unit)? = null
    private var actionDollarType: ((DollarType) -> Unit)? = null
    private var onFormatError: (() -> Unit)? = null
    private var onPriceRangeError: ((String) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalculatorHolder {
        val binding = ItemCalculatorBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return CalculatorHolder(binding)
    }

    override fun onBindViewHolder(holder: CalculatorHolder, position: Int) {
        val tradeType = items[position]
        holder.bind(tradeType)
        viewHolders[tradeType] = holder
    }

    override fun getItemCount(): Int = items.size

    fun setOnRefresh(actionRefresh: () -> Unit) {
        this.actionRefresh = actionRefresh
    }

    fun setOnDollarTypeChanged(actionDollarType: (DollarType) -> Unit) {
        this.actionDollarType = actionDollarType
    }

    fun setEnabledSwitchDollar(isEnabled: Boolean) {
        viewHolders.forEach {
            it.value.setEnabledSwitchDollar(isEnabled)
        }
    }

    fun showPriceRangeLoadingState(tradeType: TradeType) {
        viewHolders[tradeType]?.showPriceRangeLoadingState()
    }

    fun showPriceRangeDataSuccess(tradeType: TradeType, priceRange: PriceRange) {
        viewHolders[tradeType]?.showPriceRangeDataSuccess(priceRange)
    }

    fun setOnFormatError(onFormatError: () -> Unit) {
        this.onFormatError = onFormatError
    }

    fun setOnPriceRangeError(onPriceRangeError: (String) -> Unit) {
        this.onPriceRangeError = onPriceRangeError
    }

    fun resetUIComponents(tradeType: TradeType) {
        viewHolders[tradeType]?.resetUIComponents()
    }

    inner class CalculatorHolder(
        private val binding: ItemCalculatorBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(tradeType: TradeType) {
            setListeners()
            setupSwipeRefresh()
            resetUIComponents()
        }

        private fun setListeners() = with(binding) {
            displayView.setOnDollarTypeChanged { dollarType ->
                resetUIComponents()
                actionDollarType?.invoke(dollarType)
            }
            swipeRefreshLayout.setOnRefreshListener {
                actionRefresh?.invoke()
                Handler(Looper.getMainLooper()).postDelayed({
                    swipeRefreshLayout.isRefreshing = false
                }, DELAY_SWIPE_REFRESH)
            }
            displayView.setOnFormatError {
                onFormatError?.invoke()
            }
            displayView.setOnPriceRangeError {
                onPriceRangeError?.invoke(it)
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

        private fun setupSwipeRefresh() = with(binding) {
            swipeRefreshLayout.apply {
                setColorSchemeResources(
                    R.color.java
                )
                setProgressBackgroundColorSchemeColor(context.getColorRes(R.color.rhino))
            }
        }

        fun setEnabledSwitchDollar(isEnabled: Boolean) = with(binding) {
            displayView.setEnabledSwitchDollar(isEnabled)
        }

        fun showPriceRangeLoadingState() = with(binding)  {
            displayView.showPriceRangeLoadingState()
            keyboardView.showPriceRangeLoadingState()
        }

        fun showPriceRangeDataSuccess(priceRange: PriceRange) = with(binding) {
            displayView.showPriceRangeDataSuccess(priceRange)
            keyboardView.showContentView()
        }

        fun resetUIComponents() = with(binding) {
            displayView.resetUIComponents()
            keyboardView.resetUIComponents()
        }
    }

    companion object {
        private const val DELAY_SWIPE_REFRESH = 400L
    }
}
