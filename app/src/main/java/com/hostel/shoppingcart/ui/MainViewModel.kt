package com.hostel.shoppingcart.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hostel.shoppingcart.data.model.CartItem
import com.hostel.shoppingcart.data.repositories.DormsRepository
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val dormsRepository: DormsRepository
) : ViewModel() {
    val cartItems = MutableLiveData<MutableList<CartItem>>()
    val loading = MutableLiveData(true)
    val totalPrice = MutableLiveData<Double>()

    val subscriptions = CompositeDisposable()

    init {
        getCartItemsList()
        cartItems.observeForever(::updateTotalPrice)
    }

    private fun getCartItemsList() = viewModelScope.launch {
        loading.postValue(true)
        val items = mutableListOf<CartItem>()
        dormsRepository.getDorms().forEach {
            items.add(CartItem(it, 0))
        }
        cartItems.postValue(items)
        loading.postValue(false)
    }

    fun addBedToCart(cartItem: CartItem) {
        val currentList = cartItems.value ?: mutableListOf()
        val found = currentList.find { it.dorm.id == cartItem.dorm.id }
        if (found != null) {
            found.quantity++
        }
        cartItems.value = currentList
    }

    fun removeBedFromCart(id: Long) {
        val currentList = cartItems.value ?: mutableListOf()
        val found = currentList.find { it.dorm.id == id }
        if (found != null)
            found.quantity--
        cartItems.value = currentList
    }

    fun clearCart() {
        cartItems.value = mutableListOf()
    }

    private fun updateTotalPrice(items: List<CartItem>) {
        if (items.isNotEmpty()) {
            var price = 0.0
            items.forEach {
                price += it.dorm.price * it.quantity
            }
            var quantityCart = 0
            items.forEach { quantityCart += it.quantity }
            totalPrice.value = price
            totalPrice.postValue(totalPrice.value)
        } else
            totalPrice.postValue(MutableLiveData<Double>().value)
    }
}