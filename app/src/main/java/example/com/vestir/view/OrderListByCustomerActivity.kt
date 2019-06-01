package example.com.vestir.view

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import example.com.vestir.R
import kotlinx.android.synthetic.main.activity_order_list_by_customer.*

class OrderListByCustomerActivity : AppCompatActivity() {

    private lateinit var adapter: OrderListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_list_by_customer)

        adapter = OrderListAdapter(this)
        rv_order_list.adapter = adapter

        btnSubmit.setOnClickListener {
            startActivity(Intent(this@OrderListByCustomerActivity, CreateOrderActivity::class.java))
        }
    }
}
