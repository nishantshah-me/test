package example.com.vestir.database

import android.arch.persistence.room.Insert

/**
 * Created by Nishant on 31-May-19.
 */
interface ClientDao {
    @Insert
    fun insertClient(client: Client)
}