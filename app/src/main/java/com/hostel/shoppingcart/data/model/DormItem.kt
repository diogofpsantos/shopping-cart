package com.hostel.shoppingcart.data.model

data class DormItem(
    val dorm: Dorm,
    var quantity: Int = 0,
    val currency: String
)