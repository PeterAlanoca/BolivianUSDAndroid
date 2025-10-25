package com.bolivianusd.app.feature.price.presentation.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
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
import com.bolivianusd.app.databinding.ViewPriceRangeBinding
import com.bolivianusd.app.shared.domain.model.PriceRange

class PriceRangeView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private val binding: ViewPriceRangeBinding by lazy {
        ViewPriceRangeBinding.inflate(LayoutInflater.from(context), this, true)
    }

    init {
        resetDataUIComponents()
        showPriceRangeLoadingState()
    }

    fun resetDataUIComponents() {
        with(binding) {
            minTextView.clearText()
            minLabelTextView.clearText()
            medianTextView.clearText()
            medianLabelTextView.clearText()
            maxTextView.clearText()
            maxLabelTextView.clearText()
            rangeLabel.clearText()
            currencyTextView.clearText()
            updateAtTextView.clearText()
            dotView.invisible()
            rangeTitle.invisible()
            updateAtTextView.invisible()
            rangeTitleShimmer.visible()
            rangeTitleShimmer.startShimmer()
            dateShimmerLayout.visible()
            dateShimmerLayout.startShimmer()
        }
    }

    fun showPriceRangeLoadingState() = with(binding) {
        rangeValue.invisible()
        shimmerLayout.visible()
        shimmerLayout.startShimmer()
        rangeTitle.invisible()
        rangeTitleShimmer.visible()
        rangeTitleShimmer.startShimmer()
    }

    fun showPriceRangeDataSuccess(priceRange: PriceRange) = with(binding) {
        if (rangeValue.isVisible) {
            setPriceRangeData(priceRange)
            return@with
        }
        val fadeOut = AnimationUtils.loadAnimation(context, R.anim.anim_view_fade_out)
        fadeOut.setAnimationListener(object : SimpleAnimationListener() {
            override fun onAnimationEnd(animation: Animation?) {
                hidePriceRangeLoading()
                setPriceRangeData(priceRange)
                val fadeIn =
                    AnimationUtils.loadAnimation(context, R.anim.anim_view_fade_in)
                rangeValue.visible()
                rangeValue.startAnimation(fadeIn)

                rangeTitle.visible()
                rangeTitle.startAnimation(fadeIn)

                updateAtTextView.visible()
                updateAtTextView.startAnimation(fadeIn)
            }
        })

        if (shimmerLayout.isVisible) {
            shimmerLayout.startAnimation(fadeOut)
        }
        if (rangeTitleShimmer.isVisible) {
            rangeTitleShimmer.startAnimation(fadeOut)
        }
        if (dateShimmerLayout.isVisible) {
            dateShimmerLayout.startAnimation(fadeOut)
        }
    }

    private fun setPriceRangeData(priceRange: PriceRange) = with(binding) {
        currencyTextView.text = priceRange.fiat
        with(priceRange.min) {
            minTextView.text = this.valueLabel
            minLabelTextView.text = this.description
        }
        with(priceRange.median) {
            medianTextView.text = this.valueLabel
            medianLabelTextView.text = this.description
        }
        with(priceRange.max) {
            maxTextView.text = this.valueLabel
            maxLabelTextView.text = this.description
        }
        rangeLabel.text = context.getString(R.string.price_view_item_range)
        updateAtTextView.text = priceRange.updatedAtFormat
        dotView.visible()
    }

    private fun hidePriceRangeLoading() = with(binding) {
        shimmerLayout.stopShimmer()
        shimmerLayout.gone()
        rangeTitleShimmer.stopShimmer()
        rangeTitleShimmer.gone()
        dateShimmerLayout.stopShimmer()
        dateShimmerLayout.gone()
    }

}