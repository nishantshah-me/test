package example.com.vestir.database.entity

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey


@Entity
class TimeSheet{
    @PrimaryKey
    var timeSheetId : Long = 1
    var timeSheetDate : String = ""
    var staffName : String = ""
    var timeSheetHours : Int = 0
    @ColumnInfo(name = "clientName_order")
    var clientNameAndOrder : String = ""
    var description : String = ""
}