package com.hostel.shoppingcart.ui.home

import com.hostel.shoppingcart.R
import com.hostel.shoppingcart.data.model.CartItem
import com.hostel.shoppingcart.databinding.ItemDormBinding
import com.hostel.shoppingcart.ui.base.BaseAdapter

class CartItemsAdapter : BaseAdapter<CartItem, ItemDormBinding>(R.layout.item_dorm) {

    override fun bind(binding: ItemDormBinding, item: CartItem, position: Int) {
        binding.cartItem = item
        binding.removeBtnLayout.setOnClickListener { _ ->
            if (item.quantity > 0) {
                item.quantity--
                notifyItemChanged(position)
            }
        }
        binding.addBtnLayout.setOnClickListener { _ ->
            if (item.quantity < item.dorm.bedsAvailable) {
                item.quantity++
                notifyItemChanged(position)
            }
        }
    }
}