package com.hostel.shoppingcart.di.modules

import android.content.Context
import com.google.gson.Gson
import com.hostel.shoppingcart.ShoppingCartApplication
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule {
    @Provides
    fun provideContext(application: ShoppingCartApplication): Context {
        return application.applicationContext
    }

    @Provides
    @Singleton
    fun provideGson(): Gson = Gson()
}