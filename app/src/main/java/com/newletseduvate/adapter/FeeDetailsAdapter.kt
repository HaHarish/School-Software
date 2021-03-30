package com.newletseduvate.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.newletseduvate.R
import com.newletseduvate.model.finance.FeeDetailsResponse
import com.newletseduvate.utils.extensions.openMediaFile
import java.lang.reflect.Array

class FeeDetailsAdapter (private val dataSet: ArrayList<FeeDetailsResponse.FeeDetailsResponseItem>) :
    RecyclerView.Adapter<FeeDetailsAdapter.ViewHolder>() {

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
     //   val viewMediaButton: Button
        val textFeeTypeName : TextView
        val textDiscount: TextView
        val textInstallAmount: TextView
        val textRefund: TextView
        val textPaidAmount: TextView
        val textBalance: TextView

        init {
            // Define click listener for the ViewHolder's View.
          //  viewMediaButton = view.findViewById(R.id.button_circular_details_media)
            textFeeTypeName = view.findViewById(R.id.tv_fee_type_name)
            textDiscount = view.findViewById(R.id.tv_discount)
            textInstallAmount = view.findViewById(R.id.tv_installment_amount)
            textRefund = view.findViewById(R.id.tv_refund)
            textPaidAmount = view.findViewById(R.id.tv_paid_amount)
            textBalance = view.findViewById(R.id.tv_balance)
        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.adapter_finance_manage_payment_fee_details, viewGroup, false)

        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        /*viewHolder.viewMediaButton.setOnClickListener {

        }*/

        viewHolder.textFeeTypeName.text = dataSet.get(position).feeType?.feeTypeName
        viewHolder.textDiscount.text = dataSet.get(position).discount.toString()
        viewHolder.textInstallAmount.text = dataSet.get(position).installments?.installmentAmount.toString()
        viewHolder.textRefund.text = "0"
        viewHolder.textPaidAmount.text = dataSet.get(position).amountPaid.toString()
        viewHolder.textBalance.text = dataSet.get(position).balance.toString()
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size

}