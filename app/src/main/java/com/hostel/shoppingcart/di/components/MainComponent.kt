package com.hostel.shoppingcart.di.components

import com.hostel.shoppingcart.di.modules.MainModule
import com.hostel.shoppingcart.di.scopes.ActivityScope
import com.hostel.shoppingcart.ui.checkout.CheckoutFragment
import com.hostel.shoppingcart.ui.MainActivity
import com.hostel.shoppingcart.ui.home.HomeFragment
import dagger.Subcomponent

@ActivityScope
@Subcomponent(modules = [MainModule::class])
interface MainComponent {
    @Subcomponent.Factory
    interface Factory {
        fun create(): MainComponent
    }

    fun inject(activity: MainActivity)
    fun inject(fragment: HomeFragment)
    fun inject(fragment: CheckoutFragment)
}