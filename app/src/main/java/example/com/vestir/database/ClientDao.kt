package example.com.vestir.database

import android.arch.lifecycle.LiveData

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query

/**
 * Created by Nishant on 31-May-19.
 */
@Dao
interface ClientDao {
    @Insert
    fun insertClient(client: Client?)

    @Query("Select * from Client")
    fun getAllClient() : LiveData<List<Client>>
}