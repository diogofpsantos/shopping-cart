package com.hostel.shoppingcart.utils.extensions

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.fragment.app.Fragment
import com.hostel.shoppingcart.data.model.NetworkStatsResponse
import com.hostel.shoppingcart.utils.LocalisedException
import com.hostel.shoppingcart.utils.NoInternetException
import com.hostel.shoppingcart.utils.SomethingWentWrongException
import java.io.IOException

fun Fragment.isNetworkAvailable() = context?.isNetworkAvailable() ?: false

fun Context.isNetworkAvailable(): Boolean {
    val connectivityManager =
        getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        val nw = connectivityManager.activeNetwork ?: return false
        val actNw = connectivityManager.getNetworkCapabilities(nw) ?: return false
        return when {
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            //for other device how are able to connect with Ethernet
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
//                //for check internet over Bluetooth
//                actNw.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH) -> true
            else -> false
        }
    } else {
        val nwInfo = connectivityManager.activeNetworkInfo ?: return false
        return nwInfo.isConnected
    }
}

val Exception?.localizedException: LocalisedException
    get() {
        return when (this) {
            is LocalisedException -> this
            is IOException -> NoInternetException(
                message, NetworkStatsResponse(100, "test", "test", false)
            )
            else -> SomethingWentWrongException(
                this?.localizedMessage,
                NetworkStatsResponse(100, "test", "test", false)
            )
        }
    }
