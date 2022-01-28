package com.hostel.shoppingcart.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.hostel.shoppingcart.data.model.Dorm

@Database(entities = [Dorm::class], version = 1)
abstract class ShoppingCartDatabase : RoomDatabase() {
    abstract fun dormDao(): DormDao
}