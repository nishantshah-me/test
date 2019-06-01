package example.com.vestir.view

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import example.com.vestir.R

/**
 * Created by Nishant on 01-Jun-19.
 */
class OrderListAdapter(var context: Context): RecyclerView.Adapter<OrderListAdapter.OrderViewHolder>() {
    override fun onBindViewHolder(holder: OrderViewHolder, p1: Int) {
        holder.bind()
    }

    override fun getItemCount(): Int {
        return 2
    }

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): OrderViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.row_order, parent, false)
        return OrderViewHolder(view)
    }

    inner class OrderViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

        fun bind(){

        }
    }
}