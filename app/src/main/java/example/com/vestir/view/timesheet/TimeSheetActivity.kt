package example.com.vestir.view.timesheet

import android.app.DatePickerDialog
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.CardView
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import example.com.vestir.ORDER_DATE_FORMAT
import example.com.vestir.R
import example.com.vestir.database.entity.TimeSheet
import kotlinx.android.synthetic.main.activity_time_sheet.*
import kotlinx.android.synthetic.main.layout_toolbar.*
import kotlinx.android.synthetic.main.row_time_sheet.view.*
import java.text.SimpleDateFormat
import java.util.*


class TimeSheetActivity : AppCompatActivity() {
    private lateinit var view : View
    private var count = 0
    private val timeSheetData : MutableList<TimeSheet> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_time_sheet)
        addTimeSheet()

        img_back.setOnClickListener { onBackPressed() }

        imgAddTimeSheet.setOnClickListener {
            addTimeSheet()
        }

        etDate.setOnClickListener{

                try {
                    val sdf = SimpleDateFormat(ORDER_DATE_FORMAT)

                    showDatePicker()
                } catch (e: Exception) {


            }
        }
    }

    fun addTimeSheet(){
        val inflater = LayoutInflater.from(this)
        view =  inflater.inflate(R.layout.row_time_sheet,null)
        view.imgDelCard.tag = count++
        val params = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        view.isClickable = true

        params.topMargin = 16
        llTimeSheetRow.addView(view,params)

        view.imgDelCard.setOnClickListener {

            val parentView = ((it.parent as LinearLayout).parent as CardView).parent as LinearLayout
            val index = llTimeSheetRow.indexOfChild(parentView)
            llTimeSheetRow.removeViewAt(index)
        }
    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val datePickerDialog = DatePickerDialog(this@TimeSheetActivity,
                DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                    val cal = Calendar.getInstance()
                    cal.set(Calendar.YEAR, year)
                    cal.set(Calendar.MONTH, month)
                    cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                    try {
                        val sdf = SimpleDateFormat(ORDER_DATE_FORMAT)
                        etDate.text = sdf.format(cal.time)

                    } catch (e: Exception) {

                    }
                }, calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH))
        datePickerDialog.show()
    }
}
