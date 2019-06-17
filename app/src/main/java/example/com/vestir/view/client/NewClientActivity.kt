package example.com.vestir.view.client

import android.app.Activity
import android.arch.lifecycle.Observer
import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View

import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView.OnItemClickListener
import android.widget.Toast
import example.com.vestir.database.AppDatabase
import example.com.vestir.database.entity.Client
import example.com.vestir.database.dao.ClientDao
import kotlinx.android.synthetic.main.activity_new_client.*
import android.widget.ArrayAdapter
import com.google.gson.Gson
import example.com.vestir.*
import example.com.vestir.database.dao.OrderDao
import example.com.vestir.database.entity.ClientOrder
import example.com.vestir.database.entity.Measurement
import example.com.vestir.view.clientorder.OrderListByCustomerActivity
import kotlinx.android.synthetic.main.layout_toolbar.*
import java.util.*


class NewClientActivity : AppCompatActivity() {


    lateinit var clientDao: ClientDao
    lateinit var clientOrder: OrderDao
    var client: Client = Client()
    var clientList: List<Client> = ArrayList()
    var clientNames: MutableList<String>? = null
    var measurement = Measurement()
    private var isUpdate: Boolean = false
    private var updateClientId: Long = 0
    private val REQUEST_CODE = 100
    val CLIENT_ID = "clientId"
    val MEASUREMENT = "measurement"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_client)
        hideKeyboard()

        clientNames = ArrayList()

        clientDao = AppDatabase.getInstance(this).clientDao()
        clientOrder = AppDatabase.getInstance(this).orderDao()

        img_search.visibility = View.VISIBLE
        img_search.setImageResource(R.drawable.ic_refresh)
        img_search.setOnClickListener {
            etName.setText("")
            etContact.setText("")
            etAddress.setText("")
            etReference.setText("")
        }


        clientDao.getAllClient().observe(this, Observer {
            val size = it!!.size - 1
            for (i in 0..size) {
                clientNames?.add(i, it.get(i).name)
            }
            clientList = it

            val adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, clientNames)
            etName.setAdapter(adapter)

        })

        etName.onItemClickListener = OnItemClickListener { parent, view, position, id ->
            val indexOf = clientNames?.indexOf(etName.text.toString())
            etContact.setText(clientList[indexOf!!].contact.toString())
            etAddress.setText(clientList[indexOf].address)
            etReference.setText(clientList[indexOf].reference)


            btnSubmit.setText(getString(R.string.update))
            btnNewClient.setText("Update Client")
            updateClientId = clientList[indexOf].clientid
            isUpdate = true;

            getOrderStatus(updateClientId)

        }

        btnSubmit.setOnClickListener({ addClient() })

        img_back.setOnClickListener { onBackPressed() }

        btnNewClient.setOnClickListener {
            checkForNewOrUpdate()
        }


        btnActiveOrders.setOnClickListener {
            val clientName = etName.text.toString().trim()
            if (clientNames != null && clientNames!!.contains(clientName) && clientList.isNotEmpty()) {
                val index = clientNames!!.indexOf(clientName)
                val clientId = clientList[index].clientid
                getOrderByClient(clientId, getString(R.string.status_active))
            } else {
                Toast.makeText(this@NewClientActivity, "Please select valid client", Toast.LENGTH_SHORT).show()
            }
        }

        btnPastOrders.setOnClickListener {
            val clientName = etName.text.toString().trim()
            if (clientNames != null && clientNames!!.contains(clientName) && clientList.isNotEmpty()) {
                val index = clientNames!!.indexOf(clientName)
                val clientId = clientList[index].clientid
                getOrderByClient(clientId, getString(R.string.status_paid))
            } else {
                Toast.makeText(this, "Please select valid client", Toast.LENGTH_SHORT).show()
            }
        }

        txtEditMeasurement.setOnClickListener {
            val intent = Intent(this@NewClientActivity, MeasurementActivity::class.java)
            intent.putExtra(CLIENT_ID, updateClientId)
            startActivityForResult(intent, REQUEST_CODE)
        }

    }

    private fun getOrderStatus(updateClientId: Long) {
        clientOrder.getOrderListCountByClientAndStatus(updateClientId, getString(R.string.status_paid))
                .observe(this, Observer { it ->
                    if (it != null) {
                        if (it > 0)
                            btnPastOrders.isEnabled = true

                    }
                })

        clientOrder.getOrderListCountByClientAndStatus(updateClientId, getString(R.string.status_active))
                .observe(this, Observer { it ->
                    if (it != null) {
                        if (it > 0)
                            btnActiveOrders.isEnabled = true

                    }
                })
        clientOrder.getOrderListCountByClientAndStatus(updateClientId, getString(R.string.status_trial_done))
                .observe(this, Observer { it ->
                    if (it != null) {
                        if (it > 0)
                            btnActiveOrders.isEnabled = true

                    }
                })
        clientOrder.getOrderListCountByClientAndStatus(updateClientId, getString(R.string.status_delivered))
                .observe(this, Observer { it ->
                    if (it != null) {
                        if (it > 0)
                            btnActiveOrders.isEnabled = true

                    }
                })
    }

    private fun checkForNewOrUpdate() {
        containerFields.visibility = View.VISIBLE
        btnSubmit.visibility = View.VISIBLE
        btnNewClient.visibility = View.GONE
    }

    private fun navigateToOrderList(clientId: Long, orderStatus: String) {
        val intent = Intent(this, OrderListByCustomerActivity::class.java)
        intent.putExtra(SELECTED_CLIENT_NAME, etName.text.toString().trim())
        intent.putExtra(SELECT_CLIENT_ID, clientId)
        intent.putExtra(ORDER_STATUS, orderStatus)
        intent.putExtra(IS_FROM_NEW_CLIENT, true)
        startActivity(intent)
    }

    private fun hideKeyboard() {
        if (currentFocus != null) {
            val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
        }

    }

    private fun addClient() {
        if (isvalidForm()) {

            client.name = etName.text.toString().toUpperCase()
            client.contact = etContact.text.toString().toLong()
            client.address = etAddress.text.toString()
            client.reference = etReference.text.toString()

            clientList.forEach {
                client.clientid = updateClientId
                isUpdate = it.clientid.equals(client.clientid)
            }

            if (isUpdate) {
               updateClient()
            } else {
                if (clientList.size > 0) {
                    for (i in 0..clientList.size - 1) {
                        if (client.name.equals(clientList[i].name))
                            Toast.makeText(this, "Client Name Already present", Toast.LENGTH_SHORT).show()
                        else {
                            insertClient()
                        }

                    }
                } else {
                    insertClient()
                }

            }

        }

    }

    private fun getOrderByClient(clientId: Long, status: String) {
        clientOrder.getOrderListBasedOnClientAndStatus(clientId, status)
                .observe(this, Observer<List<ClientOrder>?> {
                    if (it != null && it.isNotEmpty()) {
                        navigateToOrderList(clientId, status)
                    }
                })
    }

    fun isvalidForm(): Boolean {
        if (TextUtils.isEmpty(etName.text)) {
            etName.setError("Please enter valid client name")
            return false
        } else if (TextUtils.isEmpty(etContact.text) || etContact.text.length < 10) {
            etContact.setError("Please enter valid contact number")
            return false
        }

        return true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                val gson = Gson()
                val msmtKey = data.getStringExtra(MEASUREMENT)
                measurement = gson.fromJson(msmtKey, Measurement::class.java)
            }
        }
    }

    fun insertClient() {
        client.clientid = Calendar.getInstance().timeInMillis
        clientDao.insertClient(client)
        measurement.clientId = client.clientid
        AppDatabase.getInstance(this).measurementDao().insertMeasurement(measurement)
        Toast.makeText(this, "Client Added Successfully", Toast.LENGTH_SHORT).show()
    }

    fun updateClient() {
        client.clientid = updateClientId
        clientDao.updateClient(client)
        Toast.makeText(this, "Client Updated Successfully", Toast.LENGTH_SHORT).show()
    }

}

