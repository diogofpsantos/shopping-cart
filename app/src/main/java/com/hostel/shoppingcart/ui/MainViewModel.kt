package com.hostel.shoppingcart.ui

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hostel.shoppingcart.data.Preferences
import com.hostel.shoppingcart.data.model.CartItem
import com.hostel.shoppingcart.data.model.CurrencyConversion
import com.hostel.shoppingcart.data.model.Dorm
import com.hostel.shoppingcart.data.model.DormItem
import com.hostel.shoppingcart.data.repositories.CheckoutRepository
import com.hostel.shoppingcart.data.repositories.DormsRepository
import com.hostel.shoppingcart.utils.LocalisedException
import com.hostel.shoppingcart.utils.Result
import com.hostel.shoppingcart.utils.SingleLiveEvent
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.kotlin.subscribeBy
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val dormsRepository: DormsRepository,
    private val checkoutRepository: CheckoutRepository,
    private val preferences: Preferences
) : ViewModel() {
    val cartItems = MutableLiveData<MutableList<CartItem>>()
    val loading = MutableLiveData(true)
    val errorEvent = SingleLiveEvent<LocalisedException>()
    val totalPrice = MutableLiveData<Double>()
    val totalBeds = MutableLiveData<Int>()
    val dorms = ArrayList<DormItem>()
    var currencies = ArrayList<CurrencyConversion>()

    init {
        getDormItemsList()
        cartItems.observeForever(::updateTotalPrice)
    }

    private fun getDormItemsList() = viewModelScope.launch {
        loading.postValue(true)
        dormsRepository.getDorms().forEach {
            dorms.add(DormItem(it, 0, getDefaultCurrency()!!))
        }
//        val items = mutableListOf<CartItem>()
//        dormsRepository.getDorms().forEach {
//            items.add(CartItem(it, 0))
//        }
//        cartItems.postValue(items)
        loading.postValue(false)
    }

    fun addBedToCart(dorm: Dorm) {
        val currentList = cartItems.value ?: mutableListOf()
        val found = currentList.find { it.dorm.id == dorm.id }
        if (found != null) {
            found.quantity++
        } else {
            currentList.add(CartItem(dorm = dorm, quantity = 1))
        }
        cartItems.value = currentList
    }

    fun removeBedFromCart(id: Long) {
        val currentList = cartItems.value ?: mutableListOf()
        val found = currentList.find { it.dorm.id == id }
        if (found != null) {
            found.quantity--
            if (found.quantity == 0)
                currentList.remove(found)
        }
        cartItems.value = currentList
    }

    fun clearCart() {
        cartItems.value = mutableListOf()
    }

    private fun updateTotalPrice(items: List<CartItem>) {
        //RxJava
        var price = 0.0
        var quantity = 0
        Observable.create<CartItem> { emitter ->
            if (items.isNotEmpty()) {
                items.forEach {
                    emitter.onNext(it)
                }
                emitter.onComplete()
            } else {
                emitter.onError(RuntimeException("error"))
            }
        }.subscribeBy(
            onNext = {
                price += it.dorm.price * it.quantity
                quantity += it.quantity
            },
            onComplete = {
                totalPrice.postValue(price)
                totalBeds.postValue(quantity)
            },
            onError = {
                println(it)
                totalPrice.postValue(0.0)
                totalBeds.postValue(0)
            }
        )
//        if (items.isNotEmpty()) {
//            var price = 0.0
//            items.forEach {
//                price += it.dorm.price * it.quantity
//            }
//            var quantityCart = 0
//            items.forEach { quantityCart += it.quantity }
//            totalPrice.value = price
//            totalPrice.postValue(totalPrice.value)
//        } else
//            totalPrice.postValue(MutableLiveData<Double>().value)
    }

    fun getDefaultCurrency() = preferences.defaultCurrency

    fun getCurrencies() = viewModelScope.launch {
        loading.postValue(true)
        when (val result = checkoutRepository.getCurrencies()) {
            is Result.Success -> {
                currencies = result.data
                loading.postValue(false)
            }
            is Result.Error -> {
                errorEvent.postValue(result.exception)
            }
        }
    }
}