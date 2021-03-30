package com.newletseduvate.ui.lesson_plan.teacher

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.newletseduvate.R
import com.newletseduvate.base.BaseFragment
import com.newletseduvate.databinding.FragmentTeacherCircularViewmoreBinding
import com.newletseduvate.databinding.FragmentTeacherLessonPlanViewMoreBinding
import com.newletseduvate.model.circular.teacher.CircularTeacherFilterResponse
import com.newletseduvate.model.lesson_plan.teacher.TeacherLessonPlanViewMoreResponse
import com.newletseduvate.utils.Status
import com.newletseduvate.utils.extensions.getBranchName
import com.newletseduvate.utils.extensions.snackBar
import com.newletseduvate.viewmodels.CircularTeacherViewModel
import com.newletseduvate.viewmodels.LessonPlanViewModel
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TeacherLessonPlanViewMoreFragment: BaseFragment(R.layout.fragment_teacher_lesson_plan_view_more) {

    private val viewModel by lazy { getViewModel<LessonPlanViewModel>(requireActivity()) }

    lateinit var binding: FragmentTeacherLessonPlanViewMoreBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTeacherLessonPlanViewMoreBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val newStr = "${viewModel.academicName}" + "/" +
                "${viewModel.volumeName}" + "/" +
                "${viewModel.gradeName}" + "/" +
                "${viewModel.subjectName}" + "/" +
                "${viewModel.chapterName}" + "/" +
                "${viewModel.chapterTitle}"

        binding.branchName = newStr

        binding.chapterTitle = viewModel.chapterTitle
        binding.chapterName = viewModel.chapterName

        viewModel.getTeacherLessonPlanViewMoreResults()

        viewModel.teacherLessonPlanViewMoreResponse.observe(viewLifecycleOwner, {
            it?.let {
                GlobalScope.launch {
                    withContext(Dispatchers.Main) {
                        when (it.status) {
                            Status.Success -> {
                                binding.model = it?.data
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

        binding.buttonTeacherLessonPlanCompleted.setOnClickListener {
            viewModel.getTeacherLessonPlanCompletedResult()
        }

        viewModel.teacherLessonPlanCompletedResponse.observe(viewLifecycleOwner, {
            it?.let {
                GlobalScope.launch {
                    withContext(Dispatchers.Main) {
                        when (it.status) {
                            Status.Success -> {
                                binding.root.snackBar("Lesson Completed Successfully")
                                findNavController().popBackStack()
                            }
                            Status.Error -> {
                                binding.root.snackBar(it.message!!)
                            }
                            else -> {
                                binding.root.snackBar(it.message!!)
                            }
                        }
                    }
                }
            }

        })
    }
}