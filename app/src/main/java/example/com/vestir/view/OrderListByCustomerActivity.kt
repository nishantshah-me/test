package example.com.vestir.view

import android.arch.lifecycle.Observer
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import example.com.vestir.IS_FOR_UPDATE
import example.com.vestir.ORDER_DATA
import example.com.vestir.R
import example.com.vestir.database.AppDatabase
import example.com.vestir.database.entity.ClientOrder
import kotlinx.android.synthetic.main.activity_order_list_by_customer.*
import kotlinx.android.synthetic.main.layout_toolbar.*
import android.widget.AdapterView



class OrderListByCustomerActivity : AppCompatActivity(), OrderListAdapter.OnOrderItemClickListener {

    private lateinit var adapter: OrderListAdapter
    private lateinit var database: AppDatabase
    private var orderNames: MutableList<String> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_list_by_customer)
        database = AppDatabase.getInstance(this)

        database.clientDao().getAllClient().observe(this, Observer {
            val size = it!!.size - 1
            for (i in 0..size) {
                orderNames.add(i, it.get(i).name!!)
            }
            val adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, orderNames)
            etName.setAdapter(adapter)
        })

        etName.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            database.orderDao().getOrderListBasedOnClient(parent.getItemAtPosition(position).toString()).observe(this, Observer<List<ClientOrder>?> {
                adapter.setList(it)
                setTextToButton(getString(R.string.new_order))
            })
        }

        setTextToButton(getString(R.string.new_order))
        adapter = OrderListAdapter(this, this, null)
        rv_order_list.adapter = adapter

        img_back.setOnClickListener { onBackPressed() }

        btnSubmit.setOnClickListener {
            val intent = Intent(this@OrderListByCustomerActivity, CreateOrderActivity::class.java)
            when(btnSubmit.text.toString()){
                getString(R.string.new_order) -> { startActivity(intent) }
                getString(R.string.update_order) -> {
                    intent.putExtra(IS_FOR_UPDATE, true)
                    val orderForUpdate = adapter.getSelectedOrderList()
                    if(orderForUpdate.isNotEmpty())
                        intent.putExtra(ORDER_DATA, orderForUpdate[0])
                    startActivity(intent)
                }
                else -> {
                    Toast.makeText(this@OrderListByCustomerActivity,
                            "Not implemented." , Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun setTextToButton(text: String){
        btnSubmit.text =  text
    }

    override fun onItemCheckChanged(checkItemCount: Int) {
        val btnText = when(checkItemCount){
            0 -> getString(R.string.new_order)
            1 -> getString(R.string.update_order)
            else -> getString(R.string.open_cost_page)
        }
        setTextToButton(btnText)
    }
}
