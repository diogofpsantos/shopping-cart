package com.hostel.shoppingcart.ui.checkout

import com.hostel.shoppingcart.R
import com.hostel.shoppingcart.data.model.CartItem
import com.hostel.shoppingcart.databinding.ItemCartBinding
import com.hostel.shoppingcart.ui.base.BaseAdapter
import java.text.NumberFormat
import java.util.*

class CheckoutItemsAdapter : BaseAdapter<CartItem, ItemCartBinding>(R.layout.item_cart) {
    override fun bind(binding: ItemCartBinding, item: CartItem, position: Int) {
        binding.price = item.dorm.price
        binding.number = item.quantity
        val format = NumberFormat.getCurrencyInstance()
        format.currency = Currency.getInstance(item.conversion.currency)
        binding.currencyFormat = format
        binding.name = item.dorm.name
        binding.conversion = item.conversion.conversion
    }
}