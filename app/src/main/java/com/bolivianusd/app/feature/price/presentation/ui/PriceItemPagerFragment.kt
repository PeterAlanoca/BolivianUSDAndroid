package com.bolivianusd.app.feature.price.presentation.ui

import android.graphics.Paint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import com.bolivianusd.app.R
import com.bolivianusd.app.core.base.BaseFragment
import com.bolivianusd.app.core.extensions.clearText
import com.bolivianusd.app.core.extensions.collectFlow
import com.bolivianusd.app.core.extensions.getColorRes
import com.bolivianusd.app.core.extensions.gone
import com.bolivianusd.app.core.extensions.invisible
import com.bolivianusd.app.core.extensions.serializable
import com.bolivianusd.app.core.extensions.visible
import com.bolivianusd.app.core.listeners.SimpleAnimationListener
import com.bolivianusd.app.core.util.emptyString
import com.bolivianusd.app.databinding.FragmentPriceItemPagerBinding
import com.bolivianusd.app.feature.price.domain.model.DailyCandle
import com.bolivianusd.app.feature.price.domain.model.Price
import com.bolivianusd.app.feature.price.domain.model.PriceRange
import com.bolivianusd.app.feature.price.presentation.mapper.getDateRangeLabel
import com.bolivianusd.app.feature.price.presentation.mapper.toCandleEntries
import com.bolivianusd.app.feature.price.presentation.mapper.toDataSetLabel
import com.bolivianusd.app.feature.price.presentation.mapper.toFiat
import com.bolivianusd.app.feature.price.presentation.mapper.toXAxisValues
import com.bolivianusd.app.feature.price.presentation.viewmodel.PriceViewModel
import com.bolivianusd.app.shared.domain.model.DollarType
import com.bolivianusd.app.shared.domain.model.TradeType
import com.bolivianusd.app.shared.domain.state.UiState
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.CandleData
import com.github.mikephil.charting.data.CandleDataSet
import com.github.mikephil.charting.data.CandleEntry
import com.github.mikephil.charting.formatter.ValueFormatter
import com.google.gson.Gson
import com.yy.mobile.rollingtextview.CharOrder

class PriceItemPagerFragment : BaseFragment<FragmentPriceItemPagerBinding>() {

    private val viewModel: PriceViewModel by activityViewModels()

    private val tradeType: TradeType by lazy {
        requireNotNull(arguments?.serializable<TradeType>(TRADER_TYPE))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isNotRecreate = false
    }

    override fun onStart() {
        super.onStart()
        // Iniciar la observación cuando el fragment se hace visible
        viewModel.observePriceAndCandles(tradeType)
    }

    override fun onStop() {
        super.onStop()
        // Opcional: limpiar cuando el fragment no es visible
        // viewModel.clearTradeType(tradeType)
    }

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentPriceItemPagerBinding.inflate(inflater, container, false)

    override fun initViews() {
        setupRollingTextView()
        setupCandleStickChart()
        setupCandleStickCharShimmer()
        resetDataUIComponents()
    }

    override fun setListeners() = with(binding) {
        priceValue.dollarTypeSwitch.setOnCheckedChangeListener { _, isChecked ->
            val dollarType = if (isChecked) DollarType.USD else DollarType.USDT
            resetDataUIComponents()
            viewModel.setDollarType(tradeType, dollarType)
        }
    }

    override fun initData() {
        viewModel.setDollarType(tradeType, DollarType.USDT)
    }

    override fun setupObservers() {
        collectFlow(viewModel.getPriceState(tradeType)) { state ->
            when (state) {
                is UiState.Loading -> {
                    //println("naty getPriceState UiState.Loading")
                    showPriceLoadingState()
                }

                is UiState.Success -> {
                    //println("naty getPriceState UiState.Success ${state.data.toString()}")
                    showPriceDataSuccess(state.data)
                }

                is UiState.Error -> Unit
            }
        }
        collectFlow(viewModel.getPriceRangeState(tradeType)) { state ->
            when (state) {
                is UiState.Loading -> {
                    //println("naty getPriceRangeState UiState.Loading")
                    showPriceRangeLoadingState()
                }

                is UiState.Success -> {
                    showPriceRangeDataSuccess(state.data)
                    //println("naty getPriceRangeState UiState.Success ${state.data.toString()}")
                }

                is UiState.Error -> Unit
            }
        }

        collectFlow(viewModel.getDailyCandleState(tradeType)) { state ->
            when (state) {
                is UiState.Loading -> {
                    //showUpdateTimeShimmer()
                    if (shouldShowLoading) {
                        showPriceLoadingState()
                    }
                    println("naty getDailyCandleState UiState.Loading")
                }

                is UiState.Success -> {
                    shouldShowLoading = false // ✅ Una vez cargado, no mostrar más loading

                    showChartDataSuccess(state.data)
                    println("naty getDailyCandleState UiState.Success ${state.data.toString()}")
                }
                is UiState.Error -> Unit
            }
        }
    }
    private var shouldShowLoading = true

