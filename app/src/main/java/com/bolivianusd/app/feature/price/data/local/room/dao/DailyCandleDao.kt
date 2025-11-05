package com.bolivianusd.app.feature.price.data.local.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.bolivianusd.app.feature.price.data.local.room.entity.DailyCandleEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface DailyCandleDao {

    @Query("SELECT * FROM daily_candles WHERE asset = :asset AND type = :type ORDER BY x ASC")
    fun getCandles(asset: String, type: String): Flow<List<DailyCandleEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(candles: List<DailyCandleEntity>)

}
