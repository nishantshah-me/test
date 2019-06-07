package example.com.vestir.view.client

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import example.com.vestir.R
import example.com.vestir.database.entity.Measurement
import kotlinx.android.synthetic.main.activity_measurement.*
import kotlinx.android.synthetic.main.layout_toolbar.*

class MeasurementActivity : AppCompatActivity() {
   private var measurement  = Measurement()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_measurement)
        img_back.setOnClickListener { onBackPressed() }

        btnSubmit.setOnClickListener {
            measurement.full_length = etFL.text.toString().toFloat()
        }

    }
}
