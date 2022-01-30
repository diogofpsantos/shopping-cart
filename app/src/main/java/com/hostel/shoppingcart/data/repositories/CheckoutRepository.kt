package com.hostel.shoppingcart.data.repositories

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.hostel.shoppingcart.data.Preferences
import com.hostel.shoppingcart.data.model.CurrencyConversion
import com.hostel.shoppingcart.data.network.CurrencyRetroFitService
import com.hostel.shoppingcart.di.scopes.ActivityScope
import com.hostel.shoppingcart.utils.API_TOKEN
import com.hostel.shoppingcart.utils.Result
import com.hostel.shoppingcart.utils.SafeApiRequest
import com.hostel.shoppingcart.utils.extensions.localizedException
import org.json.JSONObject
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject


@ActivityScope
class CheckoutRepository @Inject constructor(
    private val api: CurrencyRetroFitService,
    private val preferences: Preferences,
    private val gson: Gson
) : SafeApiRequest() {
    private var currenciesList: ArrayList<CurrencyConversion>? = null
    private var currenciesTime: Date? = null

    suspend fun getCurrencies(): Result<ArrayList<CurrencyConversion>> {
        return try {
            currenciesList.let { list ->
                //Check if already has list and it has not passed 60 minutes
                if (list != null && TimeUnit.MINUTES.convert(
                        System.currentTimeMillis() - currenciesTime!!.time,
                        TimeUnit.MILLISECONDS
                    ) < 60
                ) {
                    Result.Success(list)
                } else {
                    //Check if there is already currencies in the preferences and needs to be updated
                    val localResponse = preferences.currencies
                    val lastSync = preferences.lastSync
                    val timeDiff = TimeUnit.MINUTES.convert(
                        System.currentTimeMillis() - lastSync,
                        TimeUnit.MILLISECONDS
                    )
                    if (localResponse != null && timeDiff < 60) {
                        val typeToken = object : TypeToken<List<CurrencyConversion>>() {}.type
                        currenciesList = gson.fromJson(localResponse, typeToken)
                        currenciesTime = Date(lastSync)
                        Result.Success(currenciesList!!)
                    } else {
                        // Get currencies from API
                        val currenciesResponse = apiRequestWithException {
                            api.getLatestRates(
                                API_TOKEN
                            )
                        }
                        val currencies = ArrayList<CurrencyConversion>()
                        val ratesObj = JSONObject(currenciesResponse.rates.toString())
                        val keys: Iterator<String> = ratesObj.keys()
                        var usdConversion: CurrencyConversion? = null

                        while (keys.hasNext()) {
                            val key = keys.next()
                            if (ratesObj.get(key) is Double) {
                                val currencyConversion =
                                    CurrencyConversion(key, ratesObj.getDouble(key))
                                currencies.add(currencyConversion)
                                if (key.equals("USD", true)) {
                                    usdConversion = currencyConversion
                                }
                            }
                        }
                        if (usdConversion != null) {
                            currencies.forEach {
                                //Here I make the conversion to USD
                                it.conversion = it.conversion / usdConversion.conversion
                            }
                        }
                        currenciesList = currencies
                        currenciesTime = Date(currenciesResponse.timestamp * 1000)
                        preferences.lastSync = currenciesResponse.timestamp * 1000
                        preferences.currencies = gson.toJson(currencies)
                        Result.Success(currenciesList!!)
                    }
                }

            }
        } catch (e: Exception) {
            Result.Error(e.localizedException)
        }
    }

}