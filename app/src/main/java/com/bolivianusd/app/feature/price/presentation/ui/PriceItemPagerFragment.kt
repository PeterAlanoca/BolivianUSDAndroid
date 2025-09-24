package com.bolivianusd.app.feature.price.presentation.ui

import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import com.bolivianusd.app.R
import com.bolivianusd.app.core.base.BaseFragment
import com.bolivianusd.app.core.extensions.collectFlow
import com.bolivianusd.app.core.extensions.collectFlows
import com.bolivianusd.app.core.extensions.getColorRes
import com.bolivianusd.app.core.extensions.getDrawableRes
import com.bolivianusd.app.core.extensions.gone
import com.bolivianusd.app.core.extensions.serializable
import com.bolivianusd.app.core.extensions.visible
import com.bolivianusd.app.core.formats.AmountValueFormatter
import com.bolivianusd.app.core.listeners.SimpleAnimationListener
import com.bolivianusd.app.core.util.ELEVEN_F
import com.bolivianusd.app.core.util.FOUR_F
import com.bolivianusd.app.core.util.NEGATIVE_SIX_F
import com.bolivianusd.app.core.util.ONE
import com.bolivianusd.app.core.util.SIX_F
import com.bolivianusd.app.core.util.TEN_F
import com.bolivianusd.app.core.util.THREE_F
import com.bolivianusd.app.core.util.ZERO
import com.bolivianusd.app.core.util.ZERO_F
import com.bolivianusd.app.core.util.emptyBar
import com.bolivianusd.app.core.util.emptyString
import com.bolivianusd.app.databinding.FragmentPriceItemPagerBinding
import com.bolivianusd.app.feature.price.domain.model.old.model.ChartData
import com.bolivianusd.app.feature.price.domain.model.Price
import com.bolivianusd.app.feature.price.domain.model.old.model.RangePrice
import com.bolivianusd.app.feature.price.domain.model.old.enum.OperationType
import com.bolivianusd.app.shared.data.state.State
import com.bolivianusd.app.feature.price.presentation.viewmodel.PriceViewModel
import com.bolivianusd.app.shared.domain.state.UiState
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.CandleData
import com.github.mikephil.charting.data.CandleDataSet
import com.github.mikephil.charting.data.CandleEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.yy.mobile.rollingtextview.CharOrder

class PriceItemPagerFragment : BaseFragment<FragmentPriceItemPagerBinding>() {

    private val viewModel: PriceViewModel by activityViewModels()

    private val operationType: OperationType by lazy {
        requireNotNull(arguments?.serializable<OperationType>(OPERATION_TYPE))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isNotRecreate = false
    }

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentPriceItemPagerBinding.inflate(inflater, container, false)

    override fun initViews() {
        setupObservers()


        setupRollingTextView()
        setupLineChartShimmer()
        setupLineChart()
    }

    override fun initData() {
        Handler(Looper.getMainLooper()).postDelayed({
            getPrice()
            getChartPrice()
            getRangePrice()
        }, 200)
    }

    private fun setupObservers() {
        collectFlow(viewModel.priceState) { state ->
            when (state) {
                is UiState.Loading -> println("naty Loading...")
                is UiState.Success -> println("naty Success: ${state.data}")
                is UiState.Error -> println("naty Error: ${state.throwable}")
            }
        }

        // O para múltiples flows:
        collectFlows {
            /*launch {
                viewModel.priceState.collect { state ->
                    // manejar estado de precio
                }
            }
            launch {
                viewModel.userState.collect { user ->
                    // manejar estado de usuario
                }
            }*/
        }
    }


    private fun getPrice() {
        viewModel.setPriceType("buy")


        viewModel.getPriceBuy(operationType).observe(viewLifecycleOwner) { state ->
            when (state) {
                is State.Loading -> showPriceShimmer()
                is State.Success -> setDataPriceValue(state.data)
                is State.Error -> Unit
            }
        }
    }

