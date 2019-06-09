package example.com.vestir.database.entity

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey


@Entity
class Cost{
    @PrimaryKey(autoGenerate = true)
    var costId : Long = 0

    var orderTimeTaken : String = ""
    var orderExpenses : Long = 0
    var orderManualAdd : String = ""
}