package com.newletseduvate.ui.diary.teacher

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.newletseduvate.R
import com.newletseduvate.adapter.CircularUploadFileAdapter
import com.newletseduvate.base.BaseFragment
import com.newletseduvate.databinding.FragmentTeacherCircularFilterBinding
import com.newletseduvate.databinding.FragmentTeacherDiaryCreateFilterBinding
import com.newletseduvate.model.BottomSheetItem
import com.newletseduvate.ui.bottom_sheets.SingleSelectionBottomSheetFragment
import com.newletseduvate.utils.extensions.datePickerDialog
import com.newletseduvate.utils.extensions.showSingleSelectionBottomSheetWithArrayList
import com.newletseduvate.utils.extensions.snackBar
import com.newletseduvate.viewmodels.CircularTeacherViewModel
import com.newletseduvate.viewmodels.TeacherDiaryCreateFilterViewModel
import kotlinx.android.synthetic.main.fragment_teacher_circular_filter.*
import kotlinx.android.synthetic.main.fragment_teacher_diary_create_filter.*

class TeacherDiaryCreateDiaryAndFilterFragment: BaseFragment(R.layout.fragment_teacher_diary_create_filter),
    SingleSelectionBottomSheetFragment.OnChooseReasonListener {

    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var adapter: CircularUploadFileAdapter

    private lateinit var binding: FragmentTeacherDiaryCreateFilterBinding

    private val viewModel by lazy { getViewModel<TeacherDiaryCreateFilterViewModel>(requireActivity()) }

    private val academicYearBottomSheetID = 0
    private val branchBottomSheetID = 1
    private val gradeBottomSheetID = 2
    private val sectionBottomSheetID = 3

    private val subjectBottomSheetID = 4
    private val chapterBottomSheetID = 5

    private var academicYearId = 0

    private var imageStr: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTeacherDiaryCreateFilterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel

        initTextForFilters()
        setUpViews()
        viewModel.circularAcademicYear()

        /* Academic Year */
        binding.editTextTeacherDiaryCreateFilterAcademicYear.setOnClickListener {
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

        binding.editTextTeacherDiaryCreateFilterFromDate.setOnClickListener {
            view.datePickerDialog(viewModel.fromDate)
        }
        binding.editTextTeacherDiaryCreateFilterToDate.setOnClickListener {
            view.datePickerDialog(viewModel.toDate)
        }

        /* Branch */
        binding.editTextTeacherDiaryCreateFilterBranch.setOnClickListener {
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
        binding.editTextTeacherDiaryCreateFilterGrade.setOnClickListener {
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

        /* Section */
        binding.editTextTeacherDiaryCreateFilterSection.setOnClickListener {
            val arrayList = ArrayList<BottomSheetItem>()
            viewModel.sectionList.value?.forEach {
                arrayList.add(it)
            }

            arrayList.showSingleSelectionBottomSheetWithArrayList(
                childFragmentManager,
                sectionBottomSheetID,
                viewModel.sectionListId.get()
            )
        }

        /* Subject */
        binding.editTextTeacherDiaryCreateFilterSubject.setOnClickListener {
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
        binding.editTextTeacherDiaryCreateFilterChapter.setOnClickListener {
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

        /* Next Button */
        binding.buttonTeacherDiaryCreateFilterSubmit.setOnClickListener {
            if(viewModel.isGeneralClicked.get()){

                if(binding.editTextTeacherDiaryCreateFilterAcademicYear.text?.trim().toString().isEmpty()){
                    binding.root.snackBar("Select Academic Year")
                }else if(binding.editTextTeacherDiaryCreateFilterBranch.text?.trim().toString().isEmpty()){
                    binding.root.snackBar("Select Branch")
                }else if(binding.editTextTeacherDiaryCreateFilterGrade.text?.trim().toString().isEmpty()){
                    binding.root.snackBar("Select Grade")
                }else if(binding.editTextTeacherDiaryCreateFilterSection.text?.trim().toString().isEmpty()){
                    binding.root.snackBar("Select Section")
                }else{
                    findNavController().navigate(R.id.nav_teacher_diary_active_students)
                }
            }else if(viewModel.isDailyClicked.get()){
                if(binding.editTextTeacherDiaryCreateFilterAcademicYear.text?.trim().toString().isEmpty()){
                    binding.root.snackBar("Select Academic Year")
                }else if(binding.editTextTeacherDiaryCreateFilterBranch.text?.trim().toString().isEmpty()){
                    binding.root.snackBar("Select Branch")
                }else if(binding.editTextTeacherDiaryCreateFilterGrade.text?.trim().toString().isEmpty()){
                    binding.root.snackBar("Select Grade")
                }else if(binding.editTextTeacherDiaryCreateFilterSection.text?.trim().toString().isEmpty()){
                    binding.root.snackBar("Select Section")
                }else if(binding.editTextTeacherDiaryCreateFilterSubject.text?.trim().toString().isEmpty()){
                    binding.root.snackBar("Select Subject")
                }else if(binding.editTextTeacherDiaryCreateFilterChapter.text?.trim().toString().isEmpty()){
                    binding.root.snackBar("Select Chapter")
                }else{
                    findNavController().navigate(R.id.nav_teacher_diary_create_general)
                }
            }
        }
    }

    /* Main Code Starts Here */
    private fun setUpViews() {
        if(viewModel.isGeneralClicked.get()){
            binding.layoutTeacherDiaryCreateFilterFromToDate.visibility = View.GONE
            binding.layoutTeacherDiaryCreateFilterClearFilter.visibility = View.GONE
        }else if (viewModel.isDailyClicked.get()){
            binding.layoutTeacherDiaryCreateFilterFromToDate.visibility = View.GONE
            binding.layoutTeacherDiaryCreateFilterClearFilter.visibility = View.VISIBLE
        }
    }

    override fun onChooseSingleItem(BOTTOM_SHEET_ID: Int, selectedItem: BottomSheetItem) {
        when(BOTTOM_SHEET_ID){
            academicYearBottomSheetID -> {
                if (selectedItem.id != null) {

                    editText_teacherDiaryCreateFilter_academicYear.setText(selectedItem.name)
                    viewModel.academicYearId = selectedItem.id
                    viewModel.circularBranch()
                }
            }

            branchBottomSheetID -> {
                if(selectedItem.id != null){
                    editText_teacherDiaryCreateFilter_branch.setText(selectedItem.name)
                    viewModel.branchId = selectedItem.id
                    viewModel.circularGrade()

                }
            }

            gradeBottomSheetID -> {
                if(selectedItem.id != null){
                    editText_teacherDiaryCreateFilter_grade.setText(selectedItem.name)
                    viewModel.gradeId = selectedItem.id
                    viewModel.circularSection()
                }
            }

            sectionBottomSheetID -> {
                if(selectedItem.id != null){
                    editText_teacherDiaryCreateFilter_section.setText(selectedItem.name)
                    viewModel.sectionId = selectedItem.id
                    viewModel.diarySubjects()
                }
            }

            subjectBottomSheetID -> {
                if(selectedItem.id != null){
                    editText_teacherDiaryCreateFilter_subject.setText(selectedItem.name)
                    viewModel.subjectId = selectedItem.id
                    viewModel.diaryChapters()
                }
            }

            chapterBottomSheetID -> {
                if(selectedItem.id != null){
                    editText_teacherDiaryCreateFilter_chapter.setText(selectedItem.name)
                    viewModel.chapterId = selectedItem.id
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

        /* Section */
        viewModel.sectionList.value?.let {
            onChooseSingleItem(
                sectionBottomSheetID,
                getBottomSheetItem(viewModel.sectionList.value, viewModel.sectionListId.get())
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

}