package com.newletseduvate.ui.finance

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.newletseduvate.R
import com.newletseduvate.adapter.AllTransactionsAdapter
import com.newletseduvate.adapter.MakePaymentAdapter
import com.newletseduvate.base.BaseFragment
import com.newletseduvate.utils.Status
import com.newletseduvate.utils.extensions.snackBar
import com.newletseduvate.viewmodels.ManageFinanceViewModel
import kotlinx.android.synthetic.main.fragment_all_transactions_finance.*
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.android.synthetic.main.fragment_make_payment_finance.*
import kotlinx.android.synthetic.main.fragment_make_payment_finance.rv_list_make_payment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AllTransactionsFinanceFragment: BaseFragment(R.layout.fragment_all_transactions_finance) {

    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var adapter: AllTransactionsAdapter

    private val viewModel by lazy { getViewModel<ManageFinanceViewModel>(this) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getAllTransactions()
        viewModel.getAllTransactionsResponse.observe(viewLifecycleOwner, {
            GlobalScope.launch {
                withContext(Dispatchers.Main) {
//                    dismissProgress()
                }
            }
            it?.let {
                GlobalScope.launch {
                    withContext(Dispatchers.Main) {
                        when (it.status) {
                            Status.Success -> {
                                adapter = AllTransactionsAdapter(it.data?.results!!)
                                rv_list_all_transactions.adapter = adapter
                                adapter.notifyDataSetChanged()
                            }
                            Status.Error -> {
                                layout_login.snackBar(it.message!!)
                            }
                            else -> {
                                layout_login.snackBar(it.message!!)
                            }
                        }
                    }
                }
            }
        })

        linearLayoutManager = LinearLayoutManager(requireContext())
        rv_list_all_transactions.layoutManager = linearLayoutManager
    }
}