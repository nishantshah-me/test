package example.com.vestir.view.timesheet

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import example.com.vestir.R
import kotlinx.android.synthetic.main.layout_toolbar.*

class TimeSheetActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_time_sheet)

        img_back.setOnClickListener { onBackPressed() }
    }
}
