package com.hostel.shoppingcart.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.hostel.shoppingcart.data.model.NetworkStatsResponse

@Dao
interface StatsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(stats: NetworkStatsResponse)

    @Query("SELECT * FROM stats")
    suspend fun getAllStats(): List<NetworkStatsResponse>

    @Query("SELECT * FROM stats WHERE NOT synced")
    suspend fun getAllStatsToSync(): List<NetworkStatsResponse>

    @Query("DELETE FROM stats WHERE id = :statsId")
    suspend fun removeStats(statsId: String)

    @Query("Update stats set synced = :state where id = :id ")
    suspend fun updateStatsSynced(id: Long, state: Boolean)

    @Query("DELETE FROM stats")
    suspend fun clear()
}