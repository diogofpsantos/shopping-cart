package com.hostel.shoppingcart.data.network

import com.hostel.shoppingcart.BuildConfig
import com.hostel.shoppingcart.data.model.NetworkStatsResponse
import com.hostel.shoppingcart.utils.NoInternetException
import dagger.Module
import dagger.Provides
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


@Module
class NetworkModule {

    @Singleton
    @Provides
    fun provideInterceptor(): Interceptor {
        return Interceptor {
            val request = it.request()
            val startNs = System.nanoTime()
            val response: Response
            try {
                response = it.proceed(request)
            } catch (e: Exception) {
                throw NoInternetException(
                    "No internet connection", stats = NetworkStatsResponse(
                        TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNs),
                        "load-" + Base64.getEncoder()
                            .encodeToString(request.url.toString().toByteArray()), "NO_NETWORK",
                        false
                    )
                )
            }
//            val tookMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNs)
//            val responseBody = response.body!!
//            val obj =JSONObject()
//            obj.put("data", JSONObject(response.body!!.string()).toString())
//            var success : String? =null
//            success = if(response.isSuccessful){
//                "SUCCESS"
//            }else{
//                "ERROR"
//            }
//            var stats = JSONObject()
//            stats.put("duration", tookMs)
//            stats.put("action", request.url.toString())
//            stats.put("status", success)
//            obj.put("stats", stats.toString())
//            val contentType: MediaType? = response.body!!.contentType()
//            val body = obj.toString().toResponseBody(contentType)
//            val newResponse = response.newBuilder().body(body).build()
            return@Interceptor response
//            try {
//                val request = it.request()
//
//                        return@Interceptor it.proceed(it.request())
//
//            } catch (e: Exception) {
//                    throw NoInternetException(message = e.message, stats = NetworkStatsResponse(100, "aa", "asf"))
//            }
        }
    }

    @Singleton
    @Provides
    fun provideCurrencyRetrofitService(interceptor: Interceptor): CurrencyRetroFitService {
        val builder = Retrofit.Builder()
            .baseUrl("http://api.exchangeratesapi.io/v1/")
            .addConverterFactory(GsonConverterFactory.create())
        val client = OkHttpClient.Builder()
            .readTimeout(60000L, TimeUnit.MILLISECONDS).addInterceptor(interceptor)

        if (BuildConfig.DEBUG) {
            val logging = HttpLoggingInterceptor()
            logging.level = HttpLoggingInterceptor.Level.BODY
            client.addInterceptor(logging)
        }
        builder.client(client.build())
        return builder.build().create(CurrencyRetroFitService::class.java)
    }

    @Singleton
    @Provides
    fun provideStatsRetrofitService(interceptor: Interceptor): StatsRetrofitService {
        val builder = Retrofit.Builder()
            .baseUrl("https://gist.githubusercontent.com/ruimendesM/cb9313c4d4b3434975a3d7a6700d1787/raw/02d17a4c542ac99fe559df360cbfe9ba24dbe6be/")
            .addConverterFactory(GsonConverterFactory.create())
        val client = OkHttpClient.Builder()
            .readTimeout(60000L, TimeUnit.MILLISECONDS).addInterceptor(interceptor)

        if (BuildConfig.DEBUG) {
            val logging = HttpLoggingInterceptor()
            logging.level = HttpLoggingInterceptor.Level.BODY
            client.addInterceptor(logging)
        }
        builder.client(client.build())
        return builder.build().create(StatsRetrofitService::class.java)
    }
}