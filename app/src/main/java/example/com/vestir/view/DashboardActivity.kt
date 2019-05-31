package example.com.vestir.view

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import example.com.vestir.R
import kotlinx.android.synthetic.main.activity_dashboard.*

class DashboardActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        titleNewClient.setOnClickListener({
            startActivity(Intent(this, NewClientActivity::class.java))
        })
    }
}
