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
import com.newletseduvate.databinding.AdapterHomeworkTeacherDetailsEvaluatedBinding
import com.newletseduvate.databinding.AdapterHomeworkTeacherDetailsUnevaluatedBinding
import com.newletseduvate.databinding.FragmentHomeworkTeacherDetailsHWEvaluatedBinding
import com.newletseduvate.model.homeWork.HomeworkTeacherDetailsEvaluatedResponse
import com.newletseduvate.model.homeWork.HomeworkTeacherModel
import com.newletseduvate.utils.NetworkCheck
import com.newletseduvate.utils.extensions.snackBar
import com.newletseduvate.viewmodels.HomeworkTeacherDetailsHWEvaluatedViewModel


class HomeworkTeacherDetailsHWEvaluatedFragment : BaseFragment(R.layout.fragment_homework_teacher_details_h_w_evaluated) {

    lateinit var binding: FragmentHomeworkTeacherDetailsHWEvaluatedBinding
    private val viewModel by lazy { getViewModel<HomeworkTeacherDetailsHWEvaluatedViewModel>(this) }
    private lateinit var adapterEvaluated: RecyclerDynamicAdapter<AdapterHomeworkTeacherDetailsEvaluatedBinding, HomeworkTeacherDetailsEvaluatedResponse.Evaluated>
    private lateinit var adapterUnEvaluated: RecyclerDynamicAdapter<AdapterHomeworkTeacherDetailsUnevaluatedBinding, HomeworkTeacherDetailsEvaluatedResponse.Unevaluated>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeworkTeacherDetailsHWEvaluatedBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel

//        viewModel.hwDetailsID.set(requireArguments().getString("hw_details_id",""))
        viewModel.subjectID.set(requireArguments().getString("subject_id",""))
        binding.tvHeading.text = requireArguments().getString("heading", "")
        viewModel.homeworkTeacherModel.value =
            requireArguments().getSerializable(HomeworkTeacherFragment.HomeworkTeacherModelConstant) as HomeworkTeacherModel

        initEvaluatedRecyclerView()
        initUnEvaluatedRecyclerView()
        initDataObserver()

        getHomeworkTeacherDetailsEvaluated()
    }

    private fun initDataObserver() {
        viewModel.teacherHomeWorkEvaluatedResponse.observe(viewLifecycleOwner, {

            it.data?.evaluatedList?.let { evaluatedList ->
                adapterEvaluated.replaceList(evaluatedList)

                if(evaluatedList.isEmpty()){
                    binding.noStudentsEvaluated.visibility = View.VISIBLE
                }
            }

            it.data?.unevaluatedList?.let { unEvaluatedList ->
                adapterUnEvaluated.replaceList(unEvaluatedList)

                if(unEvaluatedList.isEmpty()){
                    binding.noStudentsUnevaluated.visibility = View.VISIBLE
                }
            }
        })

    }

    private fun initEvaluatedRecyclerView() {

        val callbacks = ArrayList<CallBackModel<HomeworkTeacherDetailsEvaluatedResponse.Evaluated>>()

        callbacks.add(CallBackModel(R.id.root) { model, _ ->

            val submitModel = HomeworkTeacherDetailsEvaluatedResponse.Submitted(model.studentHomeworkId, model.submittedBy, model.hwStatus, model.firstName, model.lastName)

            // ToDO
            val bundle = Bundle()
//            bundle.putSerializable("teacherEvaluatedModel", model)
            bundle.putSerializable("teacherEvaluatedModel", submitModel)
            bundle.putString("heading", requireArguments().getString("heading",""))

//            findNavController().navigate(R.id.nav_teacher_homework_evaluate, bundle)
            findNavController().navigate(R.id.nav_teacher_homework_submit, bundle)
        })

        val linearLayoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        binding.rvEvaluatedStudents.layoutManager = linearLayoutManager

        adapterEvaluated = RecyclerDynamicAdapter(R.layout.adapter_homework_teacher_details_evaluated, callbacks)
        binding.rvEvaluatedStudents.adapter = adapterEvaluated
        binding.rvEvaluatedStudents.setHasFixedSize(true)
    }
    private fun initUnEvaluatedRecyclerView(){
        val callbacks = ArrayList<CallBackModel<HomeworkTeacherDetailsEvaluatedResponse.Unevaluated>>()

        val linearLayoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        binding.rvUnEvaluatedStudents.layoutManager = linearLayoutManager

        adapterUnEvaluated = RecyclerDynamicAdapter(R.layout.adapter_homework_teacher_details_unevaluated, callbacks)
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
            HomeworkTeacherDetailsHWEvaluatedFragment()
    }
}