package com.hostel.shoppingcart.data.model

import com.google.gson.annotations.SerializedName

data class CurrencyConversion(
    @SerializedName("currency") val currency: String,
    @SerializedName("conversion") var conversion: Double
)
