package com.newletseduvate.ui.homework

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.newletseduvate.R
import com.newletseduvate.adapter.dynamic_recycler_adapter.CallBackModel
import com.newletseduvate.adapter.dynamic_recycler_adapter.RecyclerDynamicAdapter
import com.newletseduvate.base.BaseFragment
import com.newletseduvate.databinding.AdapterHomeworkTeacherDetailsAssignedBinding
import com.newletseduvate.databinding.FragmentHomeworkTeacherDetailsAssignedBinding
import com.newletseduvate.model.homeWork.HomeworkTeacherDetailsSubmittedModel
import com.newletseduvate.utils.NetworkCheck
import com.newletseduvate.utils.extensions.snackBar
import com.newletseduvate.viewmodels.HomeworkTeacherSubmittedViewModel

class HomeworkTeacherDetailsAssignedFragment : BaseFragment(R.layout.fragment_homework_teacher_details_assigned) {

    lateinit var binding: FragmentHomeworkTeacherDetailsAssignedBinding
    private val viewModel by lazy { getViewModel<HomeworkTeacherSubmittedViewModel>(this) }
    lateinit var adapter: RecyclerDynamicAdapter<AdapterHomeworkTeacherDetailsAssignedBinding, HomeworkTeacherDetailsSubmittedModel>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeworkTeacherDetailsAssignedBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel

        viewModel.hwDetailsID.set(requireArguments().getString("hw_details_id",""))
        binding.tvHeading.text = requireArguments().getString("heading", "")
        initRecyclerView()
        initDataObserver()

        getTeacherDetailsSubmitted()
    }

    private fun initDataObserver() {
        viewModel.teacherHomeWorkList.observe(viewLifecycleOwner, {
            adapter.replaceList(it.data!!)
        })

    }

    private fun initRecyclerView() {

        val callbacks = ArrayList<CallBackModel<HomeworkTeacherDetailsSubmittedModel>>()

//        callbacks.add(CallBackModel(R.id.ll_homework_add_homework) { model, _ ->
//        })

        val linearLayoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        binding.rvList.layoutManager = linearLayoutManager

        adapter = RecyclerDynamicAdapter(R.layout.adapter_homework_teacher_details_assigned, callbacks)
        binding.rvList.adapter = adapter
        binding.rvList.setHasFixedSize(true)
    }

    private fun getTeacherDetailsSubmitted() {
        if (NetworkCheck.isInternetAvailable(requireContext())) {
            viewModel.getTeacherHomeWork()
        } else {
            if(::binding.isInitialized){
                binding.root.snackBar(getString(R.string.check_internet), getString(R.string.retry), true) {
                    getTeacherDetailsSubmitted()
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        setToolBarTitle(getString(R.string.menu_teacher_homework))
    }

    companion object {

        const val HomeworkTeacherModelConstant = "HomeworkTeacherModelConstant"

        @JvmStatic
        fun newInstance() = HomeworkTeacherDetailsAssignedFragment()
    }
}