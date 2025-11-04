package com.bolivianusd.app.shared.data.local.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.bolivianusd.app.shared.data.local.room.entity.PriceEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PriceDao {

    @Query("SELECT * FROM price WHERE asset = :asset AND type = :type LIMIT 1")
    fun getPrice(asset: String, type: String): Flow<PriceEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(price: PriceEntity)
}


