package example.com.vestir.view.clientorder

import android.arch.lifecycle.Observer
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import example.com.vestir.IS_FROM_ORDER_SUMMARY
import example.com.vestir.ORDER_STATUS
import example.com.vestir.R
import example.com.vestir.database.AppDatabase
import example.com.vestir.database.entity.ClientOrder
import kotlinx.android.synthetic.main.activity_order_list_by_customer.*
import kotlinx.android.synthetic.main.activity_order_summary.*
import kotlinx.android.synthetic.main.layout_toolbar.*

class OrderSummaryActivity : AppCompatActivity() {

    private lateinit var database: AppDatabase
    private var toast: Toast?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_summary)

        database = AppDatabase.getInstance(this)

        img_back.setOnClickListener { onBackPressed() }
        rl_all.setOnClickListener {
            if(txt_all_order.text.toString() != "0") {
                rl_all.isEnabled = false
                navigateToOrderList("")
            }
            else showMessage()
        }
        rl_active.setOnClickListener {
            if(txt_active_order.text.toString() != "0"){
                rl_active.isEnabled = false
                navigateToOrderList(getString(R.string.status_active))
            }
            else showMessage()
        }
        rl_trial_done.setOnClickListener {
            if(txt_trial_done_order.text.toString() != "0") {
                rl_trial_done.isEnabled = false
                navigateToOrderList(getString(R.string.status_trial_done))
            }
            else showMessage()
        }
        rl_delivered.setOnClickListener {
            if(txt_delivered_order.text.toString() != "0") {
                rl_delivered.isEnabled = false
                navigateToOrderList(getString(R.string.status_delivered))
            }
            else showMessage()
        }
        rl_paid.setOnClickListener {
            if(txt_paid_order.text.toString() != "0") {
                rl_paid.isEnabled = false
                navigateToOrderList(getString(R.string.status_paid))
            }
            else showMessage()
        }
    }

    override fun onResume() {
        super.onResume()
        database.orderDao().getOrderListCount().observe(this, Observer<Int> {
            txt_all_order.text = it.toString()
        })
        database.orderDao().getOrderListCountBasedOnStatus(getString(R.string.status_active)).observe(this, Observer<Int> {
            txt_active_order.text = it.toString()
        })
        database.orderDao().getOrderListCountBasedOnStatus(getString(R.string.status_trial_done)).observe(this, Observer<Int> {
            txt_trial_done_order.text = it.toString()
        })
        database.orderDao().getOrderListCountBasedOnStatus(getString(R.string.status_delivered)).observe(this, Observer<Int> {
            txt_delivered_order.text = it.toString()
        })
        database.orderDao().getOrderListCountBasedOnStatus(getString(R.string.status_paid)).observe(this, Observer<Int> {
            txt_paid_order.text = it.toString()
        })
        rl_all.isEnabled = true
        rl_active.isEnabled = true
        rl_trial_done.isEnabled = true
        rl_delivered.isEnabled = true
        rl_paid.isEnabled = true
    }

    private fun showMessage(){
        if(toast != null && toast!!.view.isShown){
            toast!!.setText("No order found")
        } else {
            toast = Toast.makeText(this, "No order found", Toast.LENGTH_SHORT)
            toast!!.show()
        }
    }

    private fun navigateToOrderList(status: String){
        val intent = Intent(this@OrderSummaryActivity, OrderListByCustomerActivity::class.java)
        intent.putExtra(IS_FROM_ORDER_SUMMARY, true);
        intent.putExtra(ORDER_STATUS, status)
        startActivity(intent)
    }

}
