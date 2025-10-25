package com.bolivianusd.app.feature.price.presentation.view

import android.annotation.SuppressLint
import android.content.Context
import android.widget.TextView
import com.bolivianusd.app.R
import com.bolivianusd.app.core.extensions.getColorRes
import com.github.mikephil.charting.components.MarkerView
import com.github.mikephil.charting.data.CandleEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.utils.MPPointF
import eightbitlab.com.blurview.BlurTarget
import eightbitlab.com.blurview.BlurView
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

@SuppressLint("ViewConstructor")
class CandleMarkerView(
    context: Context,
    layoutRes: Int
) : MarkerView(context, layoutRes) {

    private val blurView: BlurView = findViewById(R.id.blurView)
    private val blurTarget: BlurTarget = findViewById(R.id.blurTarget)
    private val dateTextView: TextView = findViewById(R.id.dateTextView)
    private val openTextView: TextView = findViewById(R.id.openTextView)
    private val closeTextView: TextView = findViewById(R.id.closeTextView)
    private val lowTextView: TextView = findViewById(R.id.lowTextView)
    private val highTextView: TextView = findViewById(R.id.highTextView)

    init {
        setupBlur()
    }

    private fun setupBlur() {
        val radius = 18f
        blurView.setupWith(blurTarget)
            .setBlurRadius(radius)
            .setBlurAutoUpdate(true)
    }

    override fun refreshContent(entry: Entry?, highlight: Highlight?) {
        if (entry is CandleEntry) {
            dateTextView.text = entry.data.toString()
            openTextView.text = formatValue(entry.open)
            closeTextView.text = formatValue(entry.close)
            lowTextView.text = formatValue(entry.low)
            highTextView.text = formatValue(entry.high)

            val openColorRes = R.color.malibu
            val closeColorRes = when {
                entry.close > entry.open -> R.color.green
                entry.close < entry.open -> R.color.red
                else -> R.color.yellow
            }
            openTextView.setTextColor(context.getColorRes(openColorRes))
            closeTextView.setTextColor(context.getColorRes(closeColorRes))
        }
        super.refreshContent(entry, highlight)
    }

    override fun getOffset(): MPPointF {
        return MPPointF(-(width / 2f), -height.toFloat())
    }

    private fun formatValue(value: Float): String {
        val formatter = DecimalFormat("#,###.00", DecimalFormatSymbols(Locale.ENGLISH))
        return formatter.format(value)
    }
}
