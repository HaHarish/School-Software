package com.newletseduvate.ui.circular.student

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.newletseduvate.R
import com.newletseduvate.adapter.dynamic_recycler_adapter.CallBackModel
import com.newletseduvate.base.BaseFragment
import com.newletseduvate.databinding.*
import com.newletseduvate.model.circular.student.CircularResultModel
import com.newletseduvate.utils.NetworkCheck
import com.newletseduvate.utils.constants.Constants
import com.newletseduvate.utils.extensions.datePickerDialog
import com.newletseduvate.utils.extensions.setUpRecyclerView
import com.newletseduvate.utils.extensions.snackBar
import com.newletseduvate.viewmodels.CircularViewModel

class CircularFragment : BaseFragment(R.layout.fragment_circular),View.OnClickListener{

    private lateinit var binding: FragmentCircularBinding
    private val viewModel by lazy { getViewModel<CircularViewModel>(requireActivity()) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCircularBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel

        onClickEvents()

        viewModel.moduleId = requireArguments().getInt(Constants.MODULE_ID)

        val callbacks = ArrayList<CallBackModel<CircularResultModel>>()
        callbacks.add(CallBackModel(R.id.btn_view_more) { model, position ->
            val bundle = Bundle()
            bundle.putSerializable(circularModel, model)
            findNavController().navigate(R.id.nav_circular_details, bundle)
        })

        binding.rvList.setUpRecyclerView<AdapterCircularBinding, CircularResultModel>(
            R.layout.adapter_circular,
            callbacks,
            viewModel.circularFilterResponse,
            viewLifecycleOwner,
            binding.root,
            {viewModel.circularFilter()},
            viewModel.isNoDataStudent,
            viewModel.totalPageStudent,
            viewModel.isPageLoadingStudent,
            viewModel.isDataLoadingStudent,
            viewModel.currentPageStudent
        ) {}

    }

    private fun onClickEvents() {
        binding.etFromDate.setOnClickListener(this)
        binding.etToDate.setOnClickListener(this)
        binding.btnApply.setOnClickListener(this)
    }

    override fun onResume() {
        super.onResume()
        setToolBarTitle(getString(R.string.circular))
    }

    override fun onClick(v: View?) {
        when(v?.id){
            binding.etFromDate.id -> {
                view?.datePickerDialog(viewModel.fromDate)
            }
            binding.etToDate.id -> {
                view?.datePickerDialog(viewModel.toDate)
            }
            binding.btnApply.id -> {
                if(NetworkCheck.isInternetAvailable(requireContext())){
                    viewModel.refreshPage()
                }else{
                    binding.root.snackBar(resources.getString(R.string.connect_internet))
                }
            }
        }
    }

    companion object {

        const val circularModel = "CircularModel"

        @JvmStatic
        fun newInstance() = CircularFragment()
    }
}