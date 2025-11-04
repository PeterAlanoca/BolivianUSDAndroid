package com.bolivianusd.app.shared.data.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.bolivianusd.app.shared.data.local.room.dao.PriceDao
import com.bolivianusd.app.shared.data.local.room.entity.PriceEntity

@Database(
    entities = [PriceEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun priceDao(): PriceDao
}