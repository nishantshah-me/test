package example.com.vestir.view.clientorder

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.util.SparseBooleanArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import example.com.vestir.ORDER_DATE_FORMAT
import example.com.vestir.R
import example.com.vestir.database.entity.ClientOrder
import kotlinx.android.synthetic.main.row_order.view.*
import java.text.SimpleDateFormat

/**
 * Created by Nishant on 01-Jun-19.
 */
class OrderListAdapter(var context: Context, var listener: OnOrderItemClickListener, var orderList: List<ClientOrder>?) :
        RecyclerView.Adapter<OrderListAdapter.OrderViewHolder>() {

    private var selectedCount: Int = 0
    private var selectedOrders = ArrayList<ClientOrder>()
    private var selectedOrdersPosition = SparseBooleanArray()
    private var originalList: List<ClientOrder>? = null
    private var selectedClientId: Long = 0
    private var isSelectByUser: Boolean = true

    init {
        originalList = orderList
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int {
        if (orderList == null)
            return 0
        else return orderList!!.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): OrderViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.row_order, parent, false)
        return OrderViewHolder(view)
    }

    private fun toggleOrder(position: Int) {
        if (selectedOrdersPosition.get(position, false)) {
            selectedOrdersPosition.delete(position)
            selectedOrders.remove(orderList!![position])
        } else {
            selectedOrdersPosition.put(position, true)
            selectedOrders.add(orderList!![position])
        }
    }

    fun setList(list: List<ClientOrder>?) {
        if (list == null)
            orderList = ArrayList<ClientOrder>()
        else {
            orderList = list
        }
        originalList = orderList
        selectedCount = 0
        selectedOrders = ArrayList<ClientOrder>()
        selectedOrdersPosition = SparseBooleanArray()
        notifyDataSetChanged()
    }

    fun getOriginalList(): List<ClientOrder>?{
        return originalList
    }

    fun getSelectedOrderList(): ArrayList<ClientOrder> {
        return selectedOrders
    }

    inner class OrderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(position: Int) {
            val order = orderList!![position]
            itemView.txt_client_name.text = order.name
            itemView.txt_style.text = order.style
            itemView.txt_status.text = order.status
            itemView.txt_trial_date.text = order.trial
            itemView.txt_delivery_date.text = order.delivery
            itemView.cb_order_select.setOnCheckedChangeListener { buttonView, isChecked ->

                if(isSelectByUser) {
                    if (selectedClientId == 0L || selectedClientId == orderList!![position].clientId) {
                        if (isChecked)
                            selectedCount++
                        else
                            selectedCount--
                        selectedClientId = orderList!![position].clientId
                        toggleOrder(adapterPosition)
                        listener.onItemCheckChanged(selectedCount)
                    } else {
                        isSelectByUser = false
                        itemView.cb_order_select.isChecked = false
                        listener.onDifferentClientSelected()
                        isSelectByUser = true
                    }
                }
            }

            itemView.btn_cost.setOnClickListener {
                listener.onCostClick(selectedOrders)
            }
        }
    }

    fun filterByDate(dateType: String, fromDate: String, toDate: String) {
        if (dateType.isNotEmpty()) {
            val sdf = SimpleDateFormat(ORDER_DATE_FORMAT)
            val from = sdf.parse(fromDate)
            val to = sdf.parse(toDate)
            val tempList: List<ClientOrder> = originalList!!.filter {
                when (dateType) {
                    context.getString(R.string.menu_trial_date) -> it.trial != ""
                    context.getString(R.string.menu_delivery_date) -> it.delivery != ""
                    else -> it.order != ""
                }
            }.filter {
                val date = when (dateType) {
                    context.getString(R.string.menu_trial_date) -> sdf.parse(it.trial)
                    context.getString(R.string.menu_delivery_date) -> sdf.parse(it.delivery)
                    else -> sdf.parse(it.order)
                }
                (date.compareTo(from) == 0) || (date.compareTo(to) == 0) ||
                        (date.before(to) && date.after(from))
            }
            orderList = tempList
        } else {
            orderList = originalList
            orderList!!
        }
        notifyDataSetChanged()
    }

    fun filterByClintName(clientName: String) {
        if (clientName.isNotEmpty()) {
            val tempList: List<ClientOrder> = originalList!!.filter {
                it.name == clientName
            }
            orderList = tempList
        } else {
            orderList = originalList
            orderList!!
        }
        notifyDataSetChanged()
    }

    fun resetOriginalList() {
        orderList = originalList
        notifyDataSetChanged()
    }

    interface OnOrderItemClickListener {
        fun onItemCheckChanged(checkItemCount: Int)
        fun onCostClick(selectedOrders: ArrayList<ClientOrder>)
        fun onDifferentClientSelected()
    }
}