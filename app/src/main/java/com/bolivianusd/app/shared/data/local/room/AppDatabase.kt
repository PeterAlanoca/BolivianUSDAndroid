package com.bolivianusd.app.shared.data.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.bolivianusd.app.feature.price.data.local.room.dao.DailyCandleDao
import com.bolivianusd.app.feature.price.data.local.room.entity.DailyCandleEntity
import com.bolivianusd.app.shared.data.local.room.dao.PriceDao
import com.bolivianusd.app.shared.data.local.room.dao.PriceRangeDao
import com.bolivianusd.app.shared.data.local.room.entity.PriceEntity
import com.bolivianusd.app.shared.data.local.room.entity.PriceRangeEntity

@Database(
    entities = [PriceEntity::class, PriceRangeEntity::class, DailyCandleEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun priceDao(): PriceDao
    abstract fun priceRangeDao(): PriceRangeDao
    abstract fun dailyCandleDao(): DailyCandleDao
}