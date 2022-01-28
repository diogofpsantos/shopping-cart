package com.hostel.shoppingcart.ui.home

import com.hostel.shoppingcart.R
import com.hostel.shoppingcart.data.model.CartItem
import com.hostel.shoppingcart.databinding.ItemDormBinding
import com.hostel.shoppingcart.ui.base.BaseAdapter
import java.text.NumberFormat
import java.util.*

class CartItemsAdapter(private val clickListener: () -> Unit) : BaseAdapter<CartItem, ItemDormBinding>(R.layout.item_dorm) {

    override fun bind(binding: ItemDormBinding, item: CartItem, position: Int) {
        binding.cartItem = item
        val currencyFormat = NumberFormat.getCurrencyInstance()
        currencyFormat.currency = Currency.getInstance("USD")
        binding.currencyFormat = currencyFormat
        binding.removeBtnLayout.setOnClickListener {
            if (item.quantity > 0) {
                item.quantity--
                notifyItemChanged(position)
                clickListener.invoke()
            }
        }
        binding.addBtnLayout.setOnClickListener {
            if (item.quantity < item.dorm.bedsAvailable) {
                item.quantity++
                notifyItemChanged(position)
                clickListener.invoke()
            }
        }
    }
}