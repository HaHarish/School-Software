package com.newletseduvate.ui.lesson_plan.student

import android.os.Bundle
import android.view.View
import com.newletseduvate.R
import com.newletseduvate.base.BaseFragment
import com.newletseduvate.model.BottomSheetItem
import com.newletseduvate.ui.bottom_sheets.SingleSelectionBottomSheetFragment
import com.newletseduvate.utils.constants.Constants.MODULE_ID
import com.newletseduvate.utils.extensions.showSingleSelectionBottomSheetWithArrayList
import com.newletseduvate.viewmodels.LessonPlanViewModel
import kotlinx.android.synthetic.main.fragment_lesson_plan.*

class LessonPlanFragment : BaseFragment(R.layout.fragment_lesson_plan),
    SingleSelectionBottomSheetFragment.OnChooseReasonListener{

    private val viewModel by lazy { getViewModel<LessonPlanViewModel>() }

    private val academicYearBottomSheetID = 0
    private val volumeBottomSheetID = 1
    private val branchBottomSheetID = 2
    private val gradeBottomSheetID = 3
    private val subjectBottomSheetID = 4
    private val chapterBottomSheetID = 5

    private var moduelID: Int = 0

    private var academicYearId = 0
    private var volumeId = 0
    private var branchId = 0
    private var gradeId = 0
    private var subjectId = 0
    private var chapterId= 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        moduelID = requireArguments().getInt(MODULE_ID)

        initTextForFilters()

        viewModel.getLessonPlanAcademicYear()

        /* Academic Year */
        editText_lessonPlan_academicYear.setOnClickListener {
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
        editText_lessonPlan_volume.setOnClickListener {
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
        editText_lessonPlan_branch.setOnClickListener {
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
        editText_lessonPlan_grade.setOnClickListener {
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
        editText_lessonPlan_subject.setOnClickListener {
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
        editText_lessonPlan_chapter.setOnClickListener {
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

    }

    override fun onChooseSingleItem(BOTTOM_SHEET_ID: Int, selectedItem: BottomSheetItem) {
        when(BOTTOM_SHEET_ID){
            academicYearBottomSheetID -> {
                if (selectedItem.id != null) {
                    /*binding.etSubject.setText(selectedItem.name)
                    viewModel.subjectId.set(selectedItem.id)*/

                    editText_lessonPlan_academicYear.setText(selectedItem.name)
                    academicYearId = selectedItem.id
                    viewModel.getLessonPlanVolume()
                }
            }

            volumeBottomSheetID -> {
                if(selectedItem.id != null){
                    editText_lessonPlan_volume.setText(selectedItem.name)
                    volumeId = selectedItem.id
                    viewModel.getLessonPlanBranch()
                }
            }

            branchBottomSheetID -> {
                if(selectedItem.id != null){
                    editText_lessonPlan_branch.setText(selectedItem.name)
                    branchId = selectedItem.id
                    viewModel.getLessonPlanGrade()
                }
            }

            gradeBottomSheetID -> {
                if(selectedItem.id != null){
                    editText_lessonPlan_grade.setText(selectedItem.name)
                    gradeId = selectedItem.id
                    viewModel.getLessonPlanSubject()
                }
            }

            subjectBottomSheetID -> {
                if(selectedItem.id != null){
                    editText_lessonPlan_subject.setText(selectedItem.name)
                    subjectId = selectedItem.id
                    viewModel.getLessonPlanChapter()

                }
            }

            chapterBottomSheetID -> {
                if(selectedItem.id != null){
                    editText_lessonPlan_chapter.setText(selectedItem.name)
                    chapterId = selectedItem.id

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
        setToolBarTitle(getString(R.string.lesson_plan))
    }
}