package example.com.vestir.view.costing

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import example.com.vestir.R

class CostPageAdapter(var context : Context) : RecyclerView.Adapter<CostPageAdapter.CostPageViewHolder>() {

    override fun onBindViewHolder(p0: CostPageViewHolder, p1: Int) {
        p0.bind(p1)
    }


    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): CostPageViewHolder {

        val view = LayoutInflater.from(context).inflate(R.layout.cost_page_item, p0, false)
        return CostPageViewHolder(view)
    }

    override fun getItemCount(): Int {
        return 3
    }

    inner class CostPageViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        fun bind(position: Int) {

        }

    }
}