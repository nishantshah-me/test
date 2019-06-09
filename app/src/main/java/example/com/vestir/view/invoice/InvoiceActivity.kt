package example.com.vestir.view.invoice

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import example.com.vestir.R
import kotlinx.android.synthetic.main.activity_invoice.*

class InvoiceActivity : AppCompatActivity() {

    private lateinit var adapter: InvoiceListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_invoice)

        adapter = InvoiceListAdapter(this)
        rv_order_style_list.adapter = adapter
    }
}
