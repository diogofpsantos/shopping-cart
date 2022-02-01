package com.hostel.shoppingcart.data.model

import android.content.Intent
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import org.json.JSONObject

@Entity(tableName = "stats")
data class NetworkStatsResponse(
    @SerializedName("duration") val duration: Long,
    @SerializedName("action") val action: String,
    @SerializedName("status") val status: String,
    var synced: Boolean
) {
    @PrimaryKey(autoGenerate = true)
    var id: Long? = null

    fun getIntent(): Intent {
        val intent = Intent()
        val obj = JSONObject()
        obj.put("duration", duration)
        obj.put("action", action)
        obj.put("status", status)
        obj.put("synced", synced)
        intent.putExtra("data", obj.toString())
        return intent
    }

    companion object {
        fun parseIntent(intent: Intent): NetworkStatsResponse {
            val obj = JSONObject(intent.getStringExtra("data")!!)
            return NetworkStatsResponse(
                obj.getLong("duration"),
                obj.getString("action"), obj.getString("status"),
                obj.getBoolean("synced")
            )
        }
    }
}