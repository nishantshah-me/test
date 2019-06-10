package example.com.vestir.database.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import example.com.vestir.database.entity.Cost

@Dao
interface CostDao {

    @Insert
    fun insertCost(cost : Cost)
}