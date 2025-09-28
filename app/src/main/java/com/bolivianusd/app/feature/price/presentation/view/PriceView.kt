package com.bolivianusd.app.feature.price.presentation.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import com.bolivianusd.app.R
import com.bolivianusd.app.core.extensions.clearText
import com.bolivianusd.app.core.extensions.gone
import com.bolivianusd.app.core.extensions.invisible
import com.bolivianusd.app.core.extensions.visible
import com.bolivianusd.app.core.listeners.SimpleAnimationListener
import com.bolivianusd.app.databinding.LayoutDailyCandleChartBinding
import com.bolivianusd.app.databinding.LayoutPriceBinding
import com.bolivianusd.app.feature.price.domain.model.Price
import com.bolivianusd.app.shared.domain.model.DollarType
import com.yy.mobile.rollingtextview.CharOrder

class PriceView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private val binding: LayoutPriceBinding by lazy {
        LayoutPriceBinding.inflate(LayoutInflater.from(context), this, true)
    }

    private var onDollarTypeChanged: ((DollarType) -> Unit)? = null

    init {
        binding.priceValue.dollarTypeSwitch.setOnCheckedChangeListener { _, isChecked ->
            val dollarType = if (isChecked) DollarType.USD else DollarType.USDT
            onDollarTypeChanged?.invoke(dollarType)
        }
        setupRollingTextView()
    }

    private fun setupRollingTextView() = with(binding.priceValue) {
        priceTextView.animationDuration = 600L
        priceTextView.addCharOrder(CharOrder.Number)
        priceTextView.animationInterpolator = AccelerateDecelerateInterpolator()
    }

    fun setOnDollarTypeChanged(onDollarTypeChanged: ((DollarType) -> Unit)) {
        this.onDollarTypeChanged = onDollarTypeChanged
    }

    fun showPriceLoadingState() = with(binding) {
        priceValue.root.gone()
        priceShimmer.root.visible()
        priceShimmer.shimmerLayout.startShimmer()
    }

    fun showPriceDataSuccess(price: Price) = with(binding) {
        if (priceValue.root.isVisible) {
            setPriceData(price)
            return@with
        }
        val fadeOut = AnimationUtils.loadAnimation(context, R.anim.anim_view_fade_out)
        fadeOut.setAnimationListener(object : SimpleAnimationListener() {
            override fun onAnimationEnd(animation: Animation?) {
                hidePriceLoading()
                setPriceData(price)
                val fadeIn =
                    AnimationUtils.loadAnimation(context, R.anim.anim_view_fade_in)
                priceValue.root.visible()
                priceValue.root.startAnimation(fadeIn)
            }
        })
        if (priceShimmer.root.isVisible) {
            priceShimmer.root.startAnimation(fadeOut)
        }
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
    }

    fun resetDataUIComponents() {
        with(binding.priceValue) {
            assetView.invisible()
            priceTextView.invisible()
            dollarTypeSwitch.invisible()
            assetTextView.clearText()
            fiatTextView.clearText()
            descriptionTextView.clearText()
        }
    }

}