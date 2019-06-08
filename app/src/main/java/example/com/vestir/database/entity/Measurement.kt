package example.com.vestir.database.entity

import android.arch.persistence.room.Entity
import android.arch.persistence.room.ForeignKey
import android.arch.persistence.room.PrimaryKey


@Entity
class Measurement{
    @PrimaryKey
    var clientId : Long = 1

    var full_length : Float? = null
    var dress_length : Float? = null
    var blouse_length : Float? = null
    var bust_point : Float? = null
    var under_bust_length : Float? = null
    var yoke_length : Float? = null
    var waist : Float? =null
    var low_waist : Float? = null
    var hip :Float? = null
    var shoulder :Float? = null
    var chest : Float? = null

    var arm_hole : Float? = null
    var cross_front : Float? = null
    var cross_back : Float? = null
    var biceps : Float ? = null
    var short_sleeve_length : Float? = null
    var cuff1 : Float? = null
    var elbow_sleeve_length : Float? = null
    var cuff2 : Float? = null

    var neck_low : Float? = null
    var back_low : Float?= null
    var pant_length : Float? =null
    var pant_waist :Float? = null
    var inseam_length: Float? = null
    var skirt_length : Float? = null
    var skirt_waist : Float? = null
    var thigh : Float? = null
    var knee : Float? = null
    var bottom : Float? = null

}