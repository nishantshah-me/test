package example.com.vestir.database

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import example.com.vestir.database.dao.OrderDao
import example.com.vestir.database.entity.Order

/**
 * Created by Nishant on 31-May-19.
 */

@Database(entities = arrayOf(Client::class, Order::class),version = 1)
abstract class AppDatabase: RoomDatabase(){

    abstract fun clientDao():ClientDao
    abstract fun orderDao(): OrderDao

}