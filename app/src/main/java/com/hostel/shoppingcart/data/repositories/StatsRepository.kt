package com.hostel.shoppingcart.data.repositories

import com.hostel.shoppingcart.data.db.ShoppingCartDatabase
import com.hostel.shoppingcart.data.model.NetworkStatsResponse
import com.hostel.shoppingcart.data.network.StatsRetrofitService
import com.hostel.shoppingcart.di.scopes.ActivityScope
import com.hostel.shoppingcart.utils.Result
import com.hostel.shoppingcart.utils.SafeApiRequest
import com.hostel.shoppingcart.utils.extensions.localizedException
import javax.inject.Inject

@ActivityScope
class StatsRepository @Inject constructor(
    private val db: ShoppingCartDatabase,
    private val stats: StatsRetrofitService,
) : SafeApiRequest(){
    suspend fun getStatsToSync() : List<NetworkStatsResponse>{
        return db.statsDao().getAllStatsToSync()
    }

    suspend fun updateStatsEntry(id:Long, state:Boolean){
        db.statsDao().updateStatsSynced(id, state)
    }

    suspend fun addStats(statsResponse: NetworkStatsResponse){
        db.statsDao().insert(statsResponse)
    }

    suspend fun pushStats(statsResponse: NetworkStatsResponse): Result<NetworkStatsResponse> {
        return try {
            statsRequestWithException {
                val action = "load-" + statsResponse.action
                stats.pushStats(
                    action,
                    statsResponse.duration,
                    statsResponse.status
                )
            }
            statsResponse.synced = true
            Result.Success(statsResponse)
        } catch (e: Exception) {
            Result.Error(e.localizedException)
        }
    }

}