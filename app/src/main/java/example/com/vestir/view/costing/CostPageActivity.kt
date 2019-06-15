package example.com.vestir.view.costing

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import example.com.vestir.R
import example.com.vestir.SELECTED_ORDERS
import example.com.vestir.database.entity.ClientOrder
import kotlinx.android.synthetic.main.activity_cost_page.*

class CostPageActivity : AppCompatActivity(), TextWatcher {

    private lateinit var adapter: CostPageAdapter
     var orderList: List<ClientOrder> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cost_page)
        if (intent.extras != null)
            orderList = (intent.extras.getSerializable(SELECTED_ORDERS)) as List<ClientOrder>

        setAdapter()

        etStyleName.addTextChangedListener(this)

    }

    override fun afterTextChanged(s: Editable?) {
        //orderList = orderList.filter { it.style == s.toString() }

      //  setAdapter()
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
    }

    fun setAdapter(){
        if (orderList.isNotEmpty()) {
            adapter = CostPageAdapter(this, orderList)
            rvCost.adapter = adapter
        }
    }
}
