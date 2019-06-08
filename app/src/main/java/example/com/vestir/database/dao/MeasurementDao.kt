package example.com.vestir.database.dao

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import android.arch.persistence.room.Update
import example.com.vestir.database.entity.Measurement


@Dao
interface MeasurementDao {
    @Insert
    fun insertMeasurement(measurement: Measurement)

    @Update
    fun updateMeasurement(measurement: Measurement)

    @Query("Select * from Measurement WHERE clientId = :clientId")
    fun getMeasurementByClientId(clientId : Long) : LiveData<Measurement>

}