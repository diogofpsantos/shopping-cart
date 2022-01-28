package com.hostel.shoppingcart.data.repositories

import com.hostel.shoppingcart.data.db.ShoppingCartDatabase
import com.hostel.shoppingcart.data.model.Dorm
import com.hostel.shoppingcart.di.scopes.ActivityScope
import javax.inject.Inject

@ActivityScope
class DormsRepository @Inject constructor(
    private val db: ShoppingCartDatabase
) {

    suspend fun getDorms(): List<Dorm> {
        var dorms = db.dormDao().getAllDorms()
        /*If database is empty I manually insert the dorms
        * This should ask server for the available dorms and update database
        * */

        if (dorms.isEmpty() || dorms.size != 3) {
            db.dormDao().clear()
            db.dormDao().insert(Dorm("6-bed dorm", 6, 6, 17.56))
            db.dormDao().insert(Dorm("8-bed dorm", 8, 8, 14.5))
            db.dormDao().insert(Dorm("12-bed dorm", 12, 12, 12.01))
            dorms = db.dormDao().getAllDorms()
        }
        return dorms
    }

}