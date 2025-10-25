package com.bolivianusd.app.feature.price.presentation.view

import android.content.Context
import android.graphics.Paint
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import com.bolivianusd.app.R
import com.bolivianusd.app.core.extensions.getColorRes
import com.bolivianusd.app.core.extensions.gone
import com.bolivianusd.app.core.extensions.invisible
import com.bolivianusd.app.core.extensions.visible
import com.bolivianusd.app.core.listeners.SimpleAnimationListener
import com.bolivianusd.app.core.util.emptyString
import com.bolivianusd.app.databinding.ViewDailyCandleChartBinding
import com.bolivianusd.app.feature.price.domain.model.DailyCandle
import com.bolivianusd.app.feature.price.presentation.mapper.getDateRangeLabel
import com.bolivianusd.app.feature.price.presentation.mapper.getSource
import com.bolivianusd.app.feature.price.presentation.mapper.toCandleEntries
import com.bolivianusd.app.feature.price.presentation.mapper.toDataSetLabel
import com.bolivianusd.app.feature.price.presentation.mapper.toFiat
import com.bolivianusd.app.feature.price.presentation.mapper.toXAxisValues
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.CandleData
import com.github.mikephil.charting.data.CandleDataSet
import com.github.mikephil.charting.data.CandleEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

class DailyCandleChartView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private val binding: ViewDailyCandleChartBinding by lazy {
        ViewDailyCandleChartBinding.inflate(LayoutInflater.from(context), this, true)
    }

    init {
        setupCandleStickCharShimmer()
        setupCandleStickChart()
    }

    private fun setupCandleStickCharShimmer() = with(binding.chart) {
        val entries = listOf(
            CandleEntry(0f, 13.0f, 12.0f, 12.5f, 12.8f),
            CandleEntry(1f, 12.9f, 11.8f, 12.1f, 12.3f),
            CandleEntry(2f, 12.7f, 11.9f, 12.2f, 12.0f),
            CandleEntry(3f, 12.5f, 11.7f, 12.0f, 11.9f),
            CandleEntry(4f, 12.4f, 11.6f, 11.9f, 11.7f),
            CandleEntry(5f, 12.2f, 11.5f, 11.8f, 12.0f),
            CandleEntry(6f, 12.6f, 11.9f, 12.1f, 12.4f),
            CandleEntry(7f, 12.8f, 12.0f, 12.3f, 12.6f),
            CandleEntry(8f, 13.1f, 12.2f, 12.5f, 12.9f),
            CandleEntry(9f, 13.3f, 12.4f, 12.8f, 13.0f)
        )

        val candleDataSet = CandleDataSet(entries, "████████")
        candleDataSet.apply {
            barSpace = 0.3f
            shadowWidth = 1f
            shadowColorSameAsCandle = true
            decreasingColor = context.getColorRes(R.color.cool_grey)
            decreasingPaintStyle = Paint.Style.FILL
            increasingColor = context.getColorRes(R.color.cool_grey)
            increasingPaintStyle = Paint.Style.FILL
            neutralColor = context.getColorRes(R.color.cool_grey)
            setDrawValues(false)
        }

        with(binding.chart) {
            chartShimmer.apply {
                chartShimmer.setDrawBorders(false)
                chartShimmer.setExtraOffsets(0f, 0f, 0f, 0f)
                chartShimmer.setTouchEnabled(false)
                chartShimmer.setDragEnabled(false)
                chartShimmer.setScaleEnabled(false)
                chartShimmer.setPinchZoom(false)
                chartShimmer.isDoubleTapToZoomEnabled = false
                chartShimmer.isHighlightPerTapEnabled = false
            }
            chartShimmer.legend.apply {
                isEnabled = true
                textSize = 9f
                textColor = context.getColorRes(R.color.cool_grey)
                verticalAlignment = Legend.LegendVerticalAlignment.TOP
                horizontalAlignment = Legend.LegendHorizontalAlignment.LEFT
                orientation = Legend.LegendOrientation.HORIZONTAL
            }
            chartShimmer.description.apply {
                isEnabled = false
            }
            chartShimmer.axisLeft.apply {
                setDrawGridLines(false)
                isEnabled = false
            }
            chartShimmer.axisRight.apply {
                setDrawAxisLine(false)
                setDrawLabels(true)
                setDrawGridLines(true)
                textColor = context.getColorRes(R.color.cool_grey)
                textSize = 8f
                gridColor = context.getColorRes(R.color.cool_grey)
                gridLineWidth = 1f
                valueFormatter = object : ValueFormatter() {
                    override fun getFormattedValue(value: Float): String {
                        return "███████"
                    }
                }
            }
            chartShimmer.xAxis.apply {
                position = XAxis.XAxisPosition.BOTTOM
                setDrawLabels(true)
                setDrawGridLines(false)
                setDrawAxisLine(false)
                textSize = 8f
                textColor = context.getColorRes(R.color.cool_grey)
                labelCount = entries.size
                valueFormatter = object : ValueFormatter() {
                    override fun getFormattedValue(value: Float): String {
                        return "███"
                    }
                }
            }
            chartShimmer.data = CandleData(candleDataSet)
        }
    }

    private fun setupCandleStickChart() = with(binding.chart) {
        chart.apply {
            marker = CandleMarkerView(context, R.layout.layout_marker_candle)
            setDrawBorders(false)
            setExtraOffsets(0f, 0f, 0f, 0f)
            setTouchEnabled(true)
            setDragEnabled(false)
            setScaleEnabled(false)
            setPinchZoom(false)
            isDoubleTapToZoomEnabled = false
            isHighlightPerTapEnabled = true
        }
        chart.legend.apply {
            isEnabled = true
            textSize = 9f
            textColor = context.getColorRes(R.color.white_alpha_65)
            verticalAlignment = Legend.LegendVerticalAlignment.TOP
            horizontalAlignment = Legend.LegendHorizontalAlignment.LEFT
            orientation = Legend.LegendOrientation.HORIZONTAL
        }
        chart.description.apply {
            isEnabled = false
        }
        chart.axisLeft.apply {
            setDrawGridLines(false)
            isEnabled = false
        }
        chart.axisRight.apply {
            setDrawAxisLine(false)
            setDrawLabels(true)
            setDrawGridLines(true)
            textColor = context.getColorRes(R.color.white_alpha_65)
            textSize = 8f
            gridColor = context.getColorRes(R.color.white_alpha_05)
            gridLineWidth = 1f
        }
        chart.xAxis.apply {
            position = XAxis.XAxisPosition.BOTTOM
            setDrawLabels(true)
            setDrawGridLines(false)
            setDrawAxisLine(false)
            textSize = 8f
            textColor = context.getColorRes(R.color.white_alpha_65)
        }
        chart.setOnChartValueSelectedListener(object : OnChartValueSelectedListener {
            override fun onValueSelected(e: Entry?, h: Highlight?) {
            }

            override fun onNothingSelected() {
                hideMarker()
            }
        })
    }

    fun resetDataUIComponents() {
        with(binding) {
            chart.shimmerLayout.startShimmer()
            chart.shimmerLayout.visible()

            updateTimeShimmer.shimmerLayout.startShimmer()
            updateTimeShimmer.root.visible()

            updateTime.updateTextView.invisible()
            updateTime.dotTextView.invisible()
            updateTime.updatedTextView.invisible()

            chart.chart.invisible()
            chart.valueData.invisible()
            chart.labelTextView.invisible()
            hideMarker()
        }
    }

    private fun setChartData(dailyCandles: List<DailyCandle>) = with(binding.chart) {
        binding.updateTime.updatedTextView.text = dailyCandles.getDateRangeLabel()

        binding.updateTime.updateTextView.visible()
        binding.updateTime.dotTextView.visible()
        binding.updateTime.updatedTextView.visible()

        labelTextView.setText(dailyCandles.getSource())
        labelTextView.visible()

        val entries = dailyCandles.toCandleEntries()
        val xAxisValues = dailyCandles.toXAxisValues()
        val dataSetLabel = dailyCandles.toDataSetLabel()
        val fiat = dailyCandles.toFiat()

        val candleDataSet = CandleDataSet(entries, dataSetLabel)
        candleDataSet.apply {
            barSpace = 0.3f
            shadowWidth = 1f
            shadowColorSameAsCandle = true
            decreasingColor = context.getColorRes(R.color.red)
            decreasingPaintStyle = Paint.Style.FILL
            increasingColor = context.getColorRes(R.color.green)
            increasingPaintStyle = Paint.Style.FILL
            neutralColor = context.getColorRes(R.color.yellow)
            setDrawValues(false)
            setDrawHorizontalHighlightIndicator(false)
            setDrawVerticalHighlightIndicator(false)
        }
        chart.axisRight.apply {
            valueFormatter = object : ValueFormatter() {
                override fun getFormattedValue(value: Float): String {
                    val formatter = DecimalFormat("#,###.00", DecimalFormatSymbols(Locale.ENGLISH))
                    val amount = formatter.format(value)
                    return "$fiat $amount"
                }
            }
        }
        chart.xAxis.apply {
            labelCount = xAxisValues.size
            valueFormatter = object : ValueFormatter() {
                override fun getFormattedValue(value: Float): String {
                    val index = value.toInt()
                    return if (index >= 0 && index < xAxisValues.size) xAxisValues[index] else emptyString
                }
            }
        }
        chart.data = CandleData(candleDataSet)
        chart.invalidate()
        chart.visible()
    }

    fun showChartLoadingState() = with(binding) {
        updateTimeShimmer.shimmerLayout.startShimmer()
        updateTimeShimmer.root.visible()

        chart.shimmerLayout.startShimmer()
        chart.shimmerLayout.visible()
        chart.chart.invisible()
    }

    fun showChartDataSuccess(dailyCandles: List<DailyCandle>) = with(binding) {
        val fadeOut = AnimationUtils.loadAnimation(context, R.anim.anim_view_fade_out)
        fadeOut.setAnimationListener(object : SimpleAnimationListener() {
            override fun onAnimationEnd(animation: Animation?) {
                hideChartLoadingState()
                setChartData(dailyCandles)
                val fadeIn = AnimationUtils.loadAnimation(context, R.anim.anim_view_fade_in)
                chart.valueData.visible()
                chart.valueData.startAnimation(fadeIn)
                updateTime.root.visible()
                updateTime.root.startAnimation(fadeIn)
            }
        })

        if (chart.shimmerLayout.isVisible) {
            chart.shimmerLayout.startAnimation(fadeOut)
        }
        if (updateTimeShimmer.shimmerLayout.isVisible) {
            updateTimeShimmer.shimmerLayout.startAnimation(fadeOut)
        }
    }

    private fun hideChartLoadingState() = with(binding) {
        chart.shimmerLayout.stopShimmer()
        chart.shimmerLayout.gone()
        updateTimeShimmer.shimmerLayout.stopShimmer()
        updateTimeShimmer.root.gone()
        updateTime.updateTextView.invisible()
        updateTime.dotTextView.invisible()
        updateTime.updatedTextView.invisible()
        chart.chart.invisible()
        chart.valueData.invisible()
        chart.labelTextView.invisible()
    }

    private fun hideMarker() = with(binding.chart) {
        chart.highlightValue(null)
    }

}