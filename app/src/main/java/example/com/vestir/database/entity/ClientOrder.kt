package example.com.vestir.database.entity

import android.arch.persistence.room.Entity
import android.arch.persistence.room.ForeignKey
import android.arch.persistence.room.PrimaryKey
import java.io.Serializable

/**
 * Created by Nishant on 01-Jun-19.
 */
@Entity
class ClientOrder : Serializable {
    @PrimaryKey(autoGenerate = true)
    var orderId: Long = 0

    var clientId: Long = 0
    var name: String = ""
    var style: String = ""
    var description: String = ""
    var status: String = ""
    var order: String = ""
    var trial: String = ""
    var delivery: String = ""
    var reference: String = "ok"
    var final: String = "ok"
    var quote: Long = 0
    var cost: Long = 0
    var advance: Long = 0
    var costByHours : Long = 0
    var manual : Long = 0
    var expenses : Long = 0
}