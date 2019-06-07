package example.com.vestir.view.client

import android.arch.lifecycle.Observer
import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView.OnItemClickListener
import android.widget.Toast
import example.com.vestir.R
import example.com.vestir.database.AppDatabase
import example.com.vestir.database.entity.Client
import example.com.vestir.database.dao.ClientDao
import kotlinx.android.synthetic.main.activity_new_client.*
import android.widget.ArrayAdapter
import example.com.vestir.database.entity.Measurement
import example.com.vestir.view.OrderListByCustomerActivity
import kotlinx.android.synthetic.main.layout_toolbar.*


class NewClientActivity : AppCompatActivity() {

    lateinit var clientDao: ClientDao
    var client: Client? = null
    var clientList: List<Client> = ArrayList()
    var clientNames: MutableList<String>? = null
    var msmt = Measurement()
    private var isUpdate: Boolean = false
    private var updateClientId : Int = 0
    private val REQUEST_CODE = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_client)
        hideKeyboard()

        clientNames = ArrayList()
        client = Client()

        clientDao = AppDatabase.getInstance(this).clientDao()

        clientDao.getAllClient().observe(this, Observer {
            val size = it!!.size - 1
            for (i in 0..size) {
                clientNames?.add(i, it.get(i).name.toString())
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
        }

        btnSubmit.setOnClickListener({ addClient() })

        img_back.setOnClickListener { onBackPressed() }

        btnNewClient.setOnClickListener{
            checkForNewOrUpdate()
        }

        btnActiveOrders.setOnClickListener{
            val intent = Intent(this,OrderListByCustomerActivity::class.java)
            intent.putExtra("activeOrders",true)
            startActivity(intent)
        }

        btnPastOrders.setOnClickListener{
            val intent = Intent(this,OrderListByCustomerActivity::class.java)
            intent.putExtra("pastOrders",true)
            startActivity(intent)
        }

        txtEditMeasurement.setOnClickListener {
            startActivityForResult(Intent(this@NewClientActivity,MeasurementActivity::class.java),REQUEST_CODE)
        }

    }

    private fun checkForNewOrUpdate() {
        containerFields.visibility = View.VISIBLE
        btnSubmit.visibility = View.VISIBLE
        btnNewClient.visibility = View.GONE
    }

    private fun hideKeyboard() {
        if (currentFocus != null) {
            val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
        }

    }

    private fun addClient() {
        if (isvalidForm()) {

            client?.name = etName.text.toString()
            client?.contact = etContact.text.toString().toLong()
            client?.address = etAddress.text.toString()
            client?.reference = etReference.text.toString()

             clientList.forEach {
                 client!!.clientid = updateClientId
                 isUpdate = it.clientid.equals(client?.clientid)
             }

            if (isUpdate) {
                client!!.clientid = updateClientId
                clientDao.updateClient(client)
                Toast.makeText(this, "Client Updated Successfully", Toast.LENGTH_SHORT).show()
            } else {
                clientDao.insertClient(client)
               // msmt.height = 12.3f
                //msmt.width = 9.3f
                msmt.clientId = client!!.clientid
                AppDatabase.getInstance(this).measurementDao().insertMeasurement(msmt)
                Toast.makeText(this, "Client Added Successfully", Toast.LENGTH_SHORT).show()
            }
        }

    }


    fun isvalidForm(): Boolean {
        if (TextUtils.isEmpty(etName.text)) {
            etName.setError("Please enter valid client name")
            return false
        } else if (TextUtils.isEmpty(etContact.text) || etContact.text.length < 10) {
            etContact.setError("Please enter valid contact number")
            return false
        } else if (TextUtils.isEmpty(etAddress.text)) {
            etAddress.setError("Please enter valid address")
            return false
        } else if (TextUtils.isEmpty(etReference.text)) {
            etReference.setError("Please enter valid reference")
            return false
        }

        return true
    }
}

