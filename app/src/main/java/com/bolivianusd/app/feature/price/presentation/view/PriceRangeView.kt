package com.bolivianusd.app.feature.price.presentation.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.bolivianusd.app.R
import com.bolivianusd.app.core.extensions.clearText
import com.bolivianusd.app.core.extensions.invisible
import com.bolivianusd.app.core.extensions.visible
import com.bolivianusd.app.core.util.ONE_F
import com.bolivianusd.app.core.util.ZERO_F
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
            rangeTitle.alpha = ONE_F
            rangeTitle.invisible()
            dotView.invisible()
            updateAtTextView.alpha = ONE_F
            updateAtTextView.invisible()
            rangeTitleShimmer.visible()
            rangeTitleShimmer.startShimmer()
            dateShimmerLayout.visible()
            dateShimmerLayout.startShimmer()
        }
    }

    fun showPriceRangeLoadingState() = with(binding) {
        rangeValue.apply {
            alpha = ZERO_F
            invisible()
        }
        rangeTitle.apply {
            alpha = ZERO_F
            invisible()
        }
        updateAtTextView.apply {
            alpha = ZERO_F
            invisible()
        }

        shimmerLayout.apply {
            alpha = ONE_F
            visible()
        }
        shimmerLayout.startShimmer()

        rangeTitleShimmer.apply {
            alpha = ONE_F
            visible()
        }
        rangeTitleShimmer.startShimmer()

        dateShimmerLayout.apply {
            alpha = ONE_F
            visible()
        }
        dateShimmerLayout.startShimmer()
    }

    fun showPriceRangeDataSuccess(priceRange: PriceRange) = with(binding) {
        rangeValue.animate().cancel()
        rangeTitle.animate().cancel()
        updateAtTextView.animate().cancel()
        shimmerLayout.animate().cancel()
        rangeTitleShimmer.animate().cancel()
        dateShimmerLayout.animate().cancel()

        setPriceRangeData(priceRange)

        shimmerLayout.animate()
            .alpha(ZERO_F)
            .setDuration(DURATION_ANIMATION_FADE_IN_OUT)
            .start()

        rangeTitleShimmer.animate()
            .alpha(ZERO_F)
            .setDuration(DURATION_ANIMATION_FADE_IN_OUT)
            .start()

        dateShimmerLayout.animate()
            .alpha(ZERO_F)
            .setDuration(DURATION_ANIMATION_FADE_IN_OUT)
            .withEndAction {
                hidePriceRangeLoading()
            }
            .start()

        rangeValue.apply {
            alpha = ZERO_F
            visible()
        }
        rangeValue.animate()
            .alpha(ONE_F)
            .setDuration(DURATION_ANIMATION_FADE_IN_OUT)
            .start()

        rangeTitle.apply {
            alpha = ZERO_F
            visible()
        }
        rangeTitle.animate()
            .alpha(ONE_F)
            .setDuration(DURATION_ANIMATION_FADE_IN_OUT)
            .start()

        updateAtTextView.apply {
            alpha = ZERO_F
            visible()
        }
        updateAtTextView.animate()
            .alpha(ONE_F)
            .setDuration(DURATION_ANIMATION_FADE_IN_OUT)
            .start()
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
        shimmerLayout.invisible()
        shimmerLayout.alpha = ONE_F
        rangeTitleShimmer.stopShimmer()
        rangeTitleShimmer.invisible()
        rangeTitleShimmer.alpha = ONE_F
        dateShimmerLayout.stopShimmer()
        dateShimmerLayout.invisible()
        dateShimmerLayout.alpha = ONE_F
    }

    companion object {
        private const val DURATION_ANIMATION_FADE_IN_OUT = 300L
    }
}