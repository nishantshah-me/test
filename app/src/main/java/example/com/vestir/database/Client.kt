package example.com.vestir.database

import android.arch.persistence.room.PrimaryKey

/**
 * Created by Nishant on 31-May-19.
 */
class Client{
    @PrimaryKey(autoGenerate = true)
    var clientId: Int = 0

     var name: String? = null
     var contact: String? = null
     var address: String? = null
     var Reference: String? = null
     var isMeasurementCorrect: Boolean = false
}