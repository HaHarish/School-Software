package com.newletseduvate.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.newletseduvate.R
import com.newletseduvate.model.finance.AllTransactionsResponse
import com.newletseduvate.model.finance.FeeDetailsResponse

class AllTransactionsAdapter (private val dataSet: ArrayList<AllTransactionsResponse.Result>) :
    RecyclerView.Adapter<AllTransactionsAdapter.ViewHolder>() {

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvTransactionId : TextView = view.findViewById(R.id.tv_transaction_id)
        val tvReceiptNo: TextView = view.findViewById(R.id.tv_receipt_no)
        val tvDateOfPayment: TextView = view.findViewById(R.id.tv_date_of_payment)
        val tvPaymentMode: TextView = view.findViewById(R.id.tv_payment_mode)
        val tvFeeType: TextView = view.findViewById(R.id.tv_fee_type)
        val tvAmountPaid: TextView = view.findViewById(R.id.tv_amount_paid)
        val tvTotal: TextView = view.findViewById(R.id.tv_total)
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.adapter_finance_manage_payment_all_transactions, viewGroup, false)

        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        /*viewHolder.viewMediaButton.setOnClickListener {

        }*/

        viewHolder.tvTransactionId.text = dataSet[position].transactionId.toString()
        viewHolder.tvReceiptNo.text = dataSet[position].recepitNo.toString()
        viewHolder.tvDateOfPayment.text = dataSet[position].dateOfPayment.toString()
        viewHolder.tvPaymentMode.text = dataSet[position].paymentMode.toString()
        viewHolder.tvFeeType.text = dataSet[position].feeType?.get(0)?.feeTypeInstallment.toString()
        viewHolder.tvAmountPaid.text = dataSet[position].feeType?.get(0)?.paidAmount.toString()
        viewHolder.tvTotal.text = dataSet[position].feeType?.get(0)?.paidAmount.toString()
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size

}