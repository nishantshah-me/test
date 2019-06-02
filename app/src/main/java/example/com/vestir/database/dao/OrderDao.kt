package example.com.vestir.database.dao

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*
import example.com.vestir.database.entity.ClientOrder

/**
 * Created by Nishant on 01-Jun-19.
 */
@Dao
interface OrderDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addOrder(order: ClientOrder)

    @Update
    fun updateOrder(order: ClientOrder)

    @Delete
    fun deleteOrder(order: ClientOrder)

    @Query("select * from ClientOrder")
    fun getOrderList(): LiveData<List<ClientOrder>?>

}