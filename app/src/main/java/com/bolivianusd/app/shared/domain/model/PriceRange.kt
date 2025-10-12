package com.bolivianusd.app.shared.domain.model

import com.bolivianusd.app.core.util.emptyString
import java.math.BigDecimal
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class PriceRange(
    var median: RangeValue = RangeValue(),
    var min: RangeValue = RangeValue(),
    var max: RangeValue = RangeValue(),
    var asset: String = emptyString,
    var fiat: String = emptyString,
    var updatedAt: String = emptyString
) {
    data class RangeValue(
        var value: BigDecimal = BigDecimal.ZERO,
        var valueLabel: String = emptyString,
        var description: String = emptyString
    )

    val descriptionLabel: String
        get() = "${min.description} ${min.valueLabel} " +
                "| ${median.description} ${median.valueLabel} " +
                "| ${max.description} ${max.valueLabel}"

    val updatedAtFormat: String
        get() {
            return try {
                var safe = updatedAt.replace(Regex(":(\\d\\d)\$"), "$1")
                safe = safe.replace(Regex("\\.(\\d{3})\\d*"), ".$1")
                val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.getDefault())
                val outputFormat = SimpleDateFormat("dd/MM/yy HH:mm:ss", Locale.getDefault())
                val date = inputFormat.parse(safe)
                outputFormat.format(date)
            } catch (e: Exception) {
                e.printStackTrace()
                SimpleDateFormat("dd/MM/yy HH:mm:ss", Locale.getDefault()).format(Date())
            }
        }

}
