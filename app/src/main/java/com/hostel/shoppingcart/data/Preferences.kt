package com.hostel.shoppingcart.data

import android.content.Context
import androidx.preference.PreferenceManager
import com.hostel.shoppingcart.utils.Constants
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Preferences @Inject constructor(private val context: Context) {
    private val prefManager by lazy { PreferenceManager.getDefaultSharedPreferences(context) }

    var currencies
        get() = prefManager.getString(Constants.CURRENCIES, null)
        set(value) = prefManager.edit().putString(Constants.CURRENCIES, value).apply()

    var lastSync
        get() = prefManager.getLong(Constants.LAST_SYNC, -1)
        set(value) = prefManager.edit().putLong(Constants.LAST_SYNC, value).apply()

    var defaultCurrency
        get() = prefManager.getString(Constants.DEFAULT_CURRENCY, "USD")
        set(value) = prefManager.edit().putString(Constants.DEFAULT_CURRENCY, value).apply()
}