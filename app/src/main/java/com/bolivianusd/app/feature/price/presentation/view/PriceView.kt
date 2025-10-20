package com.bolivianusd.app.feature.price.presentation.view

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isVisible
import com.bolivianusd.app.R
import com.bolivianusd.app.core.extensions.clearText
import com.bolivianusd.app.core.extensions.gone
import com.bolivianusd.app.core.extensions.invisible
import com.bolivianusd.app.core.extensions.visible
import com.bolivianusd.app.core.listeners.SimpleAnimationListener
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

    private val textSwitcherRunnable = object : Runnable {
        override fun run() {
            indexTextSwitcher = (indexTextSwitcher + 1) % descriptions.size
            binding.priceValue.descriptionTextSwitcher.setText(descriptions[indexTextSwitcher])
            descriptionHandler.postDelayed(this, DURATION_ANIMATION_DESCRIPTION)
        }
    }
    private val descriptionHandler = Handler(Looper.getMainLooper())
    private var indexTextSwitcher = 0
    private val descriptions = mutableListOf<String>()

    init {
        binding.priceValue.dollarTypeSwitch.setOnCheckedChangeListener { _, isChecked ->
            val dollarType = if (isChecked) DollarType.USD else DollarType.USDT
            onDollarTypeChanged?.invoke(dollarType)
        }
        setupRollingTextView()
        setupDescriptionTextSwitcher()
    }

    private fun setupRollingTextView() = with(binding.priceValue) {
        priceTextView.animationDuration = DURATION_ANIMATION_PRICE
        priceTextView.addCharOrder(CharOrder.Number)
        priceTextView.animationInterpolator = AccelerateDecelerateInterpolator()
    }

    private fun setupDescriptionTextSwitcher() = with(binding.priceValue) {
        descriptionTextSwitcher.setFactory {
            val textView = TextView(context)
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12f)
            textView.textAlignment = TEXT_ALIGNMENT_CENTER
            textView.typeface = ResourcesCompat.getFont(context, R.font.sfuidisplay_medium)
            textView.setTextColor(ContextCompat.getColor(context, R.color.white_alpha_65))
            textView
        }
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
            startTextSwitcherRotation(listOf(price.label, price.updatedAtFormat))
            assetView.visible()
            priceTextView.visible()
            dollarTypeSwitch.visible()
        }
    }

    fun startTextSwitcherRotation(newDescriptions: List<String>) = with(binding.priceValue) {
        descriptionHandler.removeCallbacks(textSwitcherRunnable)
        descriptions.clear()
        descriptions.addAll(newDescriptions)
        indexTextSwitcher = 0
        descriptionTextSwitcher.visible()
        descriptionTextSwitcher.setText(descriptions[indexTextSwitcher])
        descriptionHandler.postDelayed(textSwitcherRunnable, DURATION_ANIMATION_DESCRIPTION)
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
            descriptionTextSwitcher.invisible()
            descriptionHandler.removeCallbacksAndMessages(null)
        }
    }

    companion object {
        private const val DURATION_ANIMATION_PRICE = 600L
        private const val DURATION_ANIMATION_DESCRIPTION = 5000L
    }

}