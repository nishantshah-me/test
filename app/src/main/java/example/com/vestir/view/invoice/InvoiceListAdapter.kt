package example.com.vestir.view.invoice

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import example.com.vestir.R
import example.com.vestir.database.entity.ClientOrder
import kotlinx.android.synthetic.main.row_order_invoice.view.*

/**
 * Created by Nishant on 09-Jun-19.
 */
class InvoiceListAdapter(val context: Context, private val orderList: ArrayList<ClientOrder>):
        RecyclerView.Adapter<InvoiceListAdapter.InvoiceViewHolder>() {
    override fun onBindViewHolder(holder: InvoiceViewHolder, position: Int) {
        holder.onBind(orderList[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): InvoiceViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.row_order_invoice, parent, false)
        return InvoiceViewHolder(view)
    }

    override fun getItemCount(): Int = orderList.size

    inner class InvoiceViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

        fun onBind(order: ClientOrder){
            itemView.txt_order_style.text = order.style
            itemView.txt_order_cost.text = "${order.cost} Rs"
        }
    }
}