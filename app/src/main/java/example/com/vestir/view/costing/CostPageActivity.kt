package example.com.vestir.view.costing

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import example.com.vestir.R
import kotlinx.android.synthetic.main.activity_cost_page.*

class CostPageActivity : AppCompatActivity() {

    private lateinit var  adapter : CostPageAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cost_page)

        adapter = CostPageAdapter(this)
        rvCost.adapter = adapter

    }
}
