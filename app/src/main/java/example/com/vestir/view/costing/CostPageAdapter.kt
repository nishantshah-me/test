package example.com.vestir.view.costing

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import example.com.vestir.R
import example.com.vestir.database.entity.ClientOrder
import kotlinx.android.synthetic.main.cost_page_item.view.*

class CostPageAdapter(var context: Context, orderList: List<ClientOrder>) : RecyclerView.Adapter<CostPageAdapter.CostPageViewHolder>() {
        var mContext : Context
        var orders : List<ClientOrder>
    init {
        mContext = context
        orders = orderList
    }

    override fun onBindViewHolder(p0: CostPageViewHolder, p1: Int) {
        p0.bind(p1)
    }


    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): CostPageViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.cost_page_item, p0, false)
        return CostPageViewHolder(view)
    }

    override fun getItemCount(): Int {
        return orders.size
    }

    inner class CostPageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(position: Int) {
            itemView.txtStyle.setText(orders[position].style)
        }

    }
}