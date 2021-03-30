package com.newletseduvate.ui.lesson_plan.teacher

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.newletseduvate.R
import com.newletseduvate.base.BaseFragment
import com.newletseduvate.databinding.FragmentTeacherCircularFilterBinding
import com.newletseduvate.databinding.FragmentTeacherLessonPlanFilterBinding
import com.newletseduvate.model.BottomSheetItem
import com.newletseduvate.ui.bottom_sheets.SingleSelectionBottomSheetFragment
import com.newletseduvate.utils.extensions.showSingleSelectionBottomSheetWithArrayList
import com.newletseduvate.utils.extensions.snackBar
import com.newletseduvate.viewmodels.LessonPlanViewModel
import kotlinx.android.synthetic.main.fragment_lesson_plan.*
import kotlinx.android.synthetic.main.fragment_teacher_circular_filter.*
import kotlinx.android.synthetic.main.fragment_teacher_lesson_plan_filter.*

class TeacherLessonPlanFilterFragment: BaseFragment(R.layout.fragment_teacher_lesson_plan_filter),
    SingleSelectionBottomSheetFragment.OnChooseReasonListener {



    private lateinit var binding: FragmentTeacherLessonPlanFilterBinding

    private val viewModel by lazy { getViewModel<LessonPlanViewModel>(requireActivity()) }

    private val academicYearBottomSheetID = 0
    private val volumeBottomSheetID = 1
    private val branchBottomSheetID = 2
    private val gradeBottomSheetID = 3
    private val subjectBottomSheetID = 4
    private val chapterBottomSheetID = 5

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTeacherLessonPlanFilterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel
        initTextForFilters()

        viewModel.getLessonPlanAcademicYear()

        /* Academic Year */
        binding.editTextTeacherLessonPlanAcademicYear.setOnClickListener {
            val arrayList = ArrayList<BottomSheetItem>()
            viewModel.academicYearList.value?.forEach {
                arrayList.add(it)
            }

            arrayList.showSingleSelectionBottomSheetWithArrayList(
                childFragmentManager,
                academicYearBottomSheetID,
                viewModel.academicYearListId.get()
            )
        }

        /* Volume */
        binding.editTextTeacherLessonPlanVolume.setOnClickListener {
            val arrayList = ArrayList<BottomSheetItem>()
            viewModel.volumeList.value?.forEach {
                arrayList.add(it)
            }

            arrayList.showSingleSelectionBottomSheetWithArrayList(
                childFragmentManager,
                volumeBottomSheetID,
                viewModel.volumeListId.get()
            )
        }

        /* Branch */
        binding.editTextTeacherLessonPlanBranch.setOnClickListener {
            val arrayList = ArrayList<BottomSheetItem>()
            viewModel.branchList.value?.forEach {
                arrayList.add(it)
            }

            arrayList.showSingleSelectionBottomSheetWithArrayList(
                childFragmentManager,
                branchBottomSheetID,
                viewModel.branchListId.get()
            )
        }

        /* Grade */
        binding.editTextTeacherLessonPlanGrade.setOnClickListener {
            val arrayList = ArrayList<BottomSheetItem>()
            viewModel.gradeList.value?.forEach {
                arrayList.add(it)
            }

            arrayList.showSingleSelectionBottomSheetWithArrayList(
                childFragmentManager,
                gradeBottomSheetID,
                viewModel.gradeListId.get()
            )
        }

        /* Subject */
        binding.editTextTeacherLessonPlanSubject.setOnClickListener {
            val arrayList = ArrayList<BottomSheetItem>()
            viewModel.subjectList.value?.forEach {
                arrayList.add(it)
            }

            arrayList.showSingleSelectionBottomSheetWithArrayList(
                childFragmentManager,
                subjectBottomSheetID,
                viewModel.subjectListId.get()
            )
        }

        /* Chapter */
        binding.editTextTeacherLessonPlanChapter.setOnClickListener {
            val arrayList = ArrayList<BottomSheetItem>()
            viewModel.chapterList.value?.forEach {
                arrayList.add(it)
            }

            arrayList.showSingleSelectionBottomSheetWithArrayList(
                childFragmentManager,
                chapterBottomSheetID,
                viewModel.chapterListId.get()
            )
        }

        binding.buttonTeacherLessonPlanFilterFilter.setOnClickListener {

            if(binding.editTextTeacherLessonPlanAcademicYear.text?.trim().toString().isEmpty()){
                binding.root.snackBar("Please select Academic Year")
            }else if(binding.editTextTeacherLessonPlanVolume.text?.trim().toString().isEmpty()){
                binding.root.snackBar("Please select Volume")
            }else if(binding.editTextTeacherLessonPlanBranch.text?.trim().toString().isEmpty()){
                binding.root.snackBar("Please select Branch")
            }else if(binding.editTextTeacherLessonPlanGrade.text?.trim().toString().isEmpty()){
                binding.root.snackBar("Please select Grade")
            }else if(binding.editTextTeacherLessonPlanSubject.text?.trim().toString().isEmpty()){
                binding.root.snackBar("Please select Subject")
            }else if(binding.editTextTeacherLessonPlanChapter.text?.trim().toString().isEmpty()){
                binding.root.snackBar("Please select Chapter")
            }else{
                viewModel.getTeacherLessonPlanFilterResults()
                findNavController().popBackStack()
            }
        }
    }

    override fun onChooseSingleItem(BOTTOM_SHEET_ID: Int, selectedItem: BottomSheetItem) {
        when(BOTTOM_SHEET_ID){
            academicYearBottomSheetID -> {
                if (selectedItem.id != null) {

                    editText_teacherLessonPlan_academicYear.setText(selectedItem.name)
                    viewModel.academicName = selectedItem.name
                    viewModel.academicYearId = selectedItem.id
                    viewModel.getLessonPlanVolume()
                }
            }

            volumeBottomSheetID -> {
                if(selectedItem.id != null){
                    editText_teacherLessonPlan_volume.setText(selectedItem.name)
                    viewModel.volumeName = selectedItem.name
                    viewModel.volumeId = selectedItem.id
                    viewModel.getLessonPlanBranch()
                }
            }

            branchBottomSheetID -> {
                if(selectedItem.id != null){
                    editText_teacherLessonPlan_branch.setText(selectedItem.name)
                    viewModel.branchName = selectedItem.name
                    viewModel.branchId = selectedItem.id
                    viewModel.getLessonPlanGrade()
                }
            }

            gradeBottomSheetID -> {
                if(selectedItem.id != null){
                    editText_teacherLessonPlan_grade.setText(selectedItem.name)
                    viewModel.gradeName = selectedItem.name
                    viewModel.gradeId = selectedItem.id
                    viewModel.getLessonPlanSubject()
                }
            }

            subjectBottomSheetID -> {
                if(selectedItem.id != null){
                    editText_teacherLessonPlan_subject.setText(selectedItem.name)
                    viewModel.subjectName = selectedItem.name
                    viewModel.subjectId = selectedItem.id
                    viewModel.getLessonPlanChapter()

                }
            }

            chapterBottomSheetID -> {
                if(selectedItem.id != null){
                    editText_teacherLessonPlan_chapter.setText(selectedItem.name)
                    viewModel.chapterId = selectedItem.id
                    viewModel.chapterName = selectedItem.name!!

                }
            }
        }
    }

    private fun initTextForFilters() {
        /* Academic Year */
        viewModel.academicYearList.value?.let {
            onChooseSingleItem(
                academicYearBottomSheetID,
                getBottomSheetItem(viewModel.academicYearList.value, viewModel.academicYearListId.get())
            )
        }

        /* Volume */
        viewModel.volumeList.value?.let {
            onChooseSingleItem(
                volumeBottomSheetID,
                getBottomSheetItem(viewModel.volumeList.value, viewModel.volumeListId.get())
            )
        }

        /* Branch */
        viewModel.branchList.value?.let {
            onChooseSingleItem(
                branchBottomSheetID,
                getBottomSheetItem(viewModel.branchList.value, viewModel.branchListId.get())
            )
        }

        /* Grade */
        viewModel.gradeList.value?.let {
            onChooseSingleItem(
                gradeBottomSheetID,
                getBottomSheetItem(viewModel.gradeList.value, viewModel.gradeListId.get())
            )
        }

        /* Subject */
        viewModel.subjectList.value?.let {
            onChooseSingleItem(
                subjectBottomSheetID,
                getBottomSheetItem(viewModel.subjectList.value, viewModel.subjectListId.get())
            )
        }

        /* Chapter */
        viewModel.chapterList.value?.let {
            onChooseSingleItem(
                chapterBottomSheetID,
                getBottomSheetItem(viewModel.chapterList.value, viewModel.chapterListId.get())
            )
        }
    }

    private fun getBottomSheetItem(subjectList: ArrayList<BottomSheetItem>?, id: Int): BottomSheetItem {
        subjectList?.forEach {
            if (it.id == id)
                return it
        }
        return BottomSheetItem(-1,"", false)
    }

    override fun onResume() {
        super.onResume()
        setToolBarTitle(getString(R.string.teacher_lesson_plan_filter))
    }

}