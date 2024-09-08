package com.bolivianusd.app.ui.main

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import com.bolivianusd.app.R
import com.bolivianusd.app.core.exit
import com.bolivianusd.app.core.onBackPressed
import com.bolivianusd.app.databinding.ActivityMainBinding
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet


class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        onBackPressed { exit() }






        test()
    }

    fun test() = with(binding) {
        /*lineChartHeartRate.setBackgroundColor(getColorResource(R.color.pickled_bluewood))
        lineChartHeartRate.description.isEnabled = false
        lineChartHeartRate.setTouchEnabled(false)
        lineChartHeartRate.setDrawGridBackground(false)
        lineChartHeartRate.setDragEnabled(true)
        lineChartHeartRate.setScaleEnabled(true)
        lineChartHeartRate.setPinchZoom(false)
        lineChartHeartRate.description = null
        lineChartHeartRate.legend.isEnabled = false;
        lineChartHeartRate.setViewPortOffsets(0f, 0f, 0f, 0f)
        lineChartHeartRate.xAxis.setDrawAxisLine(false)
        lineChartHeartRate.xAxis.setDrawGridLines(false)
        lineChartHeartRate.xAxis.setDrawLabels(false)
        lineChartHeartRate.xAxis.enableGridDashedLine(10f, 10f, 0f)
        lineChartHeartRate.axisRight.isEnabled = false
        lineChartHeartRate.axisRight.setDrawGridLines(false)
        lineChartHeartRate.axisRight.setDrawLabels(false)
        lineChartHeartRate.axisLeft.setDrawGridLines(false)
        lineChartHeartRate.axisLeft.axisLineColor = getColorResource(R.color.pickled_bluewood)
        lineChartHeartRate.axisLeft.enableGridDashedLine(10f, 10f, 0f)
        lineChartHeartRate.axisLeft.setDrawLabels(false)*/

        //lineChartHeartRate.setBackgroundColor(getColorResource(R.color.pickled_bluewood))


        lineChartHeartRate.setTouchEnabled(false)
        lineChartHeartRate.setDrawGridBackground(false)
        lineChartHeartRate.setDragEnabled(true)
        lineChartHeartRate.setScaleEnabled(true)
        lineChartHeartRate.setPinchZoom(false)



        lineChartHeartRate.xAxis.setDrawAxisLine(false)
        lineChartHeartRate.xAxis.setDrawGridLines(false)
        lineChartHeartRate.xAxis.setDrawLabels(true)
        lineChartHeartRate.xAxis.textColor = getColorResource(R.color.maroon_flush)
        lineChartHeartRate.xAxis.textSize = 15f
        lineChartHeartRate.xAxis.position = XAxis.XAxisPosition.BOTTOM
        //lineChartHeartRate.xAxis.enableGridDashedLine(10f, 10f, 0f)

        lineChartHeartRate.axisRight.isEnabled = false
        lineChartHeartRate.axisRight.setDrawGridLines(false)
        lineChartHeartRate.axisRight.setDrawLabels(false)

        lineChartHeartRate.axisLeft.isEnabled = false
        lineChartHeartRate.axisLeft.setDrawGridLines(false)
        lineChartHeartRate.axisLeft.setDrawLabels(false)


        lineChartHeartRate.legend.isEnabled = true
        lineChartHeartRate.legend.textColor = getColorResource(R.color.maroon_flush)
        lineChartHeartRate.legend.textSize = 15f


        lineChartHeartRate.description.isEnabled = true
        lineChartHeartRate.description.textColor = getColorResource(R.color.maroon_flush)
        lineChartHeartRate.description.textSize = 15f
        lineChartHeartRate.description.text = "binance"

        lineChartHeartRate.axisLeft.setAxisMaximum(10f)
        lineChartHeartRate.axisLeft.setAxisMinimum(0f)

        createData()
    }

    private fun createData() = with(binding) {

        val values: MutableList<Entry> = ArrayList()

        for (i in 0..14) {
            val dddd = ((Math.random() * 10).toFloat())
            println("naty " +  dddd.toString())
            values.add(Entry(i.toFloat(), dddd))
        }
        println(values)
        val dataSet = LineDataSet(values, "siuuuuuu")
        dataSet.setDrawIcons(true)
        dataSet.setDrawValues(true)
        dataSet.valueTextSize = 15f
        dataSet.setValueTextColor(getColorResource(R.color.maroon_flush))
        //dataSet.enableDashedLine(10f, 0f, 0f)
        dataSet.color = getColorResource(R.color.maroon_flush)
        dataSet.setCircleColor(getColorResource(R.color.maroon_flush))
        dataSet.lineWidth = 2f
        dataSet.circleRadius = 0f
        dataSet.setDrawCircleHole(false)
        //dataSet.enableDashedHighlightLine(0f, 0f, 0f)
        dataSet.setDrawFilled(true)
        dataSet.mode = LineDataSet.Mode.HORIZONTAL_BEZIER
        /*if (Versioning.supportAPI18()) {
            dataSet.fillDrawable =
                ContextCompat.getDrawable(getContext(), R.drawable.gradient_chart)
        } else {
            dataSet.fillColor = getColorResource(R.color.colorBlack)
        }*/
        dataSet.fillColor = getColorResource(R.color.pickled_bluewood)

        val dataSets = ArrayList<ILineDataSet>()
        dataSets.add(dataSet)
        lineChartHeartRate.setData(LineData(dataSets))


    }

    fun getColorResource(id: Int): Int {
        return ResourcesCompat.getColor(resources, id, null) //without theme
    }


}