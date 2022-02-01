package com.hostel.shoppingcart.data.network

import com.hostel.shoppingcart.data.model.RatesResponse
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface StatsRetrofitService {
    @GET("stats")
    suspend fun pushStats(
        @Query("action") action: String,
        @Query("duration") duration: Long,
        @Query("status") status: String
    ): Response<ResponseBody>
}