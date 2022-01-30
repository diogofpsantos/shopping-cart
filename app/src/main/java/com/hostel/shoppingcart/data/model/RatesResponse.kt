package com.hostel.shoppingcart.data.model

import com.google.gson.annotations.SerializedName
import org.json.JSONObject

data class RatesResponse(
    @SerializedName("success") val success: Boolean,
    @SerializedName("timestamp") val timestamp: Long,
    @SerializedName("base") val base: String,
    @SerializedName("date") val date: String,
    @SerializedName("rates") val rates: Any
)