package com.hostel.shoppingcart

import android.app.Application
import com.hostel.shoppingcart.di.components.AppComponent
import com.hostel.shoppingcart.di.components.DaggerAppComponent

class ShoppingCartApplication : Application() {
    val appComponent: AppComponent by lazy {
        DaggerAppComponent.factory().create(this)
    }
}