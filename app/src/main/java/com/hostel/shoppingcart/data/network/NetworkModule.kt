package com.hostel.shoppingcart.data.network

import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class NetworkModule {

    @Singleton
    @Provides
    fun provideCurrencyRetrofitService(): CurrencyRetroFitService{
        val builder = Retrofit.Builder()
            .baseUrl("https://exchangeratesapi.io/")
            .addConverterFactory(GsonConverterFactory.create())
        val client = OkHttpClient.Builder()
//        if (BuildConfig.DEBUG) {
//            val logging = HttpLoggingInterceptor()
//            logging.level = HttpLoggingInterceptor.Level.BODY
//            client.addInterceptor(logging)
//        }
        builder.client(client.build())
        return builder.build().create(CurrencyRetroFitService::class.java)
    }
}