    private fun resetDataUIComponents() {
        with(binding.priceValue) {
            assetView.invisible()
            priceTextView.invisible()
            dollarTypeSwitch.invisible()
            assetTextView.clearText()
            fiatTextView.clearText()
            descriptionTextView.clearText()
        }

        with(binding.range) {
            minTextView.clearText()
            minLabelTextView.clearText()
            medianTextView.clearText()
            medianLabelTextView.clearText()
            maxTextView.clearText()
            maxLabelTextView.clearText()
            rangeLabel.clearText()
            currencyTextView.clearText()
            dotView.invisible()
            rangeTitle.invisible()
            rangeTitleShimmer.visible()
            rangeTitleShimmer.startShimmer()
        }


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
        }
    }

    private fun showPriceLoadingState() = with(binding) {
        priceValue.root.gone()
        priceShimmer.root.visible()
        priceShimmer.shimmerLayout.startShimmer()
    }

    private fun showPriceDataSuccess(price: Price) = with(binding) {
        if (priceValue.root.isVisible) {
            setPriceData(price)
            return@with
        }
        val fadeOut = AnimationUtils.loadAnimation(requireContext(), R.anim.anim_view_fade_out)
        fadeOut.setAnimationListener(object : SimpleAnimationListener() {
            override fun onAnimationEnd(animation: Animation?) {
                hidePriceLoading()
                setPriceData(price)
                val fadeIn =
                    AnimationUtils.loadAnimation(requireContext(), R.anim.anim_view_fade_in)
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

    private fun hidePriceLoading() = with(binding) {
        priceShimmer.shimmerLayout.stopShimmer()
        priceShimmer.root.gone()
    }

    ////////////////////////////////
    private fun showPriceRangeLoadingState() = with(binding) {
        range.rangeValue.invisible()
        range.shimmerLayout.visible()
        range.shimmerLayout.startShimmer()
        range.rangeTitle.invisible()
        range.rangeTitleShimmer.visible()
        range.rangeTitleShimmer.startShimmer()
    }

    private fun showPriceRangeDataSuccess(priceRange: PriceRange) = with(binding) {
        if (range.rangeValue.isVisible) {
            setPriceRangeData(priceRange)
            return@with
        }
        val fadeOut = AnimationUtils.loadAnimation(requireContext(), R.anim.anim_view_fade_out)
        fadeOut.setAnimationListener(object : SimpleAnimationListener() {
            override fun onAnimationEnd(animation: Animation?) {
                hidePriceRangeLoading()
                setPriceRangeData(priceRange)
                val fadeIn =
                    AnimationUtils.loadAnimation(requireContext(), R.anim.anim_view_fade_in)
                range.rangeValue.visible()
                range.rangeValue.startAnimation(fadeIn)

                range.rangeTitle.visible()
                range.rangeTitle.startAnimation(fadeIn)
            }
        })

        if (range.shimmerLayout.isVisible) {
            range.shimmerLayout.startAnimation(fadeOut)
        }
        if (range.rangeTitleShimmer.isVisible) {
            range.rangeTitleShimmer.startAnimation(fadeOut)
        }
    }

    private fun setPriceRangeData(priceRange: PriceRange) = with(binding.range) {
        currencyTextView.text = priceRange.currency
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
        rangeLabel.text = getString(R.string.price_view_pager_item_range)
        dotView.visible()
    }

    private fun hidePriceRangeLoading() = with(binding) {
        range.shimmerLayout.stopShimmer()
        range.shimmerLayout.gone()
        range.rangeTitleShimmer.stopShimmer()
        range.rangeTitleShimmer.gone()
    }

    ////////////////////////////////

    private fun setChartData(dailyCandles: List<DailyCandle>) = with(binding.chart) {
        val json =  Gson().toJson(dailyCandles)
        println("naty CHART $json")
        ///
        binding.updateTime.updatedTextView.text = dailyCandles.getDateRangeLabel()

        binding.updateTime.updateTextView.visible()
        binding.updateTime.dotTextView.visible()
        binding.updateTime.updatedTextView.visible()
        //

        labelTextView.visible()


        val entries = dailyCandles.toCandleEntries()
        val xAxisValues = dailyCandles.toXAxisValues()
        val dataSetLabel = dailyCandles.toDataSetLabel()
        val fiat = dailyCandles.toFiat()

        val candleDataSet = CandleDataSet(entries, dataSetLabel)
        candleDataSet.apply {
            barSpace = 0.3f   // 🔥 velas delgadas
            shadowWidth = 1f
            shadowColorSameAsCandle = true // <-- hace que la sombra tenga el mismo color que la vela
            decreasingColor = requireContext().getColorRes(R.color.red)
            decreasingPaintStyle = Paint.Style.FILL
            increasingColor = requireContext().getColorRes(R.color.green)
            increasingPaintStyle = Paint.Style.FILL
            neutralColor = requireContext().getColorRes(R.color.white_alpha_50)
            setDrawValues(false)
        }

        chart.axisRight.apply {
            valueFormatter = object : ValueFormatter() {
                override fun getFormattedValue(value: Float): String {
                    return "$fiat ${String.format("%.2f", value)}"
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

    private fun showChartLoadingState() = with(binding) {
        updateTimeShimmer.shimmerLayout.startShimmer()
        updateTimeShimmer.root.visible()

        chart.shimmerLayout.startShimmer()
        chart.shimmerLayout.visible()
        chart.chart.invisible()

    }

    private fun showChartDataSuccess(dailyCandles: List<DailyCandle>) = with(binding) {
        // ✅ SIEMPRE ocultar el loading primero (con animación)
        val fadeOut = AnimationUtils.loadAnimation(requireContext(), R.anim.anim_view_fade_out)
        fadeOut.setAnimationListener(object : SimpleAnimationListener() {
            override fun onAnimationEnd(animation: Animation?) {
                hideChartLoadingState()

                // ✅ LUEGO mostrar el contenido con animación
                setChartData(dailyCandles)
                val fadeIn = AnimationUtils.loadAnimation(requireContext(), R.anim.anim_view_fade_in)
                chart.valueData.visible()
                chart.valueData.startAnimation(fadeIn)
                updateTime.root.visible()
                updateTime.root.startAnimation(fadeIn)
            }
        })

        // Iniciar la animación de ocultamiento
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

    private fun setupRollingTextView() = with(binding.priceValue) {
        priceTextView.animationDuration = 600L
        priceTextView.addCharOrder(CharOrder.Number)
        priceTextView.animationInterpolator = AccelerateDecelerateInterpolator()
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
            decreasingColor = requireContext().getColorRes(R.color.cool_grey)
            decreasingPaintStyle = Paint.Style.FILL
            increasingColor = requireContext().getColorRes(R.color.cool_grey)
            increasingPaintStyle = Paint.Style.FILL
            neutralColor = requireContext().getColorRes(R.color.cool_grey)
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
                textColor = requireContext().getColorRes(R.color.cool_grey)
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
                textColor = requireContext().getColorRes(R.color.cool_grey)
                textSize = 8f
                gridColor = requireContext().getColorRes(R.color.cool_grey)
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
                textColor = requireContext().getColorRes(R.color.cool_grey)
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
            setDrawBorders(false)       // quitar borde del gráfico
            setExtraOffsets(0f, 0f, 0f, 0f)
            setTouchEnabled(false)       // ❌ desactiva todos los eventos táctiles
            setDragEnabled(false)        // ❌ desactiva el drag (mover con dedo)
            setScaleEnabled(false)       // ❌ desactiva pinch zoom
            setPinchZoom(false)          // ❌ desactiva pinch-to-zoom combinado
            isDoubleTapToZoomEnabled = false // ❌ desactiva zoom con doble tap
            isHighlightPerTapEnabled = false   // ❌ evita que seleccione una vela al tocar
        }
        chart.legend.apply {
            isEnabled = true
            textSize = 9f
            textColor = requireContext().getColorRes(R.color.white_alpha_65)
            verticalAlignment = Legend.LegendVerticalAlignment.TOP
            horizontalAlignment = Legend.LegendHorizontalAlignment.LEFT
            orientation = Legend.LegendOrientation.HORIZONTAL
        }
        chart.description.apply {
            isEnabled = false
        }
        chart.axisLeft.apply {
            setDrawGridLines(false)
            isEnabled = false  // opcional, si quieres quitar eje izquierdo
        }
        chart.axisRight.apply {
            setDrawAxisLine(false) // oculta la línea del eje Y derecho
            setDrawLabels(true)    // mantiene los valores visibles
            setDrawGridLines(true)
            textColor = requireContext().getColorRes(R.color.white_alpha_65)
            textSize = 8f
            gridColor = requireContext().getColorRes(R.color.white_alpha_05)
            gridLineWidth = 1f         // 🔹 grosor de línea (opcional)
        }

        chart.xAxis.apply {
            position = XAxis.XAxisPosition.BOTTOM
            setDrawLabels(true)  // mostrar etiquetas
            setDrawGridLines(false) // quitar líneas de cuadrícula
            setDrawAxisLine(false) // oculta la línea del eje X derecho
            textSize = 8f
            textColor = requireContext().getColorRes(R.color.white_alpha_65)
        }
    }

    /*override fun onStart() {
        super.onStart()
        viewModel.observeTradeType(tradeType)
        // Opcional: forzar refresh al volver a la pestaña
        viewModel.refresh(tradeType)
    }*/

    companion object {
        private const val TRADER_TYPE = "TRADER_TYPE"

        fun newInstance(tradeType: TradeType) = PriceItemPagerFragment().apply {
            arguments = Bundle().apply {
                putSerializable(TRADER_TYPE, tradeType)
            }
        }
    }
}