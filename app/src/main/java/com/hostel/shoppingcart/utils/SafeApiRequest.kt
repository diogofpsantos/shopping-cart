package com.hostel.shoppingcart.utils

import com.hostel.shoppingcart.utils.extensions.localizedException
import org.json.JSONObject
import retrofit2.Response

abstract class SafeApiRequest {

    suspend fun <T : Any> apiRequest(call: suspend () -> Response<T>): Result<T> {
        return try {
            val response = call.invoke()
            if (response.isSuccessful)
                Result.Success(response.body()!!)
            else
                Result.Error(response.getApiException())
        } catch (e: Exception) {
            Result.Error(e.localizedException)
        }
    }

    suspend fun <T : Any> apiRequestWithException(call: suspend () -> Response<T>): T {
        return try {
            val response = call.invoke()
            if (response.isSuccessful)
                response.body()!!
            else
                throw response.getApiException()
        } catch (e: Exception) {
            throw e.localizedException
        }
    }

    private fun <T : Any> Response<T>.getApiException(): LocalisedException {
        val error = errorBody()?.string()
        val errorObj = JSONObject(error)
        if (errorObj.getJSONObject("error").getInt("code") == 104) {
            return MonthlyRequestException()
        }
        return ApiException(errorObj.getJSONObject("error").getString("info"))
    }
}