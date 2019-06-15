package example.com.vestir.view.clientorder

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.widget.Toast
import example.com.vestir.*
import example.com.vestir.R.id.*
import example.com.vestir.database.AppDatabase
import example.com.vestir.database.entity.ClientOrder
import example.com.vestir.view.client.MeasurementActivity
import kotlinx.android.synthetic.main.activity_create_order.*
import kotlinx.android.synthetic.main.layout_toolbar.*
import java.text.SimpleDateFormat
import java.util.*

class CreateOrderActivity : AppCompatActivity() {
    private lateinit var database: AppDatabase
    private var isUpdate: Boolean = false
    private var updateOrderId: Long = 0
    private var selectedClientId: Long = 0
    private var isEdited: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_order)

        database = AppDatabase.getInstance(this)
        selectedClientId = intent.getLongExtra(SELECT_CLIENT_ID, 0)
        if (intent.getBooleanExtra(IS_FOR_UPDATE, false) && intent.hasExtra(ORDER_DATA)) {
            isUpdate = true
            btnSubmit.text = getString(R.string.str_update)
            val order = intent.getSerializableExtra(ORDER_DATA) as ClientOrder
            showData(order)
        } else {
            spnStatus.isEnabled = false
            etName.text = intent.getStringExtra(SELECTED_CLIENT_NAME)
            val sdf = SimpleDateFormat(ORDER_DATE_FORMAT)
            etorder.text = sdf.format(Calendar.getInstance().time)
        }

        btnSubmit.setOnClickListener { addOrder() }
        img_back.setOnClickListener { onBackPressed() }
        txt_measurements.setOnClickListener { onMeasurementsClick() }
        etTrial.setOnClickListener {
            if (!TextUtils.isEmpty(etorder.text.toString())) {
                try {
                    val sdf = SimpleDateFormat(ORDER_DATE_FORMAT)
                    val minDate = sdf.parse(etorder.text.toString().trim())
                    showDatePicker("trial", minDate.time)
                } catch (e: Exception) {

                }
            }
        }
        etDelivery.setOnClickListener {
            if (!TextUtils.isEmpty(etTrial.text.toString())) {
                try {
                    val sdf = SimpleDateFormat(ORDER_DATE_FORMAT)
                    val minDate = sdf.parse(etTrial.text.toString().trim())
                    showDatePicker("delivery", minDate.time)
                } catch (e: Exception) {

                }
            }
        }
    }

    private fun showDatePicker(date: String, minDate: Long) {
        val calendar = Calendar.getInstance()
        val datePickerDialog = DatePickerDialog(this@CreateOrderActivity,
                DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                    val cal = Calendar.getInstance()
                    cal.set(Calendar.YEAR, year)
                    cal.set(Calendar.MONTH, month)
                    cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                    try {
                        val sdf = SimpleDateFormat(ORDER_DATE_FORMAT)
                        when (date) {
                            "order" -> etorder.text = sdf.format(cal.time)
                            "trial" -> etTrial.text = sdf.format(cal.time)
                            "delivery" -> etDelivery.text = sdf.format(cal.time)
                        }
                    } catch (e: Exception) {

                    }
                }, calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH))
        datePickerDialog.datePicker.minDate = minDate
        datePickerDialog.show()
    }

    private fun addOrder() {
        if (validateInput()) {
            var quoteValue = etQuote.text.toString()
            if(TextUtils.isEmpty(quoteValue))
                quoteValue = "0"
            val order = ClientOrder().apply {
                clientId = selectedClientId
                name = etName.text.toString()
                style = etStyle.text.toString()
                description = etDesc.text.toString()
                status = spnStatus.selectedItem.toString()
                order = etorder.text.toString()
                trial = etTrial.text.toString()
                delivery = etDelivery.text.toString()
                quote = quoteValue.toLong()
            }
            var message = "Order added successfully"
            if (isUpdate) {
                order.orderId = updateOrderId
                database.orderDao().updateOrder(order)
                message = "Order updated successfully"
            } else {
                database.orderDao().addOrder(order)
            }
            isEdited = true
            Toast.makeText(this@CreateOrderActivity, message, Toast.LENGTH_SHORT).show()
            //setResult(Activity.RESULT_OK)
            //finish()
        }
    }

    private fun showData(order: ClientOrder) {
        updateOrderId = order.orderId
        etName.text = order.name
        etName.isEnabled = false
        etStyle.setText(order.style)
        etDesc.setText(order.description)

        val statusList = resources.getStringArray(R.array.statusList)
        var index = statusList.indexOf(order.status)
        index = if (index < 0) 0 else index
        spnStatus.setSelection(index)

        etorder.text = order.order
        etTrial.text = order.trial
        etDelivery.text = order.delivery
        etQuote.setText(order.quote.toString())
    }

    private fun validateInput(): Boolean {
        if (TextUtils.isEmpty(etStyle.text.toString())) {
            etStyle.requestFocus()
            etStyle.error = "Style is empty"
            return false
        }
        return true
    }

    private fun onMeasurementsClick(){
        val intent = Intent(this@CreateOrderActivity, MeasurementActivity::class.java)
        intent.putExtra("clientId", selectedClientId)
        startActivity(intent)
    }

    override fun onBackPressed() {
        if(isEdited){
            setResult(Activity.RESULT_OK)
        }
        finish()
    }
}
