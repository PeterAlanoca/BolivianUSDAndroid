package com.bolivianusd.app.feature.price.data.mappers

import com.bolivianusd.app.feature.price.data.remote.supabase.dto.DailyCandleDto
import com.bolivianusd.app.feature.price.domain.model.DailyCandle

fun DailyCandleDto.toDailyCandle(): DailyCandle {
    return DailyCandle( )
}
