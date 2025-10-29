package com.bolivianusd.app.feature.price.presentation.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import com.bolivianusd.app.core.extensions.clearText
import com.bolivianusd.app.core.extensions.gone
import com.bolivianusd.app.core.extensions.invisible
import com.bolivianusd.app.core.extensions.visible
import com.bolivianusd.app.core.util.ONE_F
import com.bolivianusd.app.core.util.ZERO_F
import com.bolivianusd.app.databinding.ViewPriceBinding
import com.bolivianusd.app.shared.domain.model.Price
import com.bolivianusd.app.shared.domain.model.DollarType
import com.yy.mobile.rollingtextview.CharOrder

class PriceView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private val binding: ViewPriceBinding by lazy {
        ViewPriceBinding.inflate(LayoutInflater.from(context), this, true)
    }
    private var onDollarTypeChanged: ((DollarType) -> Unit)? = null


    init {
        setupDollarTypeSwitch()
        setupRollingTextView()
    }

    private fun setupDollarTypeSwitch() = with(binding) {
        priceValue.dollarTypeSwitch.setOnCheckedChangeListener { _, isChecked ->
            val dollarType = if (isChecked) DollarType.USD else DollarType.USDT
            onDollarTypeChanged?.invoke(dollarType)
        }
    }

    private fun setupRollingTextView() = with(binding.priceValue) {
        priceTextView.animationDuration = DURATION_ANIMATION_PRICE
        priceTextView.addCharOrder(CharOrder.Number)
        priceTextView.animationInterpolator = AccelerateDecelerateInterpolator()
    }

    fun setOnDollarTypeChanged(onDollarTypeChanged: ((DollarType) -> Unit)) {
        this.onDollarTypeChanged = onDollarTypeChanged
    }

    fun showPriceLoadingState() = with(binding) {
        priceValue.root.apply {
            alpha = ZERO_F
            gone()
        }
        priceShimmer.root.apply {
            alpha = ONE_F
            visible()
        }
        priceShimmer.shimmerLayout.startShimmer()
    }

    fun showPriceDataSuccess(price: Price) = with(binding) {
        priceShimmer.root.animate().cancel()
        priceValue.root.animate().cancel()
        setPriceData(price)
        if (priceValue.root.isVisible) {
            return@with
        }
        priceShimmer.root.animate()
            .alpha(ZERO_F)
            .setDuration(DURATION_ANIMATION_FADE_IN_OUT)
            .withEndAction {
                hidePriceLoading()
            }
            .start()
        priceValue.root.apply {
            alpha = ZERO_F
            visible()
        }
        priceValue.root.animate()
            .alpha(ONE_F)
            .setDuration(DURATION_ANIMATION_FADE_IN_OUT)
            .start()
    }

    private fun setPriceData(price: Price) {
        with(binding.priceValue) {
            assetTextView.text = price.asset
            fiatTextView.text = price.fiat
            priceTextView.setText(price.priceLabel)
            descriptionTextView.text = price.label
            assetView.visible()
            priceTextView.visible()
            dollarTypeSwitch.visible()
        }
    }

    fun hidePriceLoading() = with(binding) {
        priceShimmer.shimmerLayout.stopShimmer()
        priceShimmer.root.gone()
        priceShimmer.root.alpha = ONE_F
    }

    fun resetDataUIComponents() {
        with(binding.priceValue) {
            alpha = ONE_F
            assetView.invisible()
            priceTextView.invisible()
            dollarTypeSwitch.invisible()
            assetTextView.clearText()
            fiatTextView.clearText()
            descriptionTextView.clearText()
        }
    }

    companion object {
        private const val DURATION_ANIMATION_PRICE = 600L
        private const val DURATION_ANIMATION_FADE_IN_OUT = 300L
    }

}