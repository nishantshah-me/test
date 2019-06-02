package example.com.vestir.view.client

import android.arch.lifecycle.Observer
import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
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
    private var isUpdate: Boolean = false
    private var updateClientId : Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_client)
        hideKeyboard()

        clientNames = ArrayList()

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
            etContact.setText(clientList.get(indexOf!!).contact.toString())
            etAddress.setText(clientList.get(indexOf).address)
            etReference.setText(clientList.get(indexOf).reference)

            btnSubmit.setText("Update")
            updateClientId = clientList.get(indexOf).clientid
        }

        btnSubmit.setOnClickListener({ addClient() })

        img_back.setOnClickListener { onBackPressed() }

        btnActiveOrders.setOnClickListener{
            var intent = Intent(this,OrderListByCustomerActivity::class.java)
            intent.putExtra("activeOrders",true)
            startActivity(intent)
        }

        btnPastOrders.setOnClickListener{
            var intent = Intent(this,OrderListByCustomerActivity::class.java)
            intent.putExtra("pastOrders",true)
            startActivity(intent)
        }
    }

    private fun hideKeyboard() {
        if (currentFocus != null) {
            val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
        }

    }

    private fun addClient() {
        if (isvalidForm()) {
            client = Client()
            client?.name = etName.text.toString()
            client?.contact = etContact.text.toString().toLong()
            client?.address = etAddress.text.toString()
            client?.reference = etReference.text.toString()

             clientList.forEach {
                 isUpdate = it.name?.equals(client?.name)!!
             }

            if (isUpdate) {
                client!!.clientid = updateClientId
                clientDao.updateClient(client)
                Toast.makeText(this, "Client Updated Successfully", Toast.LENGTH_SHORT).show()
            } else {
                clientDao.insertClient(client)
                val msmt = Measurement()
                msmt.height = 12.3f
                msmt.width = 9.3f
             //   AppDatabase.getInstance(this).measurementDao().insertMeasurement(msmt)
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

