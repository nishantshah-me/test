package example.com.vestir.database

import android.arch.persistence.room.Entity
import android.arch.persistence.room.Index
import android.arch.persistence.room.PrimaryKey

/**
 * Created by Nishant on 31-May-19.
 */

@Entity(indices = arrayOf(Index(value = ["name"], unique = true)))
class Client{
    @PrimaryKey(autoGenerate = true)
    var clientId: Int = 0

     var name: String? = null
     var contact: Long? = null
     var address: String? = null
     var reference: String? = null
     var isMeasurementCorrect: Boolean = false
}