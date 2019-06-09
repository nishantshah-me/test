package example.com.vestir.view.clientorder

import android.app.Activity
import android.app.DatePickerDialog
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import example.com.vestir.*
import example.com.vestir.database.AppDatabase
import example.com.vestir.database.entity.ClientOrder
import kotlinx.android.synthetic.main.activity_create_order.*
import kotlinx.android.synthetic.main.layout_toolbar.*
import java.text.SimpleDateFormat
import java.util.*

class CreateOrderActivity : AppCompatActivity() {
    private lateinit var database: AppDatabase
    private var isUpdate: Boolean = false
    private var updateOrderId: Long = 0
    private var selectedClientId: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_order)

        database = AppDatabase.getInstance(this)
        selectedClientId = intent.getLongExtra(SELECT_CLIENT_ID, 0)
        if(intent.getBooleanExtra(IS_FOR_UPDATE, false) && intent.hasExtra(ORDER_DATA)){
            isUpdate = true
            btnSubmit.text = getString(R.string.str_update)
            val order = intent.getSerializableExtra(ORDER_DATA) as ClientOrder
            showData(order)
        } else {
            etName.setText(intent.getStringExtra(SELECTED_CLIENT_NAME))
            etName.isEnabled = false
        }

        btnSubmit.setOnClickListener { addOrder() }
        img_back.setOnClickListener { onBackPressed() }
        etorder.setOnClickListener {
            showDatePicker("order")
        }
        etTrial.setOnClickListener {
            showDatePicker("trial")
        }
        etDelivery.setOnClickListener {
            showDatePicker("delivery")
        }
    }

    private fun showDatePicker(date: String){
        val calendar = Calendar.getInstance()
        DatePickerDialog(this@CreateOrderActivity,
                DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                    val cal = Calendar.getInstance()
                    cal.set(Calendar.YEAR, year)
                    cal.set(Calendar.MONTH, month)
                    cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                    val sdf = SimpleDateFormat(ORDER_DATE_FORMAT)
                    when(date){
                        "order" -> etorder.text = sdf.format(cal.time)
                        "trial" -> etTrial.text = sdf.format(cal.time)
                        "delivery" -> etDelivery.text = sdf.format(cal.time)
                    }
                }, calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show()
    }

    private fun addOrder(){
        if(validateInpute()){
            val order = ClientOrder().apply {
                clientId = selectedClientId
                name = etName.text.toString()
                style = etStyle.text.toString()
                description = etDesc.text.toString()
                status = spnStatus.selectedItem.toString()
                order = etorder.text.toString()
                trial = etTrial.text.toString()
                delivery = etDelivery.text.toString()
                quote = etQuote.text.toString().toLong()
            }
            var message = "Order added successfully"
            if(isUpdate){
                order.orderId = updateOrderId
                database.orderDao().updateOrder(order)
                message = "Order updated successfully"
            } else {
                database.orderDao().addOrder(order)
            }
            Toast.makeText(this@CreateOrderActivity, message , Toast.LENGTH_SHORT).show()
            setResult(Activity.RESULT_OK)
            finish()
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
        etQuote.setText(order.quote.toString())
    }

    private fun validateInpute() : Boolean{
        if(TextUtils.isEmpty(etName.text.toString())){
            etName.requestFocus()
            etName.error = "Name is empty"
            return false
        } else if(TextUtils.isEmpty(etStyle.text.toString())){
            etStyle.requestFocus()
            etStyle.error = "Style is empty"
            return false
        } else if(TextUtils.isEmpty(etDesc.text.toString())){
            etDesc.requestFocus()
            etDesc.error = "Description is empty"
            return false
        } else if(TextUtils.isEmpty(etorder.text.toString())){
            etorder.requestFocus()
            etorder.error = "Order is empty"
            return false
        } else if(TextUtils.isEmpty(etTrial.text.toString())){
            etTrial.requestFocus()
            etTrial.error = "Trial is empty"
            return false
        } else if(TextUtils.isEmpty(etDelivery.text.toString())){
            etDelivery.requestFocus()
            etDelivery.error = "Delivery is empty"
            return false
        } else if(TextUtils.isEmpty(etQuote.text.toString())){
            etQuote.requestFocus()
            etQuote.error = "Quote is empty"
            return false
        }
        return true
    }
}
