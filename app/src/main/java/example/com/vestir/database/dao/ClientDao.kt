package example.com.vestir.database.dao

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.persistence.room.*

import example.com.vestir.database.entity.Client

/**
 * Created by Nishant on 31-May-19.
 */
@Dao
interface ClientDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertClient(client: Client)

    @Query("Select * from Client")
    fun getAllClient() : LiveData<List<Client>>

    @Update
    fun updateClient(client : Client?)
}