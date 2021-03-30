package com.newletseduvate.ui.finance

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.newletseduvate.R
import com.newletseduvate.adapter.FeeDetailsAdapter
import com.newletseduvate.base.BaseFragment
import com.newletseduvate.utils.Status
import com.newletseduvate.utils.extensions.snackBar
import com.newletseduvate.viewmodels.ManageFinanceViewModel
import kotlinx.android.synthetic.main.fragment_fees_details_finance.*
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FeesDetailsFinanceFragment: BaseFragment(R.layout.fragment_fees_details_finance) {

    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var adapter: FeeDetailsAdapter

//    private lateinit var binding: FragmentFeesDetailsFinanceBinding
    private val viewModel by lazy { getViewModel<ManageFinanceViewModel>(this) }
//    private lateinit var adapter: RecyclerDynamicAdapter<AdapterFinanceManagePaymentFeeDetailsBinding, FeeDetailsResponse.FeeDetailsResponseItem>

    /*override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
       // binding = FragmentFeesDetailsFinanceBinding.inflate(inflater, container, false)
       // return binding.root
    }*/

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

      //  binding.viewModel = viewModel

        /*val callbacks = ArrayList<CallBackModel<FeeDetailsResponse.FeeDetailsResponseItem>>()

        adapter = binding.rvList.setUpRecyclerView(
            R.layout.adapter_finance_manage_payment_fee_details,
            callbacks,
            viewModel.getFeesDetailsResponse,
            viewLifecycleOwner,
            binding.root,
            {
                viewModel.getFeeDetails()
            },
            viewModel.isNoDataStudent,
            isPageLoadingDaily = viewModel.isPageLoadingStudent,
            currentPageDaily = MutableLiveData(1),
            totalPageDaily = ObservableInt(1)
        ) {}*/

        viewModel.getFeeDetails()
        viewModel.getFeesDetailsResponse.observe(viewLifecycleOwner, {
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
                                adapter = FeeDetailsAdapter(it.data!!)
                                rv_list_fee_details.adapter = adapter
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
        rv_list_fee_details.layoutManager = linearLayoutManager

    }

    override fun onResume() {
        super.onResume()
        setToolBarTitle(getString(R.string.menu_manage_payment))
    }
}