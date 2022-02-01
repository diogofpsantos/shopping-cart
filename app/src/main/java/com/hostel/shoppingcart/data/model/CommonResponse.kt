package com.hostel.shoppingcart.data.model

import com.google.gson.annotations.SerializedName

data class CommonResponse<T : Any, Y : Any>(
    @SerializedName("data") val data: T,
    @SerializedName("stats") val stats: Y,
)