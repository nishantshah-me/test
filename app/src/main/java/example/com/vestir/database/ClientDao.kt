package example.com.vestir.database

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert

/**
 * Created by Nishant on 31-May-19.
 */
@Dao
interface ClientDao {
    @Insert
    fun insertClient(client: Client)
}