package example.com.vestir.view

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import example.com.vestir.R
import example.com.vestir.database.entity.Order
import kotlinx.android.synthetic.main.activity_create_order.*

class CreateOrderActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_order)

        btnSubmit.setOnClickListener { /*addOrder()*/ }
    }

    private fun addOrder(){
        if(validateInpute()){
            val order = Order().apply {
                clientName = "Akash Gaikwad"
                name = etName.text.toString()
                style = etStyle.text.toString()
                description = etDesc.text.toString()
                status = spnStatus.selectedItem.toString()
                order = etorder.text.toString()
                trial = etTrial.text.toString()
                delivery = etDelivery.text.toString()
                quote = etQuote.text.toString()
            }
        }
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
            etorder.error = "Order is empty"
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
