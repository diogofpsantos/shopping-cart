package com.hostel.shoppingcart.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.hostel.shoppingcart.data.model.Dorm
import com.hostel.shoppingcart.data.model.NetworkStatsResponse

@Database(entities = [Dorm::class, NetworkStatsResponse::class], version = 2)
abstract class ShoppingCartDatabase : RoomDatabase() {
    abstract fun dormDao(): DormDao
    abstract fun statsDao(): StatsDao
}