package com.hostel.shoppingcart.data.model

data class CartItem(
    val dorm: Dorm,
    var quantity: Int = 0,
    var conversion: CurrencyConversion
)