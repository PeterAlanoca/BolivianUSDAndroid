package com.bolivianusd.app.ui.price

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import com.bolivianusd.app.R
import com.bolivianusd.app.databinding.FragmentPriceParallelBinding
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import com.google.firebase.database.getValue
import com.yy.mobile.rollingtextview.CharOrder

class PriceParallelFragment : Fragment() {

    private lateinit var binding: FragmentPriceParallelBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPriceParallelBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        test()
        loadData()
    }

    private fun loadData() {
        val database = Firebase.database
        val myRef = database.getReference("price_buy_usdt")
        //myRef.keepSynced(true) offline
        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val value = snapshot.getValue()
                println("naty Valor recibido: ${value.toString()}")
            }

            override fun onCancelled(error: DatabaseError) {
                println("naty Error al leer datos ${error.toException()}")
            }
        })

    }





































































    fun test() = with(binding) {

        exchangeRateTextView.animationDuration = 1000L
        exchangeRateTextView.addCharOrder(CharOrder.Number);
        exchangeRateTextView.animationInterpolator = AccelerateDecelerateInterpolator()

        exchangeRateTextView.setText("11.50")

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

        lineChartHeartRate.description.isEnabled = false
        lineChartHeartRate.legend.isEnabled = false
        lineChartHeartRate.description.isEnabled = false
        /*lineChartHeartRate.description.textColor = getColorResource(R.color.maroon_flush)
        lineChartHeartRate.description.textSize = 15f
        lineChartHeartRate.description.text = "binance"*/

        lineChartHeartRate.setTouchEnabled(false)
        lineChartHeartRate.setDrawGridBackground(false)
        lineChartHeartRate.setDragEnabled(true)
        lineChartHeartRate.setScaleEnabled(true)
        lineChartHeartRate.setPinchZoom(false)

        //lineChartHeartRate.setViewPortOffsets(20f,40f, 20f, 30f)

        lineChartHeartRate.extraBottomOffset = 7f
        lineChartHeartRate.extraTopOffset = 10f
        lineChartHeartRate.minOffset = 0f


        lineChartHeartRate.xAxis.setDrawAxisLine(false)
        lineChartHeartRate.xAxis.setDrawGridLines(false)
        lineChartHeartRate.xAxis.setDrawLabels(true)
        lineChartHeartRate.xAxis.textColor = getColorResource(com.bolivianusd.app.R.color.maroon_flush)
        lineChartHeartRate.xAxis.textSize = 10f
        lineChartHeartRate.xAxis.position = com.github.mikephil.charting.components.XAxis.XAxisPosition.BOTTOM
        //lineChartHeartRate.xAxis.enableGridDashedLine(10f, 10f, 0f)
        //lineChartHeartRate.minOffset = 0f

        //lineChartHeartRate.xAxis.setAvoidFirstLastClipping(true)

        //lineChartHeartRate.xAxis.setAvoidFirstLastClipping(true)

        val list_of_labels = arrayOf("", "04/09", "05/09", "06/09", "07/09", "08/09", "09/09", "")
        lineChartHeartRate.xAxis.setLabelCount(list_of_labels.size, true)
        lineChartHeartRate.xAxis.valueFormatter = IndexAxisValueFormatter(list_of_labels)

        lineChartHeartRate.axisRight.isEnabled = false
        lineChartHeartRate.axisRight.setDrawGridLines(false)
        lineChartHeartRate.axisRight.setDrawLabels(false)

        lineChartHeartRate.axisLeft.isEnabled = false
        lineChartHeartRate.axisLeft.setDrawGridLines(false)
        lineChartHeartRate.axisLeft.setDrawLabels(false)



        lineChartHeartRate.axisLeft.setAxisMaximum(14f)
        lineChartHeartRate.axisLeft.setAxisMinimum(6f)

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

        //values.add(Entry(6f, 10.85f))
        //values.add(Entry(7f, 10.7f))
        //values.add(Entry(8f, 10.1f))

        /*values.add(Entry(9f, 10.0f))
        values.add(Entry(10f, 11.5f))
        values.add(Entry(11f, 11.2f))
        values.add(Entry(12f, 10.15f))
        values.add(Entry(13f, 11.11f))
        values.add(Entry(14f, 12.85f))*/

        println(values)


        val dataSet = LineDataSet(values, "Fuente: binance.com")
        dataSet.setDrawIcons(true)
        dataSet.setDrawValues(true)

        dataSet.valueTextSize = 11f
        val colors = arrayOf(getColorResource(R.color.red), getColorResource(com.bolivianusd.app.R.color.red), getColorResource(
            com.bolivianusd.app.R.color.green), getColorResource(com.bolivianusd.app.R.color.green), getColorResource(
            com.bolivianusd.app.R.color.green), getColorResource(com.bolivianusd.app.R.color.red), getColorResource(
            com.bolivianusd.app.R.color.green), getColorResource(com.bolivianusd.app.R.color.green))
        dataSet.setValueTextColors(colors.toList())


        dataSet.enableDashedLine(10f, 0f, 0f)
        dataSet.color = getColorResource(com.bolivianusd.app.R.color.maroon_flush)
        dataSet.setCircleColor(getColorResource(com.bolivianusd.app.R.color.maroon_flush))
        dataSet.lineWidth = 3f
        dataSet.circleRadius = 4f
        dataSet.setDrawCircleHole(false)
        //dataSet.enableDashedHighlightLine(10f, 10f, 10f)
        dataSet.setDrawFilled(true)
        dataSet.mode = com.github.mikephil.charting.data.LineDataSet.Mode.HORIZONTAL_BEZIER
        dataSet.fillDrawable =
            androidx.core.content.ContextCompat.getDrawable(requireContext(), com.bolivianusd.app.R.drawable.gradient_chart)



        val dataSets = ArrayList<ILineDataSet>()
        dataSets.add(dataSet)


        lineChartHeartRate.setData(LineData(dataSets))



    }

    fun getColorResource(id: Int): Int {
        return ResourcesCompat.getColor(resources, id, null) //without theme
    }


    companion object {
        const val TAG = "PriceParallelFragment"
        fun newInstance() = PriceParallelFragment()
    }

}