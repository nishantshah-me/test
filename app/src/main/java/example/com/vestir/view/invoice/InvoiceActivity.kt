package example.com.vestir.view.invoice

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import example.com.vestir.R
import example.com.vestir.database.entity.ClientOrder
import kotlinx.android.synthetic.main.activity_invoice.*
import kotlinx.android.synthetic.main.layout_toolbar.*
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import android.support.v4.content.FileProvider



class InvoiceActivity : AppCompatActivity() {

    private lateinit var adapter: InvoiceListAdapter
    private lateinit var orderList: ArrayList<ClientOrder>
    private var advance: Long = 0
    private var final: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_invoice)

        orderList = intent.getSerializableExtra("order_list") as ArrayList<ClientOrder>
        adapter = InvoiceListAdapter(this, orderList)
        rv_order_style_list.adapter = adapter

        orderList.forEach {
            advance += it.advance
            final += it.cost
        }

        txt_advance_cost.text = "$advance Rs"
        txt_final_cost.text = "${final - advance} Rs"

        img_back.setOnClickListener { onBackPressed() }

        img_share.visibility = View.VISIBLE
        img_share.setOnClickListener {
            shareInvoice()
        }
    }

    private fun shareInvoice(){
        rl_progressbar.visibility = View.VISIBLE
        cl_invoice.post {
            try {
                val bitmap = Bitmap.createBitmap(cl_invoice.measuredWidth, cl_invoice.measuredHeight,
                        Bitmap.Config.ARGB_8888)
                val canvas = Canvas(bitmap)
                nsv_invoice.draw(canvas)
                // dir = File("${applicationContext.filesDir}/TEMP")
                val dir = File("${Environment.getExternalStorageDirectory()}/TEMP")
                if(!dir.exists())
                    dir.mkdir()
                val file = File("$dir/ORDER_SHARE.jpeg")
                val outputStream = FileOutputStream(file)
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
                rl_progressbar.visibility = View.GONE
                val uri = Uri.fromFile(file)
                //val uri = FileProvider.getUriForFile(this, "{$packageName}.fileprovider", file)
                val intent = Intent(Intent.ACTION_SEND)
                intent.type = "image/*"
                //intent.`package` = "com.whatsapp"
                intent.putExtra(android.content.Intent.EXTRA_STREAM, uri)
                startActivity(Intent.createChooser(intent, "Share with"))
            } catch (e: Exception){
                rl_progressbar.visibility = View.GONE
                Toast.makeText(this, "Something went wrong...", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
