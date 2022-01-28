package com.hostel.shoppingcart.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.hostel.shoppingcart.data.model.Dorm

@Dao
interface DormDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(sale: Dorm)

    @Query("SELECT * FROM dorms")
    suspend fun getAllDorms(): List<Dorm>

    @Query("SELECT * FROM dorms WHERE bedsAvailable>0")
    suspend fun getAllDormsAvailable(): List<Dorm>

    @Query("DELETE FROM dorms WHERE id = :dormId")
    suspend fun removeDorm(dormId: String)

    @Query("DELETE FROM dorms")
    suspend fun clear()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(sales: List<Dorm>)
}