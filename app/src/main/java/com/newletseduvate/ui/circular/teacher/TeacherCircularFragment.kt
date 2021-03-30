package com.newletseduvate.ui.circular.teacher

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.newletseduvate.R
import com.newletseduvate.adapter.dynamic_recycler_adapter.CallBackModel
import com.newletseduvate.base.BaseFragment
import com.newletseduvate.databinding.AdapterCircularTeacherFilterBinding
import com.newletseduvate.databinding.FragmentCircularTeacherBinding
import com.newletseduvate.model.circular.teacher.CircularTeacherFilterResponse
import com.newletseduvate.utils.Status
import com.newletseduvate.utils.constants.Constants
import com.newletseduvate.utils.extensions.setUpRecyclerView
import com.newletseduvate.utils.extensions.snackBar
import com.newletseduvate.viewmodels.CircularTeacherViewModel
import kotlinx.android.synthetic.main.fragment_circular_teacher.*
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TeacherCircularFragment: BaseFragment(R.layout.fragment_circular_teacher) {

    private lateinit var binding: FragmentCircularTeacherBinding
    private val viewModel by lazy { getViewModel<CircularTeacherViewModel>(requireActivity()) }

    private var moduelId: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCircularTeacherBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewModel = viewModel

        viewModel.moduleId = requireArguments().getInt(Constants.MODULE_ID)

        binding.buttonTeacherCircularCreate.setOnClickListener {
            viewModel.isCreateClassClicked.set(true)
            viewModel.isAttachClicked.set(false)
            findNavController().navigate(R.id.nav_circular_teacher_filter)
        }

        binding.tvCircularTeacherFilter.setOnClickListener {
            viewModel.isCreateClassClicked.set(false)
            viewModel.isAttachClicked.set(false)
            findNavController().navigate(R.id.nav_circular_teacher_filter)
        }

        val callbacks = ArrayList<CallBackModel<CircularTeacherFilterResponse.Result>>()
        callbacks.add(CallBackModel(R.id.button_circularTeacherAdapter_viewMore) { model, position ->
            val bundle = Bundle()
            bundle.putSerializable("ViewMore", model)
            findNavController().navigate(R.id.nav_circular_teacher_view_more, bundle)
        })
        callbacks.add(CallBackModel(R.id.iv_circularTeacher_delete) { model, position ->
            viewModel.circularId.set(model.id!!)
            viewModel.circularTeacherDelete()
        })

        binding.rvCircularTeacherFilterResults.setUpRecyclerView<AdapterCircularTeacherFilterBinding, CircularTeacherFilterResponse.Result>(
            R.layout.adapter_circular_teacher_filter,
            callbacks,
            viewModel.circularTeacherFilterResponse,
            viewLifecycleOwner,
            binding.root,
            {viewModel.circularTeacherFilterResults()},
            viewModel.isNoDataStudent,
            viewModel.totalPageStudent,
            viewModel.isPageLoadingStudent,
            viewModel.isDataLoadingStudent,
            viewModel.currentPageStudent
        ) {}

        viewModel.circularTeacherDeleteResponse.observe(viewLifecycleOwner, {
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
                                it.data?.message?.let { it1 -> binding.root.snackBar(it1) }
                                viewModel.circularTeacherFilterResults()
                            }
                            Status.Error -> {
                                binding.root.snackBar(it.data?.message!!)
                            }
                            else -> {
                                binding.root.snackBar(it.data?.message!!)
                            }
                        }
                    }
                }
            }
        })

    }

    companion object {

        @JvmStatic
        fun newInstance() = TeacherCircularFragment()
    }

    override fun onResume() {
        super.onResume()
        setToolBarTitle("Circular Teacher")
    }
}