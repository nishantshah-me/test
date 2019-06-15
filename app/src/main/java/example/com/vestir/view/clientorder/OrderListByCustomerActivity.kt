package example.com.vestir.view.clientorder

import android.app.Activity
import android.app.DatePickerDialog
import android.arch.lifecycle.Observer
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import example.com.vestir.database.AppDatabase
import example.com.vestir.database.entity.ClientOrder
import kotlinx.android.synthetic.main.activity_order_list_by_customer.*
import kotlinx.android.synthetic.main.layout_toolbar.*
import android.widget.AdapterView
import android.widget.PopupMenu
import com.google.gson.Gson
import example.com.vestir.*
import example.com.vestir.database.entity.Client
import example.com.vestir.view.invoice.InvoiceActivity
import java.text.SimpleDateFormat
import java.util.*
import example.com.vestir.view.costing.CostPageActivity
import kotlin.collections.ArrayList


class OrderListByCustomerActivity : AppCompatActivity(), OrderListAdapter.OnOrderItemClickListener, PopupMenu.OnMenuItemClickListener {

    private lateinit var adapter: OrderListAdapter
    private lateinit var database: AppDatabase
    private var clientList: List<Client>? = null
    private var clientNameList: MutableList<String> = ArrayList()
    private var orderStatusSelected: String = ""
    private var isFromOrderSummary: Boolean = false
    private var isFromNewClient: Boolean = false
    private var dateSearchType: String = ""

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
                    clientNameList.add(i, it[i].name)
                }
                val adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, clientNameList)
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
            setSearchView()
        } else if(isFromNewClient){
            etName.setText(intent.getStringExtra(SELECTED_CLIENT_NAME))
            getOrderByClient(intent.getLongExtra(SELECT_CLIENT_ID, 0))
        }
    }

    private fun setSearchView(){
        img_search.visibility = View.VISIBLE
        img_search.setOnClickListener { view -> showSearchMenu(view) }
        txt_from_date.setOnClickListener { showDatePicker("from") }
        txt_to_date.setOnClickListener { showDatePicker("to") }
        txt_search_cancel.setOnClickListener {
            img_search.visibility = View.VISIBLE
            cl_search_date.visibility = View.GONE
            ll_name.visibility = View.VISIBLE
            dateSearchType = ""
            txt_from_date.text = ""
            txt_to_date.text = ""
            adapter.resetOriginalList()

        }
        txt_date_search.setOnClickListener {
            if(TextUtils.isEmpty(txt_from_date.text.toString()) ||
                    TextUtils.isEmpty(txt_to_date.text.toString())){
                Toast.makeText(this@OrderListByCustomerActivity, "Both From and To date required",
                        Toast.LENGTH_SHORT).show()
            } else {
                adapter.filterByDate(dateSearchType, txt_from_date.text.toString(), txt_to_date.text.toString())
            }
        }
    }

    private fun showDatePicker(date: String){
        val calendar = Calendar.getInstance()
        DatePickerDialog(this@OrderListByCustomerActivity,
                DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                    val cal = Calendar.getInstance()
                    cal.set(Calendar.YEAR, year)
                    cal.set(Calendar.MONTH, month)
                    cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                    val sdf = SimpleDateFormat(ORDER_DATE_FORMAT)
                    when(date){
                        "from" -> txt_from_date.text = sdf.format(cal.time)
                        "to" -> txt_to_date.text = sdf.format(cal.time)
                    }
                }, calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)).show()
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
                val clientName = parent.getItemAtPosition(position).toString()
                val index = clientNameList.indexOf(clientName)
                val selectedClient = clientList!![index]
                getOrderByClient(selectedClient.clientid)
            }
        }
    }

    private fun showSearchMenu(view: View) {
        val popUp = PopupMenu(this@OrderListByCustomerActivity, view)
        popUp.setOnMenuItemClickListener(this)
        popUp.inflate(R.menu.date_search_menu)
        popUp.show()
    }

    override fun onMenuItemClick(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.order_date, R.id.trial_date, R.id.delivery_date -> {
                dateSearchType = item.title.toString()
                showDateSearchView()
                return true
            }
            else -> return false
        }
    }

    private fun showDateSearchView(){
        img_search.visibility = View.GONE
        ll_name.visibility = View.GONE
        cl_search_date.visibility = View.VISIBLE
    }

    private fun getOrderByClient(clientId: Long){
        rv_order_list.adapter = null
        setTextToButton(getString(R.string.new_order))
        if(TextUtils.isEmpty(orderStatusSelected)){
            database.orderDao().getOrderListBasedOnClient(clientId)
                    .observe(this, Observer<List<ClientOrder>?> {
                        if (it != null && it.isNotEmpty()){
                            setListToRecyclerView(it)
                        }
                    })
        } else {
            database.orderDao().getOrderListBasedOnClientAndStatus(clientId, orderStatusSelected)
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
                val nameSelected = etName.text.toString().trim()
                if(clientNameList.contains(nameSelected)){
                    val index = clientNameList.indexOf(nameSelected)
                    val selectedClient = clientList!![index]
                    val intent = Intent(this@OrderListByCustomerActivity, CreateOrderActivity::class.java)
                    intent.putExtra(SELECT_CLIENT_ID, selectedClient.clientid)
                    intent.putExtra(SELECTED_CLIENT_NAME, selectedClient.name)
                    startActivityForResult(intent, ORDER_ADD_UPDATE_REQUEST)
                } else {
                    Toast.makeText(this@OrderListByCustomerActivity,
                            "Please select valid client.", Toast.LENGTH_SHORT).show()
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
                        /*Toast.makeText(this@OrderListByCustomerActivity,
                                "Not implemented.", Toast.LENGTH_SHORT).show()*/
                       // startActivity(Intent(this, InvoiceActivity::class.java))

                        val intent = Intent(this,CostPageActivity::class.java)
                        intent.putExtra(SELECTED_ORDERS,selectedOrderList)
                        startActivity(intent)
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
                if(clientNameList.contains(autoCompleteText)){
                    val index = clientNameList.indexOf(autoCompleteText)
                    val selectedClient = clientList!![index]
                    getOrderByClient(selectedClient.clientid)
                } else {
                    Toast.makeText(this@OrderListByCustomerActivity,
                            "Please select valid client.", Toast.LENGTH_SHORT).show()
                }
            }
        } else super.onActivityResult(requestCode, resultCode, data)
    }


    override fun onCostClick(selectedOrders: ArrayList<ClientOrder>) {
        val intent = Intent(this,CostPageActivity::class.java)
        intent.putExtra(SELECTED_ORDERS,selectedOrders)
        startActivity(intent)
    }
}
