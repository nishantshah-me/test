package example.com.vestir.database

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context
import example.com.vestir.database.dao.ClientDao
import example.com.vestir.database.dao.CostDao
import example.com.vestir.database.dao.MeasurementDao
import example.com.vestir.database.dao.OrderDao
import example.com.vestir.database.entity.Client
import example.com.vestir.database.entity.ClientOrder
import example.com.vestir.database.entity.Cost
import example.com.vestir.database.entity.Measurement

/**
 * Created by Nishant on 31-May-19.
 */

@Database(entities = [(Client::class), (ClientOrder::class),(Measurement::class),(Cost::class)],version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun clientDao(): ClientDao

    abstract fun orderDao(): OrderDao

    abstract fun measurementDao(): MeasurementDao

    abstract fun costDao() : CostDao

    companion object {
        fun getInstance(context : Context):AppDatabase{
            val appDatabase = Room.databaseBuilder(context,AppDatabase::class.java,"vestir-table")
                    .allowMainThreadQueries().build()
            return appDatabase
        }

    }


}