package example.com.vestir.view.client

import android.app.Activity
import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import com.google.gson.Gson
import example.com.vestir.R
import example.com.vestir.database.AppDatabase
import example.com.vestir.database.entity.Measurement
import kotlinx.android.synthetic.main.activity_measurement.*
import kotlinx.android.synthetic.main.layout_toolbar.*

class MeasurementActivity : AppCompatActivity() {
    private var measurement = Measurement()
    val CLIENT_ID = "clientId"
    var clientId: Long = 1
    val MEASUREMENT = "measurement"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_measurement)

        if (intent.hasExtra(CLIENT_ID))
            clientId = intent.getLongExtra(CLIENT_ID, 0)

        AppDatabase.getInstance(this).measurementDao()
                .getMeasurementByClientId(clientId).observe(this, Observer {
            if (it != null)
                updateUi(it)
        })

        img_back.setOnClickListener { onBackPressed() }

        btnSubmit.setOnClickListener {
            setData()
            finish()
        }

    }

    private fun updateUi(it: Measurement?) {
        if (it?.full_length != null) etFL.setText(it.full_length.toString())
        if (it?.dress_length != null) etDL.setText(it.dress_length.toString())
        if (it?.blouse_length != null) etBL.setText(it.blouse_length.toString())
        if (it?.bust_point != null) etBP.setText(it.bust_point.toString())
        if (it?.under_bust_length != null) etUBL.setText(it.under_bust_length.toString())
        if (it?.yoke_length != null) etYL.setText(it.yoke_length.toString())
        if (it?.waist != null) etW.setText(it.waist.toString())
        if (it?.low_waist != null) etLW.setText(it.low_waist.toString())
        if (it?.hip != null) etH.setText(it.hip.toString())
        if (it?.shoulder != null) etSh.setText(it.shoulder.toString())
        if (it?.chest != null) etCh.setText(it.chest.toString())
        if (it?.arm_hole != null) etAH.setText(it.arm_hole.toString())
        if (it?.cross_front != null) etXF.setText(it.cross_front.toString())
        if (it?.cross_back != null) etXB.setText(it.cross_back.toString())
        if (it?.biceps != null) etBi.setText(it.biceps.toString())
        if (it?.short_sleeve_length != null) etSL1.setText(it.short_sleeve_length.toString())
        if (it?.cuff1 != null) etCuf1.setText(it.cuff1.toString())
        if (it?.elbow_sleeve_length != null) etSL2.setText(it.elbow_sleeve_length.toString())
        if (it?.cuff2 != null) etCuf2.setText(it.cuff2.toString())
        if (it?.neck_low != null) etNL.setText(it.neck_low.toString())
        if (it?.back_low != null) etBKL.setText(it.back_low.toString())
        if (it?.pant_length != null) etPL.setText(it.pant_length.toString())
        if (it?.pant_waist != null) etPW.setText(it.pant_waist.toString())
        if (it?.inseam_length != null) etIL.setText(it.inseam_length.toString())
        if (it?.skirt_length != null) etSL.setText(it.skirt_length.toString())
        if (it?.skirt_waist != null) etSW.setText(it.skirt_waist.toString())
        if (it?.thigh != null) etTh.setText(it.thigh.toString())
        if (it?.knee != null) etKn.setText(it.knee.toString())
        if (it?.bottom != null) etBt.setText(it.bottom.toString())

    }

    private fun setData() {
        if (!TextUtils.isEmpty(etFL.text)) measurement.full_length = etFL.text.toString().toFloat()

        if (!TextUtils.isEmpty(etDL.text)) measurement.dress_length = etDL.text.toString().toFloat()

        if (!TextUtils.isEmpty(etBL.text)) measurement.blouse_length = etBL.text.toString().toFloat()

        if (!TextUtils.isEmpty(etBP.text)) measurement.bust_point = etBP.text.toString().toFloat()

        if (!TextUtils.isEmpty(etUBL.text)) measurement.under_bust_length = etUBL.text.toString().toFloat()

        if (!TextUtils.isEmpty(etYL.text)) measurement.yoke_length = etYL.text.toString().toFloat()

        if (!TextUtils.isEmpty(etW.text)) measurement.waist = etW.text.toString().toFloat()

        if (!TextUtils.isEmpty(etLW.text)) measurement.low_waist = etLW.text.toString().toFloat()

        if (!TextUtils.isEmpty(etH.text)) measurement.hip = etH.text.toString().toFloat()

        if (!TextUtils.isEmpty(etSh.text)) measurement.shoulder = etSh.text.toString().toFloat()

        if (!TextUtils.isEmpty(etCh.text)) measurement.chest = etCh.text.toString().toFloat()

        if (!TextUtils.isEmpty(etAH.text)) measurement.arm_hole = etAH.text.toString().toFloat()

        if (!TextUtils.isEmpty(etXF.text)) measurement.cross_front = etXF.text.toString().toFloat()

        if (!TextUtils.isEmpty(etXB.text)) measurement.cross_back = etXB.text.toString().toFloat()

        if (!TextUtils.isEmpty(etBi.text)) measurement.biceps = etBi.text.toString().toFloat()

        if (!TextUtils.isEmpty(etSL1.text)) measurement.short_sleeve_length = etSL1.text.toString().toFloat()

        if (!TextUtils.isEmpty(etCuf1.text)) measurement.cuff1 = etCuf1.text.toString().toFloat()

        if (!TextUtils.isEmpty(etSL2.text)) measurement.elbow_sleeve_length = etSL2.text.toString().toFloat()

        if (!TextUtils.isEmpty(etCuf2.text)) measurement.cuff2 = etCuf2.text.toString().toFloat()

        if (!TextUtils.isEmpty(etNL.text)) measurement.neck_low = etNL.text.toString().toFloat()

        if (!TextUtils.isEmpty(etBKL.text)) measurement.back_low = etBKL.text.toString().toFloat()

        if (!TextUtils.isEmpty(etPL.text)) measurement.pant_length = etPL.text.toString().toFloat()

        if (!TextUtils.isEmpty(etPW.text)) measurement.pant_waist = etPW.text.toString().toFloat()

        if (!TextUtils.isEmpty(etIL.text)) measurement.inseam_length = etIL.text.toString().toFloat()

        if (!TextUtils.isEmpty(etSL.text)) measurement.skirt_length = etSL.text.toString().toFloat()

        if (!TextUtils.isEmpty(etSW.text)) measurement.skirt_waist = etSW.text.toString().toFloat()

        if (!TextUtils.isEmpty(etTh.text)) measurement.thigh = etTh.text.toString().toFloat()

        if (!TextUtils.isEmpty(etKn.text)) measurement.knee = etKn.text.toString().toFloat()

        if (!TextUtils.isEmpty(etBt.text)) measurement.bottom = etBt.text.toString().toFloat()

        val intent = intent
        intent.putExtra(MEASUREMENT, Gson().toJson(measurement))
        if(clientId > 1){
            measurement.clientId = clientId
            AppDatabase.getInstance(this).measurementDao().updateMeasurement(measurement)
        }
        setResult(Activity.RESULT_OK, intent)

    }

}
