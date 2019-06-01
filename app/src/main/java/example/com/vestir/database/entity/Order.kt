package example.com.vestir.database.entity

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

/**
 * Created by Nishant on 01-Jun-19.
 */
@Entity
class Order {
    @PrimaryKey(autoGenerate = true)
    var orderId: Int = 0

    var clientName: String = ""

    var name: String = ""
    var style: String = ""
    var description: String = ""
    var status: String = ""
    var order: String = ""
    var trial: String = ""
    var delivery: String = ""
    var reference: String = "ok"
    var final: String = "ok"
    var quote: String = ""
    var cost: String = "cost"
}