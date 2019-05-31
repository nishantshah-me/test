package example.com.vestir.database

import android.arch.persistence.room.Database

/**
 * Created by Nishant on 31-May-19.
 */

@Database(entities = arrayOf(Client::class),version = 1)
abstract class AppDatabase{

    abstract fun clientDao():ClientDao



}