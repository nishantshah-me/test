package example.com.vestir.view

import android.arch.lifecycle.Observer
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.ArrayAdapter
import android.widget.Toast
import example.com.vestir.IS_FOR_UPDATE
import example.com.vestir.ORDER_DATA
import example.com.vestir.R
import example.com.vestir.database.AppDatabase
import example.com.vestir.database.entity.ClientOrder
import kotlinx.android.synthetic.main.activity_create_order.*
import kotlinx.android.synthetic.main.layout_toolbar.*

class CreateOrderActivity : AppCompatActivity() {
    private lateinit var database: AppDatabase
    private var isUpdate: Boolean = false
    private var updateOrderId: Int = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_order)

        database = AppDatabase.getInstance(this)



        if(intent.getBooleanExtra(IS_FOR_UPDATE, false) && intent.hasExtra(ORDER_DATA)){
            isUpdate = true
            btnSubmit.text = getString(R.string.str_update)
            val order = intent.getSerializableExtra(ORDER_DATA) as ClientOrder
            showData(order)
        }
        btnSubmit.setOnClickListener { addOrder() }

        img_back.setOnClickListener { onBackPressed() }
    }

    private fun addOrder(){
        if(validateInpute()){
            val order = ClientOrder().apply {
                name = etName.text.toString()
                style = etStyle.text.toString()
                description = etDesc.text.toString()
                status = spnStatus.selectedItem.toString()
                order = etorder.text.toString()
                trial = etTrial.text.toString()
                delivery = etDelivery.text.toString()
                quote = etQuote.text.toString()
            }
            var message = "ClientOrder added successfully"
            if(isUpdate){
                order.orderId = updateOrderId
                database.orderDao().updateOrder(order)
                message = "ClientOrder updated successfully"
            } else {
                database.orderDao().addOrder(order)
            }
            Toast.makeText(this@CreateOrderActivity, message , Toast.LENGTH_SHORT).show()
        }
    }

    private fun showData(order: ClientOrder) {
        updateOrderId = order.orderId
        etName.setText(order.name)
        etName.isEnabled = false
        etStyle.setText(order.style)
        etDesc.setText(order.description)

        val statusList = resources.getStringArray(R.array.statusList)
        var index = statusList.indexOf(order.status)
        index = if (index < 0) 0 else index
        spnStatus.setSelection(index)

        etorder.setText(order.order)
        etTrial.setText(order.trial)
        etDelivery.setText(order.delivery)
        etQuote.setText(order.quote)
    }

    private fun validateInpute() : Boolean{
        if(TextUtils.isEmpty(etName.text.toString())){
            etName.requestFocus()
            etName.error = "Name is empty"
        } else if(TextUtils.isEmpty(etStyle.text.toString())){
            etStyle.requestFocus()
            etStyle.error = "Style is empty"
        } else if(TextUtils.isEmpty(etDesc.text.toString())){
            etDesc.requestFocus()
            etDesc.error = "Description is empty"
        } else if(TextUtils.isEmpty(etorder.text.toString())){
            etorder.requestFocus()
            etorder.error = "ClientOrder is empty"
        } else if(TextUtils.isEmpty(etTrial.text.toString())){
            etTrial.requestFocus()
            etTrial.error = "Trial is empty"
        } else if(TextUtils.isEmpty(etDelivery.text.toString())){
            etDelivery.requestFocus()
            etDelivery.error = "Delivery is empty"
        } else if(TextUtils.isEmpty(etQuote.text.toString())){
            etQuote.requestFocus()
            etQuote.error = "Quote is empty"
        }
        return true
    }
}
