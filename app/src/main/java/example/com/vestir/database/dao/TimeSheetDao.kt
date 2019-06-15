package example.com.vestir.database.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Update
import example.com.vestir.database.entity.TimeSheet

@Dao
interface TimeSheetDao {
    @Insert
    fun insertTimeSheet(timeSheet: TimeSheet)

    @Update
    fun updateTimeSheet(timeSheet: TimeSheet)
}