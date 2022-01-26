package com.hostel.shoppingcart.data.db

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(context: Context): ShoppingCartDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            ShoppingCartDatabase::class.java, "ShoppingCartDatabase"
        ).fallbackToDestructiveMigration().build()
    }
}