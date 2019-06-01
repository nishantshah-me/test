package example.com.vestir.view

import android.arch.lifecycle.Observer
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import example.com.vestir.R
import example.com.vestir.database.AppDatabase
import example.com.vestir.database.Client
import example.com.vestir.database.ClientDao
import kotlinx.android.synthetic.main.activity_new_client.*

class NewClientActivity : AppCompatActivity(), TextWatcher {


    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
    }

    override fun afterTextChanged(s: Editable?) {

    }

    lateinit var clientDao: ClientDao
    var client: Client? = null
    var clientList : List<Client>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_client)

        clientDao = AppDatabase.getInstance(this).clientDao()
        clientDao.getAllClient().observe(this, Observer {
            clientList = it
        })

        etName.addTextChangedListener(this)

        btnSubmit.setOnClickListener({
            if (isvalidForm()) {
                client = Client()
                client?.name = etName.text.toString()
                client?.contact = etContact.text.toString().toLong()
                client?.address = etAddress.text.toString()
                client?.reference = etReference.text.toString()
                client?.isMeasurementCorrect = cbMeasurement.isChecked

                Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show()

                clientDao.insertClient(client)
            }

        })
    }

    fun isvalidForm(): Boolean {
        if (TextUtils.isEmpty(etName.text)) {
            etName.setError("Please enter valid client name")
            return false
        }

        if (TextUtils.isEmpty(etContact.text) || etContact.text.length < 10) {
            etContact.setError("Please enter valid contact number")
            return false
        }

        if (TextUtils.isEmpty(etAddress.text)) {
            etAddress.setError("Please enter valid address")
            return false
        }

        if (TextUtils.isEmpty(etReference.text)) {
            etReference.setError("Please enter valid reference")
            return false
        }

        return true
    }
}

