package com.newletseduvate.ui.bindings

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.newletseduvate.R
import com.newletseduvate.adapter.dynamic_recycler_adapter.CallBackModel
import com.newletseduvate.adapter.dynamic_recycler_adapter.RecyclerDynamicAdapter
import com.newletseduvate.databinding.AdapterFinanceFeeInstallmentBinding
import com.newletseduvate.databinding.AdapterFinanceFeeInstallmentDueDateWiseBinding
import com.newletseduvate.model.finance.StudentFeedDisplayResponse

object BindingsFinanceFragment {

    @JvmStatic
    @BindingAdapter(value = ["bindFinanceFeeDisplayList", "bindFinanceStudentDetailsModel"], requireAll = true)
    fun bindFinanceFeeDisplayList(recyclerView: RecyclerView, arrayList: ArrayList<StudentFeedDisplayResponse.Installment>?, feeModel:StudentFeedDisplayResponse) {

        arrayList?.let {
            val linearLayoutManager = LinearLayoutManager(recyclerView.context, RecyclerView.VERTICAL, false)
            recyclerView.layoutManager = linearLayoutManager

            val callbacks = ArrayList<CallBackModel<StudentFeedDisplayResponse.Installment>>()

            val adapter =
                RecyclerDynamicAdapter<AdapterFinanceFeeInstallmentDueDateWiseBinding, StudentFeedDisplayResponse.Installment>(R.layout.adapter_finance_fee_installment_due_date_wise, callbacks)
            recyclerView.adapter = adapter
            recyclerView.setHasFixedSize(true)

            val tempList = ArrayList<StudentFeedDisplayResponse.Installment>()

            it.forEach { model ->
                model.feeTypeName = feeModel.feeTypeName
                model.feeTypeAmount = feeModel.feeTypeAmount
                tempList.add(model)
            }

            adapter.submitList(arrayList)
        }
    }

    @JvmStatic
    @BindingAdapter(value = ["bindFinanceDefaultFeeDisplayList"], requireAll = true)
    fun bindFinanceDefaultFeeDisplayList(recyclerView: RecyclerView, arrayList: ArrayList<StudentFeedDisplayResponse.Installment>?) {

        arrayList?.let {
            val linearLayoutManager = LinearLayoutManager(recyclerView.context, RecyclerView.VERTICAL, false)
            recyclerView.layoutManager = linearLayoutManager

            val callbacks = ArrayList<CallBackModel<StudentFeedDisplayResponse.Installment>>()

            val adapter =
                RecyclerDynamicAdapter<AdapterFinanceFeeInstallmentBinding, StudentFeedDisplayResponse.Installment>(R.layout.adapter_finance_fee_installment, callbacks)
            recyclerView.adapter = adapter
            recyclerView.setHasFixedSize(true)

            adapter.submitList(arrayList)
        }
    }

}