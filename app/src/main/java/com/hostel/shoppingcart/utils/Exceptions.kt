package com.hostel.shoppingcart.utils

import androidx.annotation.StringRes

/*Base Class for all localised Exceptions*/
open class LocalisedException(message: String?) : Exception(message)

class MonthlyRequestException : LocalisedException(null)
class NoInternetException(message: String?) : LocalisedException(message)
class SomethingWentWrongException(message: String?) : LocalisedException(message)

class ApiException(message: String?) : LocalisedException(message)