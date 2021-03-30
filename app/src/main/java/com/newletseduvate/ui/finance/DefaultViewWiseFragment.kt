package com.newletseduvate.ui.finance

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ObservableInt
import androidx.lifecycle.MutableLiveData
import com.newletseduvate.R
import com.newletseduvate.adapter.dynamic_recycler_adapter.CallBackModel
import com.newletseduvate.adapter.dynamic_recycler_adapter.RecyclerDynamicAdapter
import com.newletseduvate.base.BaseFragment
import com.newletseduvate.databinding.AdapterFinanceDueDateWiseFeeBinding
import com.newletseduvate.databinding.FragmentDefaultViewWiseBinding
import com.newletseduvate.model.finance.StudentFeedDisplayResponse
import com.newletseduvate.utils.extensions.setUpRecyclerView
import com.newletseduvate.viewmodels.FeeStructureViewModel

class DefaultViewWiseFragment : BaseFragment(R.layout.fragment_default_view_wise) {

    private lateinit var binding: FragmentDefaultViewWiseBinding
    private val viewModel by lazy { getViewModel<FeeStructureViewModel>(requireActivity()) }
    private lateinit var adapter: RecyclerDynamicAdapter<AdapterFinanceDueDateWiseFeeBinding, StudentFeedDisplayResponse>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentDefaultViewWiseBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewModel = viewModel

        val callbacks = ArrayList<CallBackModel<StudentFeedDisplayResponse>>()

        adapter = binding.rvList.setUpRecyclerView(
            R.layout.adapter_finance_default_view_wise_fee,
            callbacks,
            viewModel.financeFeeListResponse,
            viewLifecycleOwner,
            binding.root,
            {
                viewModel.getStudentFeeDisplay()
            },
            viewModel.isNoDataStudent,
            isPageLoadingDaily = viewModel.isPageLoadingStudent,
            currentPageDaily = MutableLiveData(1),
            totalPageDaily = ObservableInt(1)
        ) {}

    }

}