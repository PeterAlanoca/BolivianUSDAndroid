package com.bolivianusd.app.ui.price

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
import com.bolivianusd.app.core.extensions.getColorRes
import com.bolivianusd.app.core.extensions.getDrawableRes
import com.bolivianusd.app.core.extensions.gone
import com.bolivianusd.app.core.extensions.serializable
import com.bolivianusd.app.core.extensions.visible
import com.bolivianusd.app.core.listeners.SimpleAnimationListener
import com.bolivianusd.app.core.formats.AmountValueFormatter
import com.bolivianusd.app.core.util.ONE
import com.bolivianusd.app.core.util.ZERO
import com.bolivianusd.app.core.util.ZERO_F
import com.bolivianusd.app.core.util.emptyBar
import com.bolivianusd.app.core.util.emptyString
import com.bolivianusd.app.data.repository.entity.PriceBuy
import com.bolivianusd.app.data.repository.entity.enum.OperationType
import com.bolivianusd.app.data.repository.state.State
import com.bolivianusd.app.databinding.FragmentPriceItemPagerBinding
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.yy.mobile.rollingtextview.CharOrder
import kotlin.getValue

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
        setupRollingTextView()
        setupLineChartShimmer()
        setupLineChart()

        /*Handler(Looper.getMainLooper()).postDelayed({
            animateShowPriceValue()
            animateShowUpdateTimeView()
            animateShowChartView()
            animateShowRangeView()
        }, 2200)*/
    }

    override fun initData() {
        getPrice()
    }

    private fun getPrice() {
        viewModel.priceBuy.observe(viewLifecycleOwner) { state ->
            when (state) {
                is State.Loading -> showPriceShimmer()

                is State.Success -> {
                    setDataPriceValue(state.data)
                    animateShowPriceValue()
                }

                is State.Error -> Unit
            }
        }
    }

    private fun setDataPriceValue(priceBuy: PriceBuy) = with(binding.priceValue) {
        with(priceBuy) {
            originCurrencyTextView.text = origin.currency
            originAmountTextView.setText(origin.amountLabel)
            destinationCurrencyTextView.text = destination.currency
            descriptionTextView.text = priceBuy.label
        }
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

    private fun animateShowUpdateTimeView() = with(binding) {
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

    private fun animateShowChartView() = with(binding) {
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

    private fun animateShowRangeView() = with(binding) {
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

    private fun setupLineChartShimmer() = with(binding.chartShimmer) {
        val labels = List(CHART_DATA_ITEMS_SIZE) { index ->
            if (index == ZERO || index == CHART_DATA_ITEMS_SIZE - ONE) emptyString else emptyBar
        }
        val colors = List(CHART_DATA_ITEMS_SIZE) {
            requireContext().getColorRes(R.color.cool_grey)
        }
        val values = List(CHART_DATA_ITEMS_SIZE) { index -> Entry(index.toFloat(), ZERO_F) }

        lineChart.apply {
            description.isEnabled = false
            legend.isEnabled = false
            description.isEnabled = false
            extraBottomOffset = 7f
            extraTopOffset = 10f
            minOffset = 0f
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
            xAxis.textSize = 10f
            xAxis.position = XAxis.XAxisPosition.BOTTOM
            xAxis.valueFormatter = IndexAxisValueFormatter(labels)
            axisRight.isEnabled = false
            axisRight.setDrawGridLines(false)
            axisRight.setDrawLabels(false)
            axisLeft.isEnabled = false
            axisLeft.setDrawGridLines(false)
            axisLeft.setDrawLabels(false)
            axisLeft.setAxisMaximum(6f)
            axisLeft.setAxisMinimum(-6f)
        }
        val dataSet = LineDataSet(values, emptyString)
        dataSet.apply {
            valueFormatter = AmountValueFormatter()
            color = requireContext().getColorRes(R.color.cool_grey)
            valueTextSize = 11f
            lineWidth = 3f
            dataSet.circleRadius = 4f
            mode = LineDataSet.Mode.HORIZONTAL_BEZIER
            fillDrawable = requireContext().getDrawableRes(R.drawable.gradient_chart_shimmer)
            setDrawIcons(true)
            setDrawValues(true)
            setValueTextColors(colors)
            enableDashedLine(10f, 0f, 0f)
            setCircleColor(requireContext().getColorRes(R.color.cool_grey))
            setDrawCircleHole(false)
            setDrawFilled(true)
        }
        val dataSets = ArrayList<ILineDataSet>()
        dataSets.add(dataSet)
        lineChart.setData(LineData(dataSets))
    }

    private fun setupLineChart() = with(binding.chart) {
        val list_of_labels = arrayOf("", "04/09", "05/09", "06/09", "07/09", "08/09", "09/09", "")
        lineChart.apply {
            description.isEnabled = false
            legend.isEnabled = false
            description.isEnabled = false

            setTouchEnabled(false)
            setDrawGridBackground(false)
            setDragEnabled(true)
            setScaleEnabled(true)
            setPinchZoom(false)

            extraBottomOffset = 7f
            extraTopOffset = 10f
            minOffset = 0f

            xAxis.setDrawAxisLine(false)
            xAxis.setDrawGridLines(false)
            xAxis.setDrawLabels(true)
            xAxis.setLabelCount(list_of_labels.size, true)
            xAxis.textColor = requireContext().getColorRes(R.color.maroon_flush)
            xAxis.textSize = 10f
            xAxis.position = XAxis.XAxisPosition.BOTTOM
            xAxis.valueFormatter = IndexAxisValueFormatter(list_of_labels)

            axisRight.isEnabled = false
            axisRight.setDrawGridLines(false)
            axisRight.setDrawLabels(false)

            axisLeft.isEnabled = false
            axisLeft.setDrawGridLines(false)
            axisLeft.setDrawLabels(false)
            axisLeft.setAxisMaximum(14f)
            axisLeft.setAxisMinimum(6f)
        }
        createData()
    }

    private fun createData() = with(binding.chart) {

        val values: MutableList<Entry> = ArrayList()

        /*for (i in 0..14) {
            val dddd = ((Math.random() * 10).toFloat())
            println("naty " +  dddd.toString())
            values.add(Entry(i.toFloat(), dddd))
        }*/

        values.add(Entry(0f, 8f))
        values.add(Entry(1f, 8f))
        values.add(Entry(2f, 10.5f))
        values.add(Entry(3f, 10.9f))
        values.add(Entry(4f, 13.0f))
        values.add(Entry(5f, 11.20f))
        values.add(Entry(6f, 12.20f))
        values.add(Entry(7f, 12.20f))

        println(values)

        val colors = arrayOf(
            requireContext().getColorRes(R.color.red),
            requireContext().getColorRes(R.color.red),
            requireContext().getColorRes(R.color.green),
            requireContext().getColorRes(R.color.green),
            requireContext().getColorRes(R.color.green),
            requireContext().getColorRes(R.color.red),
            requireContext().getColorRes(R.color.green),
            requireContext().getColorRes(R.color.green)
        )

        val dataSet = LineDataSet(values, "Fuente: binance.com")
        dataSet.setDrawIcons(true)
        dataSet.setDrawValues(true)
        dataSet.valueFormatter = AmountValueFormatter()
        dataSet.valueTextSize = 11f
        dataSet.setValueTextColors(colors.toList())
        dataSet.enableDashedLine(10f, 0f, 0f)
        dataSet.color = requireContext().getColorRes(R.color.maroon_flush)
        dataSet.setCircleColor(requireContext().getColorRes(R.color.maroon_flush))
        dataSet.lineWidth = 3f
        dataSet.circleRadius = 4f
        dataSet.setDrawCircleHole(false)
        dataSet.setDrawFilled(true)
        dataSet.mode = LineDataSet.Mode.HORIZONTAL_BEZIER
        dataSet.fillDrawable = requireContext().getDrawableRes(R.drawable.gradient_chart)

        val dataSets = ArrayList<ILineDataSet>()
        dataSets.add(dataSet)
        lineChart.setData(LineData(dataSets))
    }

    private fun setupRollingTextView() = with(binding.priceValue) {
        originAmountTextView.animationDuration = 600L
        originAmountTextView.addCharOrder(CharOrder.Number)
        originAmountTextView.animationInterpolator = AccelerateDecelerateInterpolator()
    }

    companion object {
        private const val OPERATION_TYPE = "OPERATION_TYPE"
        private const val CHART_DATA_ITEMS_SIZE = 8

        fun newInstance(operationType: OperationType) = PriceItemPagerFragment().apply {
            val bundle = Bundle().apply {
                putSerializable(OPERATION_TYPE, operationType)
            }
            arguments = bundle
        }
    }

}