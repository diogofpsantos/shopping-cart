package com.hostel.shoppingcart.utils

import com.hostel.shoppingcart.data.model.NetworkStatsResponse
import java.io.IOException

/*Base Class for all localised Exceptions*/
open class LocalisedException(message: String?, var stats: NetworkStatsResponse) :
    IOException(message) {
}

class MonthlyRequestException(stats: NetworkStatsResponse) :
    LocalisedException(message = null, stats = stats)

class NoInternetException(message: String?, stats: NetworkStatsResponse) :
    LocalisedException(message = message, stats = stats)

class SomethingWentWrongException(message: String?, stats: NetworkStatsResponse) :
    LocalisedException(message, stats)

class ApiException(message: String?, stats: NetworkStatsResponse) :
    LocalisedException(message, stats)