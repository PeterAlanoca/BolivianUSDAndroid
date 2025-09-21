package com.bolivianusd.app.feature.price.domain.model

import com.bolivianusd.app.core.util.ZERO
import com.bolivianusd.app.core.util.ZERO_F
import com.bolivianusd.app.core.util.emptyString
import com.github.mikephil.charting.data.Entry

class ChartData(
    var updated: String = emptyString,
    var label: String = emptyString,
    var description: String = emptyString,
    var variation: String = emptyString,
    val variationColor: Int = ZERO,
    var price: String = emptyString,
    var labels: List<String> = emptyList(),
    val values: List<Entry> = emptyList(),
    val colors: List<Int> = emptyList(),
    val axisMaximum: Float = ZERO_F,
    val axisMinimum: Float = ZERO_F
)
