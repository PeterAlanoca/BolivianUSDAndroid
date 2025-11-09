package com.bolivianusd.app.shared.data.local.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.bolivianusd.app.shared.data.local.room.entity.PriceRangeEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PriceRangeDao {

    @Query("SELECT * FROM price_range WHERE asset = :asset AND type = :type LIMIT 1")
    fun getPriceRange(asset: String, type: String): Flow<PriceRangeEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(priceRange: PriceRangeEntity)
}