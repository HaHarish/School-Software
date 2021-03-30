package com.newletseduvate.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.newletseduvate.R
import com.newletseduvate.model.finance.FeeDetailsResponse
import com.newletseduvate.model.finance.MakePaymentResponse
import com.newletseduvate.ui.finance.ChoosePaymentInterface

class MakePaymentAdapter (private val dataSet: ArrayList<MakePaymentResponse.MakePaymentResponseItem.InstallmentsData>,
                            private val choosePaymentInterface: ChoosePaymentInterface) :
    RecyclerView.Adapter<MakePaymentAdapter.ViewHolder>() {

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        //   val viewMediaButton: Button
        val tvInstallmentName : TextView = view.findViewById(R.id.tv_makePayment_installment_name_value)
        val tvFeeType: TextView = view.findViewById(R.id.tv_makePayment_fee_type_value)
        val tvAmount: TextView = view.findViewById(R.id.tv_makePayment_amount_value)
        val tvPaidAmount: TextView = view.findViewById(R.id.tv_makePayment_paid_amount_value)
        val tvBalance: TextView = view.findViewById(R.id.tv_makePayment_balance_value)
        val tvConcession: TextView = view.findViewById(R.id.tv_makePayment_concession_value)
        val tvFineAmount: TextView = view.findViewById(R.id.tv_makePayment_fine_amount_value)
        val tvDueDate: TextView = view.findViewById(R.id.tv_makePayment_due_date_value)
        val tvPartialAmount: TextView = view.findViewById(R.id.tv_makePayment_partial_payment_value)
        val cbChoosePayment: CheckBox = view.findViewById(R.id.cb_adapter_makePayment)

    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.adapter_finance_make_payment, viewGroup, false)

        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        /*viewHolder.viewMediaButton.setOnClickListener {

        }*/

        viewHolder.tvInstallmentName.text = dataSet.get(position).installmentsName
        viewHolder.tvFeeType.text = dataSet.get(position).feeType
        viewHolder.tvAmount.text = dataSet.get(position).installmentAmount.toString()
        viewHolder.tvPaidAmount.text = dataSet.get(position).amountPaid.toString()
        viewHolder.tvBalance.text = dataSet.get(position).balance.toString()
        viewHolder.tvConcession.text = dataSet.get(position).discount.toString()
        viewHolder.tvFineAmount.text = dataSet.get(position).fineAmount.toString()
        viewHolder.tvDueDate.text = dataSet.get(position).dueDate.toString()
        viewHolder.tvPartialAmount.text = "No"

        viewHolder.cbChoosePayment.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked){
                choosePaymentInterface.onClickedCheckBox(position, true)
            }else{
                choosePaymentInterface.onClickedCheckBox(position, false)
            }
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size

}