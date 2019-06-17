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

    @Query("select * from ClientOrder where status = :orderStatus")
    fun getOrderListBasedOnStatus(orderStatus: String): LiveData<List<ClientOrder>?>

    @Query("select * from ClientOrder where clientId = :clientID")
    fun getOrderListBasedOnClient(clientID: Long): LiveData<List<ClientOrder>?>

    @Query("select * from ClientOrder where clientId = :clientID and status = :orderStatus")
    fun getOrderListBasedOnClientAndStatus(clientID: Long, orderStatus: String): LiveData<List<ClientOrder>?>

    @Query("select * from ClientOrder where clientId = :clientID and style = :orderStyle")
    fun getOrderBasedOnClientAndStyle(clientID: Long, orderStyle: String): ClientOrder?

    @Query("select count(*) from ClientOrder where clientId = :clientID and status = :orderStatus")
    fun getOrderListCountByClientAndStatus(clientID: Long, orderStatus: String): LiveData<Int>

    @Query("select count(*) from ClientOrder where status = :orderStatus")
    fun getOrderListCountBasedOnStatus(orderStatus: String): LiveData<Int>

    @Query("select count(*) from ClientOrder")
    fun getOrderListCount(): LiveData<Int>
}