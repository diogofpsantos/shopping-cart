package com.hostel.shoppingcart.data.network

import com.hostel.shoppingcart.data.model.RatesResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface CurrencyRetroFitService {
    @GET("latest")
    suspend fun getLatestRates(
        @Query("access_key") accessKey: String
    ): Response<RatesResponse>
}