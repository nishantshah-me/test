package example.com.vestir.database.entity

import android.arch.persistence.room.Entity
import android.arch.persistence.room.ForeignKey
import android.arch.persistence.room.PrimaryKey


/*@Entity(foreignKeys = arrayOf(ForeignKey(entity = Client::class,
        parentColumns = arrayOf("clientid"),
        childColumns = arrayOf("clientId"),
        onDelete = ForeignKey.NO_ACTION)))*/
@Entity
class Measurement{
    @PrimaryKey(autoGenerate = true)
    var measurementId : Int = 0

    var clientId : Int = 0

    var width : Float? = null
    var  height : Float? = null

}