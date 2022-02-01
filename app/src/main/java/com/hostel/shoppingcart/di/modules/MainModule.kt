package com.hostel.shoppingcart.di.modules

import androidx.lifecycle.ViewModel
import com.hostel.shoppingcart.di.ViewModelKey
import com.hostel.shoppingcart.ui.MainViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class MainModule {

    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    abstract fun bindCartViewModel(viewModel: MainViewModel): ViewModel
}