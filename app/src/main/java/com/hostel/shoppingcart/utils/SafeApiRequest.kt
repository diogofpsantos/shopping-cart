package com.hostel.shoppingcart.utils

import com.hostel.shoppingcart.data.model.NetworkStatsResponse
import com.hostel.shoppingcart.utils.extensions.localizedException
import org.greenrobot.eventbus.EventBus
import org.json.JSONObject
import retrofit2.Response
import java.util.*

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
            val networkStatsResponse = NetworkStatsResponse(
                response.raw().receivedResponseAtMillis - response.raw().sentRequestAtMillis,
                "load-" + Base64.getEncoder()
                    .encodeToString(response.raw().request.url.toString().toByteArray()),
                if (response.isSuccessful) "SUCCESS" else "ERROR", false
            )
            EventBus.getDefault().postSticky(networkStatsResponse.getIntent())
            if (response.isSuccessful) {
                response.body()!!
            } else {
                throw response.getApiException()
            }

        } catch (e: Exception) {
            throw e.localizedException
        }
    }

    suspend fun <T : Any> statsRequestWithException(call: suspend () -> Response<T>): T {
        return try {
            val response = call.invoke()
            if (response.isSuccessful) {
                response.body()!!
            } else {
                throw response.getApiException()
            }

        } catch (e: Exception) {
            throw e.localizedException
        }
    }

    private fun <T : Any> Response<T>.getApiException(): LocalisedException {
        val error = errorBody()?.string()
        val errorObj = JSONObject(error)
        val statsResponse = NetworkStatsResponse(
            raw().receivedResponseAtMillis - raw().sentRequestAtMillis,
            "load-" + Base64.getEncoder()
                .encodeToString(raw().request.url.toString().toByteArray()),
            "ERROR", false
        )
        if (errorObj.getJSONObject("error").getInt("code") == 104) {
            return MonthlyRequestException(statsResponse)
        }
        return ApiException(errorObj.getJSONObject("error").getString("info"), statsResponse)
    }
}