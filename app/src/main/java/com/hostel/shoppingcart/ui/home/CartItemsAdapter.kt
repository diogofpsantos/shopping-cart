package com.hostel.shoppingcart.ui.home

import com.hostel.shoppingcart.R
import com.hostel.shoppingcart.data.model.Dorm
import com.hostel.shoppingcart.data.model.DormItem
import com.hostel.shoppingcart.databinding.ItemDormBinding
import com.hostel.shoppingcart.ui.base.BaseAdapter
import java.text.NumberFormat
import java.util.*

class CartItemsAdapter(private val optionsListener: OptionsListener) :
    BaseAdapter<DormItem, ItemDormBinding>(R.layout.item_dorm) {

    interface OptionsListener {
        fun addToCart(item: Dorm)
        fun removeFromCart(item: Dorm)
    }

    override fun bind(binding: ItemDormBinding, item: DormItem, position: Int) {
        binding.dorm = item
        val currencyFormat = NumberFormat.getCurrencyInstance()
        currencyFormat.currency = Currency.getInstance("USD")
        binding.currencyFormat = currencyFormat
        binding.removeBtnLayout.setOnClickListener {
            if (item.quantity > 0) {
                item.quantity--
                notifyItemChanged(position)
                optionsListener.removeFromCart(item.dorm)
            }
        }
        binding.addBtnLayout.setOnClickListener {
            if (item.quantity < item.dorm.bedsAvailable) {
                item.quantity++
                notifyItemChanged(position)
                optionsListener.addToCart(item.dorm)
            }
        }
    }
}