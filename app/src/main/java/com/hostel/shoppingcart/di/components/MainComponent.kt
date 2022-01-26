package com.hostel.shoppingcart.di.components

import com.hostel.shoppingcart.di.scopes.ActivityScope
import com.hostel.shoppingcart.ui.CheckoutFragment
import com.hostel.shoppingcart.ui.HomeFragment
import com.hostel.shoppingcart.ui.MainActivity
import dagger.Subcomponent

@ActivityScope
@Subcomponent
interface MainComponent {
    @Subcomponent.Factory
    interface Factory {
        fun create(): MainComponent
    }

    fun inject(activity: MainActivity)
    fun inject(fragment:HomeFragment)
    fun inject(fragment: CheckoutFragment)
}