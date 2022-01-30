package com.hostel.shoppingcart.data.network

import com.hostel.shoppingcart.BuildConfig
import dagger.Module
import dagger.Provides
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
class NetworkModule {

    @Singleton
    @Provides
    fun provideCurrencyRetrofitService(): CurrencyRetroFitService{
        val builder = Retrofit.Builder()
            .baseUrl("http://api.exchangeratesapi.io/v1/")
            .addConverterFactory(GsonConverterFactory.create())
        val client = OkHttpClient.Builder()
            .readTimeout(60000L, TimeUnit.MILLISECONDS)

        if (BuildConfig.DEBUG) {
            val logging = HttpLoggingInterceptor()
            logging.level = HttpLoggingInterceptor.Level.BODY
            client.addInterceptor(logging)
        }
        builder.client(client.build())
        return builder.build().create(CurrencyRetroFitService::class.java)
    }
}