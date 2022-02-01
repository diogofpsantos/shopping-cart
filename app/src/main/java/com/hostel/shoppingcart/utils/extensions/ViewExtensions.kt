package com.hostel.shoppingcart.utils.extensions

import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.databinding.ViewDataBinding

fun View.showInfoDialog(message: String?, clickListener: (() -> Unit)?) {
    if (message != null) {
        val builder = AlertDialog.Builder(this.context)
        builder.setTitle("Alert")
        builder.setMessage(message)
        builder.setPositiveButton(android.R.string.ok) { dialog, which ->
            clickListener!!.invoke()
        }

        builder.show()
    }
}

fun ViewDataBinding.showInfoDialog(message: String?, clickListener: (() -> Unit)?) =
    root.showInfoDialog(message, clickListener)
