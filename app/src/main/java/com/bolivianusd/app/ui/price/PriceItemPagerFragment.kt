package com.bolivianusd.app.ui.price

import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.fragment.app.activityViewModels
import com.bolivianusd.app.R
import com.bolivianusd.app.core.base.BaseFragment
import com.bolivianusd.app.core.extensions.getColorRes
import com.bolivianusd.app.core.extensions.getDrawableRes
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

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentPriceItemPagerBinding.inflate(inflater, container, false)

    override fun initViews() {
        setupRollingTextView()
        setupLineChart()

        loadData()
    }

    private fun loadData() {
        viewModel.priceBuy.observe(viewLifecycleOwner) { price ->
            println("naty priceData ${price.toString()}")
        }
    }

    private fun setupRollingTextView() = with(binding) {
        exchangeRateTextView.animationDuration = 1000L
        exchangeRateTextView.addCharOrder(CharOrder.Number);
        exchangeRateTextView.animationInterpolator = AccelerateDecelerateInterpolator()
        exchangeRateTextView.setText("11.50")
    }

    private fun setupLineChart() = with(binding) {
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

    private fun createData() = with(binding) {

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

    companion object {
        const val TAG = "PriceItemPagerFragment"
        fun newInstance() = PriceItemPagerFragment()
    }

}