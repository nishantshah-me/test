package example.com.vestir.view.transaction

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.CardView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.LinearLayout
import example.com.vestir.R
import kotlinx.android.synthetic.main.activity_transaction.*
import kotlinx.android.synthetic.main.layout_generic_transaction.view.*
import kotlinx.android.synthetic.main.layout_toolbar.*

class TransactionActivity : AppCompatActivity() {
    lateinit var view : View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transaction)
        addTransactionRow()
        img_back.setOnClickListener { onBackPressed() }

        imgAddTransaction.setOnClickListener {
            addTransactionRow()
        }

        spnParticular.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                when(position){
                    7 -> addSalaryRow()
                    8 -> addVestExpRow()
                    else -> addTransactionRow()
                }
            }

        }
    }

    private fun addTransactionRow(){
        llSalary.visibility = View.GONE
        llTransaction.visibility = View.VISIBLE
        llVestExp.visibility = View.GONE

        val inflater = LayoutInflater.from(this)
        view =  inflater.inflate(R.layout.layout_generic_transaction,null)
        val params = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        view.isClickable = true

        params.topMargin = 16
        llTransaction.addView(view,params)

        view.imgDelCard.setOnClickListener {
            val parentView = ((it.parent as LinearLayout).parent as CardView).parent as LinearLayout
            val index = llTransaction.indexOfChild(parentView)
            llTransaction.removeViewAt(index)
        }
    }

    private fun addSalaryRow(){
        llSalary.visibility = View.VISIBLE
        llTransaction.visibility = View.GONE
        llVestExp.visibility = View.GONE

        val inflater = LayoutInflater.from(this)
        view =  inflater.inflate(R.layout.layout_staff_salary,null)
        val params = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        view.isClickable = true

        params.topMargin = 16
        llSalary.addView(view,params)

    }

    private fun addVestExpRow(){
        llSalary.visibility = View.GONE
        llTransaction.visibility = View.GONE
        llVestExp.visibility = View.VISIBLE

        val inflater = LayoutInflater.from(this)
        view =  inflater.inflate(R.layout.layout_vestir_expense,null)
        val params = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        view.isClickable = true

        params.topMargin = 16
        llVestExp.addView(view,params)
    }
}
