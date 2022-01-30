package com.hostel.shoppingcart.utils

sealed class Result<out R> {
    data class Success<out T>(val data: T) : Result<T>()
    data class Error(val exception: LocalisedException) : Result<Nothing>()
}