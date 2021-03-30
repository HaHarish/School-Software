package com.newletseduvate.ui.homework

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.newletseduvate.R
import com.newletseduvate.adapter.dynamic_recycler_adapter.CallBackModel
import com.newletseduvate.adapter.dynamic_recycler_adapter.RecyclerDynamicAdapter
import com.newletseduvate.base.BaseFragment
import com.newletseduvate.databinding.*
import com.newletseduvate.model.homeWork.HomeWorkStudentModel
import com.newletseduvate.model.homeWork.HomeworkTeacherDetailsEvaluatedResponse
import com.newletseduvate.model.homeWork.HomeworkTeacherModel
import com.newletseduvate.ui.homework.HomeworkTeacherFragment.Companion.HomeworkTeacherModelConstant
import com.newletseduvate.utils.NetworkCheck
import com.newletseduvate.utils.extensions.snackBar
import com.newletseduvate.viewmodels.HomeworkTeacherDetailsHWEvaluatedViewModel


class HomeworkTeacherDetailsHWSubmittedFragment : BaseFragment(R.layout.fragment_homework_teacher_details_h_w_submitted) {

    lateinit var binding: FragmentHomeworkTeacherDetailsHWSubmittedBinding
    private val viewModel by lazy { getViewModel<HomeworkTeacherDetailsHWEvaluatedViewModel>(this) }
    private lateinit var adapterEvaluated: RecyclerDynamicAdapter<AdapterHomeworkTeacherDetailsHwSubmittedBinding, HomeworkTeacherDetailsEvaluatedResponse.Submitted>
    private lateinit var adapterUnEvaluated: RecyclerDynamicAdapter<AdapterHomeworkTeacherDetailsHwUnsubmittedBinding, HomeworkTeacherDetailsEvaluatedResponse.UnSubmitted>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeworkTeacherDetailsHWSubmittedBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel

        binding.tvHeading.text = requireArguments().getString("heading", "")
        viewModel.subjectID.set(requireArguments().getString("subject_id",""))

        viewModel.homeworkTeacherModel.value =
            requireArguments().getSerializable(HomeworkTeacherModelConstant) as HomeworkTeacherModel

        initEvaluatedRecyclerView()
        initUnEvaluatedRecyclerView()
        initDataObserver()

        getHomeworkTeacherDetailsEvaluated()
    }

    private fun initDataObserver() {
        viewModel.teacherHomeWorkEvaluatedResponse.observe(viewLifecycleOwner, {

            it.data?.submittedList?.let { evaluatedList ->
                adapterEvaluated.replaceList(evaluatedList)

                if(evaluatedList.isEmpty()){
                    binding.noStudentsEvaluated.visibility = View.VISIBLE
                }
            }

            it.data?.unSubmittedList?.let { unEvaluatedList ->
                adapterUnEvaluated.replaceList(unEvaluatedList)

                if(unEvaluatedList.isEmpty()){
                    binding.noStudentsUnevaluated.visibility = View.VISIBLE
                }
            }
        })

    }

    private fun initEvaluatedRecyclerView() {

        val callbacks = ArrayList<CallBackModel<HomeworkTeacherDetailsEvaluatedResponse.Submitted>>()

        callbacks.add(CallBackModel(R.id.root) { model, _ ->
            val bundle = Bundle()

//            val b = HomeworkTeacherDetailsEvaluatedResponse.Evaluated(model.studentHomeworkId, model.submittedBy, model.hwStatus, model.firstName, model.lastName)
            bundle.putSerializable("teacherEvaluatedModel", model)
            bundle.putString("heading", requireArguments().getString("heading",""))
            findNavController().navigate(R.id.nav_teacher_homework_submit, bundle)
        })

        val linearLayoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        binding.rvEvaluatedStudents.layoutManager = linearLayoutManager

        adapterEvaluated = RecyclerDynamicAdapter(R.layout.adapter_homework_teacher_details_hw_submitted, callbacks)
        binding.rvEvaluatedStudents.adapter = adapterEvaluated
        binding.rvEvaluatedStudents.setHasFixedSize(true)
    }
    private fun initUnEvaluatedRecyclerView(){
        val callbacks = ArrayList<CallBackModel<HomeworkTeacherDetailsEvaluatedResponse.UnSubmitted>>()

        val linearLayoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        binding.rvUnEvaluatedStudents.layoutManager = linearLayoutManager

        adapterUnEvaluated = RecyclerDynamicAdapter(R.layout.adapter_homework_teacher_details_hw_unsubmitted, callbacks)
        binding.rvUnEvaluatedStudents.adapter = adapterUnEvaluated
        binding.rvUnEvaluatedStudents.setHasFixedSize(true)
    }


    private fun getHomeworkTeacherDetailsEvaluated() {
        if (NetworkCheck.isInternetAvailable(requireContext())) {
            viewModel.getHomeworkTeacherDetailsEvaluated()
        } else {
            if(::binding.isInitialized){
                binding.root.snackBar(getString(R.string.check_internet), getString(R.string.retry), true) {
                    getHomeworkTeacherDetailsEvaluated()
                }
            }
        }
    }

    companion object {

        @JvmStatic
        fun newInstance() =
            HomeworkTeacherDetailsHWSubmittedFragment()
    }
}