    private fun setDataPriceValue(price: Price) = with(binding.priceValue) {
        with(price) {
            when (operationType) {
                OperationType.BUY -> {
                    exchangeRateCurrencyTextView.text = origin.currency
                    exchangeRateAmountTextView.setText(origin.amountLabel)
                    currencyTextView.text = destination.currency
                }

                OperationType.SELL -> {
                    exchangeRateCurrencyTextView.text = destination.currency
                    exchangeRateAmountTextView.setText(destination.amountLabel)
                    currencyTextView.text = origin.currency
                }
            }
            descriptionTextView.text = price.label
        }
        animateShowPriceValue()
    }

    private fun showPriceShimmer() = with(binding) {
        priceShimmer.shimmerLayout.startShimmer()
        priceShimmer.root.visible()
    }

    private fun animateShowPriceValue() = with(binding) {
        if (priceValue.root.isVisible) return@with
        val fadeIn = AnimationUtils.loadAnimation(requireContext(), R.anim.anim_view_fade_in)
        priceValue.root.visible()
        priceValue.root.startAnimation(fadeIn)
        val fadeOut = AnimationUtils.loadAnimation(requireContext(), R.anim.anim_view_fade_out)
        fadeOut.setAnimationListener(object : SimpleAnimationListener() {
            override fun onAnimationEnd(animation: Animation?) {
                hidePriceShimmer()
            }
        })
        priceShimmer.root.startAnimation(fadeOut)
    }

    private fun hidePriceShimmer() = with(binding) {
        priceShimmer.shimmerLayout.stopShimmer()
        priceShimmer.root.gone()
    }

    private fun getChartPrice() {
        viewModel.getChartPrice(operationType).observe(viewLifecycleOwner) { state ->
            when (state) {
                is State.Loading -> {
                    showUpdateTimeShimmer()
                    showChartShimmer()
                }
                is State.Success -> setDataChartPrice(state.data)
                is State.Error -> Unit
            }
        }
    }

    private fun setDataChartPrice(chartPrice: ChartData) = with(binding) {
        with(updateTime) {
            updatedTextView.text = chartPrice.updated
            animateShowUpdateTimeView()
        }
        with(chart) {
            setChartData(chartPrice)
            animateShowChartView()
        }
    }

    private fun showUpdateTimeShimmer() = with(binding) {
        updateTimeShimmer.shimmerLayout.startShimmer()
        updateTimeShimmer.root.visible()
    }

    private fun animateShowUpdateTimeView() = with(binding) {
        if (updateTime.root.isVisible) return@with
        val fadeIn = AnimationUtils.loadAnimation(requireContext(), R.anim.anim_view_fade_in)
        updateTime.root.visible()
        updateTime.root.startAnimation(fadeIn)
        val fadeOut = AnimationUtils.loadAnimation(requireContext(), R.anim.anim_view_fade_out)
        fadeOut.setAnimationListener(object : SimpleAnimationListener() {
            override fun onAnimationEnd(animation: Animation?) {
                hideUpdateTimeShimmer()
            }
        })
        updateTimeShimmer.root.startAnimation(fadeOut)
    }

    private fun hideUpdateTimeShimmer() = with(binding) {
        updateTimeShimmer.shimmerLayout.stopShimmer()
        updateTimeShimmer.root.gone()
    }

