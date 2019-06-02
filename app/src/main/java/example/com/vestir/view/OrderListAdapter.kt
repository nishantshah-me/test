package example.com.vestir.view

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.SparseBooleanArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import example.com.vestir.R
import example.com.vestir.database.entity.ClientOrder
import kotlinx.android.synthetic.main.row_order.view.*

/**
 * Created by Nishant on 01-Jun-19.
 */
class OrderListAdapter(var context: Context, var listener: OnOrderItemClickListener, var orderList: List<ClientOrder>?):
        RecyclerView.Adapter<OrderListAdapter.OrderViewHolder>() {

    private var selectedCount: Int = 0
    private var selectedOrders= ArrayList<ClientOrder>()
    private var selectedOrdersPosition = SparseBooleanArray()

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int {
        if(orderList == null)
            return 0
        else return orderList!!.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): OrderViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.row_order, parent, false)
        return OrderViewHolder(view)
    }

    private fun toggleOrder(position: Int){
        if(selectedOrdersPosition.get(position, false)){
            selectedOrdersPosition.removeAt(position)
            selectedOrders.remove(orderList!![position])
        } else {
            selectedOrdersPosition.put(position, true)
            selectedOrders.add(orderList!![position])
        }
    }

    fun setList(list: List<ClientOrder>?){
        if(list == null)
            orderList = ArrayList<ClientOrder>()
        else orderList = list
        selectedCount = 0
        selectedOrders= ArrayList<ClientOrder>()
        selectedOrdersPosition = SparseBooleanArray()
        notifyDataSetChanged()
    }

    fun getSelectedOrderList(): ArrayList<ClientOrder>{
        return selectedOrders
    }

    inner class OrderViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

        fun bind(position: Int){
            itemView.cb_order_select.setOnCheckedChangeListener { buttonView, isChecked ->
                if(isChecked) {
                    toggleOrder(adapterPosition)
                    selectedCount++
                }
                else {
                    selectedCount--
                }
                listener.onItemCheckChanged(selectedCount)
            }
        }
    }

    interface OnOrderItemClickListener{
        fun onItemCheckChanged(checkItemCount: Int)
    }
}