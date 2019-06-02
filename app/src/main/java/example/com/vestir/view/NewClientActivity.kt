package example.com.vestir.view

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Observer
import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import example.com.vestir.R
import example.com.vestir.database.AppDatabase
import example.com.vestir.database.entity.Client
import example.com.vestir.database.dao.ClientDao
import kotlinx.android.synthetic.main.activity_new_client.*
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView


class NewClientActivity : AppCompatActivity() {

    lateinit var clientDao: ClientDao
    var client: Client? = null
    var clientNames: MutableList<String>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_client)



        if (currentFocus != null) {
            val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
        }

        clientNames = ArrayList()

        clientDao = AppDatabase.getInstance(this).clientDao()

        clientDao.getAllClient().observe(this, Observer {
            val size = it!!.size - 1
            for (i in 0..size) {
                clientNames?.add(i, it.get(i).name.toString())
            }

            val adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, clientNames)
            etName.setAdapter(adapter)

        })


           btnSubmit.setOnClickListener({
              if (isvalidForm()) {
                  client = Client()
                  client?.name = etName.text.toString()
                  client?.contact = etContact.text.toString().toLong()
                  client?.address = etAddress.text.toString()
                  client?.reference = etReference.text.toString()

                  Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show()

                  clientDao.insertClient(client)
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

