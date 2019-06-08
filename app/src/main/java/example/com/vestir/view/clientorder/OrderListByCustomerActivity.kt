package example.com.vestir.view.clientorder

import android.app.Activity
import android.arch.lifecycle.Observer
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.ArrayAdapter
import android.widget.Toast
import example.com.vestir.database.AppDatabase
import example.com.vestir.database.entity.ClientOrder
import kotlinx.android.synthetic.main.activity_order_list_by_customer.*
import kotlinx.android.synthetic.main.layout_toolbar.*
import android.widget.AdapterView
import example.com.vestir.*
import example.com.vestir.database.entity.Client


class OrderListByCustomerActivity : AppCompatActivity(), OrderListAdapter.OnOrderItemClickListener {

    private lateinit var adapter: OrderListAdapter
    private lateinit var database: AppDatabase
    private var clientList: List<Client>? = null
    private var selectedClientId: Long? = null
    private var orderNames: MutableList<String> = ArrayList()
    private var orderStatusSelected: String = ""
    private var isFromOrderSummary: Boolean = false
    private var isFromNewClient: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_list_by_customer)

        getIntentData()
        database = AppDatabase.getInstance(this)

        database.clientDao().getAllClient().observe(this, Observer {
            if(it != null){
                clientList = it
                val size = it.size - 1
                for (i in 0..size) {
                    orderNames.add(i, it[i].name!!)
                }
                val adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, orderNames)
                etName.setAdapter(adapter)
            }
        })

        etName.onItemClickListener = getAutoCompleteViewItemSelectListener()

        setTextToButton(getString(R.string.new_order))
        adapter = OrderListAdapter(this, this, null)
        rv_order_list.adapter = adapter
        img_back.setOnClickListener { onBackPressed() }
        btnSubmit.setOnClickListener { onSubmitButtonClick() }

        if(isFromOrderSummary){
            getDefaultOrderList()
        } else if(isFromNewClient){
            getOrderByClient(intent.getStringExtra(SELECTED_CLIENT_NAME))
        }
    }

    private fun setTextToButton(text: String){
        btnSubmit.text =  text
    }

    private fun setListToRecyclerView(list: List<ClientOrder>, status: String = orderStatusSelected){
        val listToSort = ArrayList<ClientOrder>()
        listToSort.addAll(list)
        adapter = OrderListAdapter(this, this, sortListBasedOnStatus(status, listToSort))
        rv_order_list.adapter = adapter
    }

    private fun getDefaultOrderList(){
        rv_order_list.adapter = null
        setTextToButton(getString(R.string.new_order))
        if(TextUtils.isEmpty(orderStatusSelected)){
            database.orderDao().getOrderList()
                    .observe(this, Observer<List<ClientOrder>?> {
                        if (it != null && it.isNotEmpty()){
                            setListToRecyclerView(it)
                        }
                    })
        } else {
            database.orderDao().getOrderListBasedOnStatus(orderStatusSelected)
                    .observe(this, Observer<List<ClientOrder>?> {
                        if (it != null && it.isNotEmpty()){
                            setListToRecyclerView(it)
                        }
                    })
        }
    }

    private fun getAutoCompleteViewItemSelectListener(): AdapterView.OnItemClickListener{
        return AdapterView.OnItemClickListener { parent, _, position, _ ->
            if(clientList != null && clientList!!.isNotEmpty()){
                selectedClientId = clientList!![position].clientid.toLong()
            }
            val clientName = parent.getItemAtPosition(position).toString()
            getOrderByClient(clientName)
        }
    }

    private fun getOrderByClient(clientName: String){
        rv_order_list.adapter = null
        setTextToButton(getString(R.string.new_order))
        if(TextUtils.isEmpty(orderStatusSelected)){
            database.orderDao().getOrderListBasedOnClient(clientName)
                    .observe(this, Observer<List<ClientOrder>?> {
                        if (it != null && it.isNotEmpty()){
                            setListToRecyclerView(it)
                        }
                    })
        } else {
            database.orderDao().getOrderListBasedOnClientAndStatus(clientName, orderStatusSelected)
                    .observe(this, Observer<List<ClientOrder>?> {
                        if (it != null && it.isNotEmpty()){
                            setListToRecyclerView(it)
                        }
                    })
        }
    }

    private fun sortListBasedOnStatus(status: String, list: ArrayList<ClientOrder>): ArrayList<ClientOrder>{
        when(status){
            getString(R.string.status_active)-> list.sortBy { it.trial }
            getString(R.string.status_trial_done)-> list.sortBy { it.delivery }
            getString(R.string.status_delivered)-> list.sortBy { it.order }
            getString(R.string.status_paid)-> list.sortBy { it.order }
            else -> list.sortBy { it.order }
        }
        list.reverse()
        return list
    }

    private fun onSubmitButtonClick(){
        when (btnSubmit.text.toString()) {
            getString(R.string.new_order) -> {
                if(selectedClientId != null) {
                    val intent = Intent(this@OrderListByCustomerActivity, CreateOrderActivity::class.java)
                    intent.putExtra(SELECT_CLIENT_ID, selectedClientId!!)
                    startActivityForResult(intent, ORDER_ADD_UPDATE_REQUEST)
                } else {
                    Toast.makeText(this@OrderListByCustomerActivity,
                            "Please select client.", Toast.LENGTH_SHORT).show()
                }
            }
            getString(R.string.update_order) -> {
                val intent = Intent(this@OrderListByCustomerActivity, CreateOrderActivity::class.java)
                intent.putExtra(IS_FOR_UPDATE, true)
                val orderForUpdate = adapter.getSelectedOrderList()
                if (orderForUpdate.isNotEmpty()) {
                    intent.putExtra(SELECT_CLIENT_ID, orderForUpdate[0].clientId)
                    intent.putExtra(ORDER_DATA, orderForUpdate[0])
                    startActivityForResult(intent, ORDER_ADD_UPDATE_REQUEST)
                } else {
                    Toast.makeText(this@OrderListByCustomerActivity,
                            "Please select order.", Toast.LENGTH_SHORT).show()
                }
            }
            else -> {
                val selectedOrderList = adapter.getSelectedOrderList()
                if(selectedOrderList.isNotEmpty()){
                    val firstOrder = selectedOrderList.first()
                    val counter = selectedOrderList
                            .takeWhile { firstOrder.name == it.name }
                            .count();
                    if(counter == selectedOrderList.size){
                        Toast.makeText(this@OrderListByCustomerActivity,
                                "Not implemented.", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this@OrderListByCustomerActivity,
                                "Please select order with same client.", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    override fun onItemCheckChanged(checkItemCount: Int) {
        val btnText = when(checkItemCount){
            0 -> getString(R.string.new_order)
            1 -> getString(R.string.update_order)
            else -> getString(R.string.open_cost_page)
        }
        setTextToButton(btnText)
    }

    private fun getIntentData(){
        if(intent.hasExtra(ORDER_STATUS)){
            orderStatusSelected = intent.getStringExtra(ORDER_STATUS)
            isFromOrderSummary = intent.getBooleanExtra(IS_FROM_ORDER_SUMMARY, false)
            isFromNewClient = intent.getBooleanExtra(IS_FROM_NEW_CLIENT, false)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == ORDER_ADD_UPDATE_REQUEST && resultCode == Activity.RESULT_OK){
            val autoCompleteText = etName.text.toString().trim()
            if(TextUtils.isEmpty(autoCompleteText)){
                getDefaultOrderList()
            } else {
                getOrderByClient(autoCompleteText)
            }
        } else super.onActivityResult(requestCode, resultCode, data)
    }
}
