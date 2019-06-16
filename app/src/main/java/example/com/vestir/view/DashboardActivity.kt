package example.com.vestir.view

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import example.com.vestir.R
import example.com.vestir.view.client.NewClientActivity
import example.com.vestir.view.clientorder.OrderListByCustomerActivity
import example.com.vestir.view.clientorder.OrderSummaryActivity
import example.com.vestir.view.timesheet.TimeSheetActivity
import example.com.vestir.view.transaction.TransactionActivity
import kotlinx.android.synthetic.main.activity_dashboard.*
import kotlinx.android.synthetic.main.layout_toolbar.*

class DashboardActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        img_back.visibility = View.GONE
        titleNewClient.setOnClickListener({
            startActivity(Intent(this, NewClientActivity::class.java))
        })

        titleNewOrder.setOnClickListener({
            startActivity(Intent(this, OrderListByCustomerActivity::class.java))
        })

        titleOrderSummary.setOnClickListener({
            startActivity(Intent(this, OrderSummaryActivity::class.java))
        })

        titleTimeSheet.setOnClickListener {
            startActivity(Intent(this,TimeSheetActivity::class.java))
        }

        titleTransaction.setOnClickListener {
            startActivity(Intent(this,TransactionActivity::class.java))
        }
    }
}
