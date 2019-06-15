package example.com.vestir.view.timesheet

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.CardView
import android.view.LayoutInflater
import android.view.View
import example.com.vestir.R
import kotlinx.android.synthetic.main.activity_time_sheet.*
import kotlinx.android.synthetic.main.layout_toolbar.*
import android.view.ViewGroup
import android.widget.LinearLayout
import kotlinx.android.synthetic.main.row_time_sheet.*
import kotlinx.android.synthetic.main.row_time_sheet.view.*


class TimeSheetActivity : AppCompatActivity() {
    private lateinit var view : View
    private var count = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_time_sheet)
        addTimeSheet()

        img_back.setOnClickListener { onBackPressed() }

        imgAddTimeSheet.setOnClickListener {
            addTimeSheet()
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

        view.imgDelCard.setOnClickListener { it ->
            /*for (i in 0 until llTimeSheetRow.childCount){
                if( i == view.imgDelCard.tag){
                    llTimeSheetRow.removeView(llTimeSheetRow.getChildAt(i))
                    count--
                }
            }*/
            val parentView = ((it.parent as LinearLayout).parent as CardView).parent as LinearLayout
            val index = llTimeSheetRow.indexOfChild(parentView)
            llTimeSheetRow.removeViewAt(index)
        }
    }
}
