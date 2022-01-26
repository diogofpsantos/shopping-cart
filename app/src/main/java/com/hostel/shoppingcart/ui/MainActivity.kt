package com.hostel.shoppingcart.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.hostel.shoppingcart.R
import com.hostel.shoppingcart.ShoppingCartApplication
import com.hostel.shoppingcart.databinding.ActivityHomeBinding
import com.hostel.shoppingcart.di.components.MainComponent

class MainActivity : AppCompatActivity() {
    lateinit var mainComponent: MainComponent

    private lateinit var binding: ActivityHomeBinding
    lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        mainComponent =
            (applicationContext as ShoppingCartApplication).appComponent.mainComponent().create()
        mainComponent.inject(this)

        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        navController = findNavController(R.id.nav_host_fragment)
        binding.toolbar.setupWithNavController(navController)
    }
}