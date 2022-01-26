package com.hostel.shoppingcart.di.components

import com.hostel.shoppingcart.ShoppingCartApplication
import com.hostel.shoppingcart.data.db.DatabaseModule
import com.hostel.shoppingcart.data.network.NetworkModule
import com.hostel.shoppingcart.di.modules.AppModule
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, SubComponentsModule::class, NetworkModule::class, DatabaseModule::class])
interface AppComponent {
    @Component.Factory
    interface Factory {
        fun create(@BindsInstance application: ShoppingCartApplication): AppComponent
    }

    fun mainComponent(): MainComponent.Factory
}