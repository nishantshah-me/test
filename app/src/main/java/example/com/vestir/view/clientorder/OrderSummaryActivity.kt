package example.com.vestir.view.clientorder

import android.arch.lifecycle.Observer
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_summary)

        database = AppDatabase.getInstance(this)

        img_back.setOnClickListener { onBackPressed() }
        rl_all.setOnClickListener { navigateToOrderList("") }
        rl_active.setOnClickListener { navigateToOrderList(getString(R.string.status_active)) }
        rl_trial_done.setOnClickListener { navigateToOrderList(getString(R.string.status_trial_done)) }
        rl_delivered.setOnClickListener { navigateToOrderList(getString(R.string.status_delivered)) }
        rl_paid.setOnClickListener { navigateToOrderList(getString(R.string.status_paid)) }
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
    }

    private fun navigateToOrderList(status: String){
        val intent = Intent(this@OrderSummaryActivity, OrderListByCustomerActivity::class.java)
        intent.putExtra(IS_FROM_ORDER_SUMMARY, true);
        intent.putExtra(ORDER_STATUS, status)
        startActivity(intent)
    }

}
