package com.hostel.shoppingcart.di.modules

import androidx.lifecycle.ViewModelProvider
import com.hostel.shoppingcart.ShoppingCartViewModelFactory
import dagger.Binds
import dagger.Module

@Module
abstract class ViewModelModule {

    @Binds
    abstract fun bindViewModelFactory(factory: ShoppingCartViewModelFactory): ViewModelProvider.Factory
}