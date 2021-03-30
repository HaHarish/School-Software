package com.newletseduvate.ui.lesson_plan.teacher

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.newletseduvate.R
import com.newletseduvate.adapter.dynamic_recycler_adapter.CallBackModel
import com.newletseduvate.base.BaseFragment
import com.newletseduvate.databinding.AdapterTeacherLessonPlanFilterResultsBinding
import com.newletseduvate.databinding.FragmentTeacherLessonPlanHomeBinding
import com.newletseduvate.model.lesson_plan.teacher.TeacherLessonPlanFilterResultsResponse
import com.newletseduvate.utils.constants.Constants
import com.newletseduvate.utils.extensions.setUpRecyclerView
import com.newletseduvate.viewmodels.LessonPlanViewModel

class TeacherLessonPlanHomeFragment: BaseFragment(R.layout.fragment_teacher_lesson_plan_home) {

    private lateinit var binding: FragmentTeacherLessonPlanHomeBinding
    private val viewModel by lazy { getViewModel<LessonPlanViewModel>(requireActivity()) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTeacherLessonPlanHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewModel = viewModel
        viewModel.moduelId = requireArguments().getInt(Constants.MODULE_ID)

        binding.buttonTeacherLessonPlanHomeFilter.setOnClickListener {
            findNavController().navigate(R.id.nav_teacher_lesson_plan_filter)
        }

        val callbacks = ArrayList<CallBackModel<TeacherLessonPlanFilterResultsResponse.Result>>()
        callbacks.add(CallBackModel(R.id.button_teacherLessonPlanAdapter_viewMore) { model, position ->
            viewModel.chapterTitle = model.periodName
            viewModel.chapterName = model.chapterName
            viewModel.lessonClickValue = model.id!!
            findNavController().navigate(R.id.nav_teacher_lesson_plan_view_more)
        })

        binding.rvTeacherLessonPlanFilterResults.setUpRecyclerView<AdapterTeacherLessonPlanFilterResultsBinding, TeacherLessonPlanFilterResultsResponse.Result>(
            R.layout.adapter_teacher_lesson_plan_filter_results,
            callbacks,
            viewModel.teacherLessonPlanFilterResultsResponse,
            viewLifecycleOwner,
            binding.root,
            {viewModel.getTeacherLessonPlanFilterResults()},
            viewModel.isNoDataStudent,
            viewModel.totalPageStudent,
            viewModel.isPageLoadingStudent,
            viewModel.isDataLoadingStudent,
            viewModel.currentPageStudent
        ) {}
    }

    companion object {

        @JvmStatic
        fun newInstance() = TeacherLessonPlanHomeFragment()
    }

    override fun onResume() {
        super.onResume()
        setToolBarTitle(resources.getString(R.string.teacher_lesson_plan))
    }

}