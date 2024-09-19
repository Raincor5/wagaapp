package com.wagaapp.foodguide.data.dao

import androidx.room.*
import com.wagaapp.foodguide.data.entitites.SessionData

@Dao
interface SessionDataDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSessionData(sessionData: kotlin.collections.List<com.wagaapp.foodguide.data.entitites.SessionData>)

    @Query("DELETE FROM SessionData WHERE timestamp < :timestamp")
    suspend fun clearOldSessionData(timestamp: Long): Int

    @Query("SELECT * FROM SessionData")
    fun getAllSessionData(): List<SessionData>

}