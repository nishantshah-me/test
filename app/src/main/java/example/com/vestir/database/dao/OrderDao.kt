package example.com.vestir.database.dao

import android.arch.persistence.room.*
import example.com.vestir.database.entity.Order

/**
 * Created by Nishant on 01-Jun-19.
 */
@Dao
interface OrderDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertClient(order: Order)

    @Delete
    fun deleteOrder(order: Order)

    /*@Query("SELECT * FROM Order")
    fun getOrderList(): List<Order>*/
}