    private fun setChartData(chartData: ChartData) = with(binding.chart) {
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
        val set = CandleDataSet(entries, "USDT - BOB")

        set.barSpace = 0.3f   // 🔥 velas delgadas
        set.shadowWidth = 1f
        set.shadowColorSameAsCandle = true // <-- hace que la sombra tenga el mismo color que la vela
        set.decreasingColor = requireContext().getColorRes(R.color.red)
        set.decreasingPaintStyle = Paint.Style.FILL
        set.increasingColor = requireContext().getColorRes(R.color.green)
        set.increasingPaintStyle = Paint.Style.FILL
        set.neutralColor = Color.BLUE
        set.setDrawValues(false)


        val data = CandleData(set)

        chart.description.isEnabled = false
        chart.description = null


        chart.setDrawBorders(false)       // quitar borde del gráfico

        // Eje Y izquierdo
        chart.axisLeft.setDrawGridLines(false)
        chart.axisLeft.isEnabled = false  // opcional, si quieres quitar eje izquierdo

        // Eje Y derecho
        chart.axisRight.setDrawAxisLine(false) // oculta la línea del eje Y derecho
        chart.axisRight.setDrawLabels(true)    // mantiene los valores visibles
        chart.axisRight.setDrawGridLines(true)
        chart.axisRight.textColor = requireContext().getColorRes(R.color.white_alpha_65)
        chart.axisRight.textSize = 8f
        chart.axisRight.setDrawGridLines(true)
        chart.axisRight.gridColor = requireContext().getColorRes(R.color.white_alpha_05)
        chart.axisRight.gridLineWidth = 1f         // 🔹 grosor de línea (opcional)
        chart.axisRight.valueFormatter = object : ValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                return "BOB " + String.format("%.2f", value)
            }
        }

        // Eje X derecho
        val fechas = listOf(
            "20/09",
            "21/09",
            "22/09",
            "23/09",
            "24/09",
            "25/09",
            "26/09",
            "27/09",
            "28/09",
            "29/09"
        )
        chart.xAxis.position = XAxis.XAxisPosition.BOTTOM
        chart.xAxis.setDrawLabels(true)  // mostrar etiquetas
        chart.xAxis.setDrawGridLines(false) // quitar líneas de cuadrícula
        chart.xAxis.setDrawAxisLine(false) // oculta la línea del eje X derecho
        chart.xAxis.labelCount = fechas.size
        chart.xAxis.textSize = 8f
        chart.xAxis.textColor = requireContext().getColorRes(R.color.white_alpha_65)
        chart.xAxis.valueFormatter = object : ValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                val index = value.toInt()
                return if (index >= 0 && index < fechas.size) fechas[index] else ""
            }
        }


        // Establecer mínimo y máximo
        //chart.axisRight.axisMinimum = 2f   // mínimo del eje Y
        //chart.axisRight.axisMaximum = 24f   // máximo del eje Y

        chart.data = data

        chart.legend.isEnabled = true
        chart.legend.textSize = 9f
        chart.legend.textColor = requireContext().getColorRes(R.color.white_alpha_65)
        chart.legend.verticalAlignment = Legend.LegendVerticalAlignment.TOP
        chart.legend.horizontalAlignment = Legend.LegendHorizontalAlignment.LEFT
        chart.legend.orientation = Legend.LegendOrientation.HORIZONTAL

        chart.setExtraOffsets(0f, 0f, 0f, 0f)

        chart.setTouchEnabled(false)       // ❌ desactiva todos los eventos táctiles
        chart.setDragEnabled(false)        // ❌ desactiva el drag (mover con dedo)
        chart.setScaleEnabled(false)       // ❌ desactiva pinch zoom
        chart.setPinchZoom(false)          // ❌ desactiva pinch-to-zoom combinado
        chart.isDoubleTapToZoomEnabled = false // ❌ desactiva zoom con doble tap
        chart.isHighlightPerTapEnabled = false   // ❌ evita que seleccione una vela al tocar


        chart.invalidate()

        /*val labels = chartData.labels
        val values = chartData.values
        val colors = chartData.colors
        val label = chartData.label

        descriptionTextView.text = chartData.description
        variationTextView.text = chartData.variation
        variationTextView.setTextColor(chartData.variationColor)
        priceTextView.text = chartData.price
        labelTextView.text = label

        val dataSet = LineDataSet(values, label)
        dataSet.setDrawIcons(true)
        dataSet.setDrawValues(true)
        dataSet.valueFormatter = AmountValueFormatter()
        dataSet.valueTextSize = ELEVEN_F
        dataSet.setValueTextColors(colors)
        dataSet.enableDashedLine(TEN_F, ZERO_F, ZERO_F)
        dataSet.color = requireContext().getColorRes(R.color.maroon_flush)
        dataSet.setCircleColor(requireContext().getColorRes(R.color.maroon_flush))
        dataSet.lineWidth = THREE_F
        dataSet.circleRadius = FOUR_F
        dataSet.setDrawCircleHole(false)
        dataSet.setDrawFilled(true)
        dataSet.mode = LineDataSet.Mode.HORIZONTAL_BEZIER
        dataSet.fillDrawable = requireContext().getDrawableRes(R.drawable.gradient_chart)
        val dataSets = ArrayList<ILineDataSet>()
        dataSets.add(dataSet)

        lineChart.apply {
            xAxis.setLabelCount(labels.size, true)
            xAxis.valueFormatter = IndexAxisValueFormatter(labels)
            axisLeft.setAxisMaximum(chartData.axisMaximum)
            axisLeft.setAxisMinimum(chartData.axisMinimum)
            clear()
            setData(LineData(dataSets))
            invalidate()
        }*/
    }

    private fun showChartShimmer() = with(binding) {
        chartShimmer.shimmerLayout.startShimmer()
        chartShimmer.root.visible()
    }

    private fun animateShowChartView() = with(binding) {
        if (chart.root.isVisible) return@with
        val fadeIn = AnimationUtils.loadAnimation(requireContext(), R.anim.anim_view_fade_in)
        chart.root.visible()
        chart.root.startAnimation(fadeIn)
        val fadeOut = AnimationUtils.loadAnimation(requireContext(), R.anim.anim_view_fade_out)
        fadeOut.setAnimationListener(object : SimpleAnimationListener() {
            override fun onAnimationEnd(animation: Animation?) {
                hideChartShimmer()
            }
        })
        chartShimmer.root.startAnimation(fadeOut)
    }

    private fun hideChartShimmer() = with(binding) {
        chartShimmer.shimmerLayout.stopShimmer()
        chartShimmer.root.gone()
    }

    private fun getRangePrice() {
        viewModel.getRangePrice(operationType).observe(viewLifecycleOwner) { state ->
            when (state) {
                is State.Loading -> showRangeShimmer()
                is State.Success -> setRangePrice(state.data)
                is State.Error -> Unit
            }
        }
    }

    private fun setRangePrice(rangePrice: RangePrice) = with(binding.range) {
        currencyTextView.text = rangePrice.currency
        with(rangePrice.min) {
            minTextView.text = this.amount
            minLabelTextView.text = this.label
        }
        with(rangePrice.avg) {
            avgTextView.text = this.amount
            avgLabelTextView.text = this.label
        }
        with(rangePrice.max) {
            maxTextView.text = this.amount
            maxLabelTextView.text = this.label
        }
        animateShowRangeView()
    }

    private fun showRangeShimmer() = with(binding) {
        rangeShimmer.shimmerLayout.startShimmer()
        rangeShimmer.root.visible()
    }

    private fun animateShowRangeView() = with(binding) {
        if (range.root.isVisible) return@with
        val fadeIn = AnimationUtils.loadAnimation(requireContext(), R.anim.anim_view_fade_in)
        range.root.visible()
        range.root.startAnimation(fadeIn)
        val fadeOut = AnimationUtils.loadAnimation(requireContext(), R.anim.anim_view_fade_out)
        fadeOut.setAnimationListener(object : SimpleAnimationListener() {
            override fun onAnimationEnd(animation: Animation?) {
                hideRangeShimmer()
            }
        })
        rangeShimmer.root.startAnimation(fadeOut)
    }

    private fun hideRangeShimmer() = with(binding) {
        rangeShimmer.shimmerLayout.stopShimmer()
        rangeShimmer.root.gone()
    }

    private fun setupRollingTextView() = with(binding.priceValue) {
        exchangeRateAmountTextView.animationDuration = 600L
        exchangeRateAmountTextView.addCharOrder(CharOrder.Number)
        exchangeRateAmountTextView.animationInterpolator = AccelerateDecelerateInterpolator()
    }

    private fun setupLineChartShimmer() = with(binding.chartShimmer) {
        val labels = List(CHART_DATA_ITEMS_SIZE) { index ->
            if (index == ZERO || index == CHART_DATA_ITEMS_SIZE - ONE) emptyString else emptyBar
        }
        val colors = List(CHART_DATA_ITEMS_SIZE) {
            requireContext().getColorRes(R.color.cool_grey)
        }
        val values = List(CHART_DATA_ITEMS_SIZE) { index -> Entry(index.toFloat(), ZERO_F) }

        lineChart.apply {
            setTouchEnabled(false)
            setDrawGridBackground(false)
            setDragEnabled(true)
            setScaleEnabled(true)
            setPinchZoom(false)
            xAxis.setDrawAxisLine(false)
            xAxis.setDrawGridLines(false)
            xAxis.setDrawLabels(true)
            xAxis.setLabelCount(labels.size, true)
            xAxis.textColor = requireContext().getColorRes(R.color.cool_grey)
            xAxis.position = XAxis.XAxisPosition.BOTTOM
            xAxis.valueFormatter = IndexAxisValueFormatter(labels)
            axisRight.setDrawGridLines(false)
            axisRight.setDrawLabels(false)
            axisLeft.isEnabled = false
            axisLeft.setDrawGridLines(false)
            axisLeft.setDrawLabels(false)
            axisLeft.setAxisMaximum(SIX_F)
            axisLeft.setAxisMinimum(NEGATIVE_SIX_F)
        }
        val dataSet = LineDataSet(values, emptyString)
        dataSet.apply {
            valueFormatter = AmountValueFormatter()
            color = requireContext().getColorRes(R.color.cool_grey)
            valueTextSize = ELEVEN_F
            lineWidth = THREE_F
            dataSet.circleRadius = FOUR_F
            mode = LineDataSet.Mode.HORIZONTAL_BEZIER
            fillDrawable = requireContext().getDrawableRes(R.drawable.gradient_chart_shimmer)
            setDrawIcons(true)
            setDrawValues(true)
            setValueTextColors(colors)
            enableDashedLine(TEN_F, ZERO_F, ZERO_F)
            setCircleColor(requireContext().getColorRes(R.color.cool_grey))
            setDrawCircleHole(false)
            setDrawFilled(true)
        }
        val dataSets = ArrayList<ILineDataSet>()
        dataSets.add(dataSet)
        lineChart.setData(LineData(dataSets))
    }

    private fun setupLineChart() = with(binding.chart) {
        /*lineChart.apply {
            description.isEnabled = false
            legend.isEnabled = false
            extraBottomOffset = SEVEN_F
            extraTopOffset = TEN_F
            minOffset = ZERO_F
            setTouchEnabled(false)
            setDrawGridBackground(false)
            setDragEnabled(true)
            setScaleEnabled(true)
            setPinchZoom(false)
            xAxis.setDrawAxisLine(false)
            xAxis.setDrawGridLines(false)
            xAxis.setDrawLabels(true)
            xAxis.textColor = requireContext().getColorRes(R.color.maroon_flush)
            xAxis.textSize = TEN_F
            xAxis.position = XAxis.XAxisPosition.BOTTOM
            axisRight.setDrawGridLines(false)
            axisRight.setDrawLabels(false)
            axisRight.isEnabled = false
            axisLeft.isEnabled = false
            axisLeft.setDrawGridLines(false)
            axisLeft.setDrawLabels(false)
        }*/
    }

    companion object {
        private const val OPERATION_TYPE = "OPERATION_TYPE"
        private const val CHART_DATA_ITEMS_SIZE = 9

        fun newInstance(operationType: OperationType) = PriceItemPagerFragment().apply {
            val bundle = Bundle().apply {
                putSerializable(OPERATION_TYPE, operationType)
            }
            arguments = bundle
        }
    }

}

