package example.com.vestir.database.entity

import android.arch.persistence.room.Entity
import android.arch.persistence.room.Index

import android.arch.persistence.room.PrimaryKey

/**
 * Created by Nishant on 31-May-19.
 */

@Entity(indices = arrayOf(Index(value = ["name"], unique = true)))
class Client{
     @PrimaryKey()
     var clientid: Long = 1

     var name: String = ""
     var contact: Long = 0
     var address: String = ""
     var reference: String = ""
}