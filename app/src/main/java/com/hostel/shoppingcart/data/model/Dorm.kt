package com.hostel.shoppingcart.data.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "dorms")
@Parcelize
data class Dorm(
    @SerializedName("name") val name: String,
    @SerializedName("beds") val beds: Int,
    @SerializedName("bedsAvailable") val bedsAvailable: Int,
    @SerializedName("price") val price: Double,
) : Parcelable {
    @PrimaryKey(autoGenerate = true)
    var id: Long? = null
}
