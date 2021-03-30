package com.newletseduvate.ui.online_class

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.Observable
import androidx.fragment.app.FragmentManager
import com.newletseduvate.R
import com.newletseduvate.base.BaseDialogFragment
import com.newletseduvate.databinding.FragmentOnlineClassTeacherResourcesFilterBinding
import com.newletseduvate.databinding.FragmentOnlineClassTeacherViewClassFilterBinding
import com.newletseduvate.model.BottomSheetItem
import com.newletseduvate.model.online_class.*
import com.newletseduvate.ui.bottom_sheets.SingleSelectionBottomSheetFragment
import com.newletseduvate.utils.Resource
import com.newletseduvate.utils.extensions.*
import com.newletseduvate.viewmodels.OnlineClassTeacherResourcesViewModel
import com.newletseduvate.viewmodels.OnlineClassTeacherViewClassViewModel
import java.util.*
import kotlin.collections.ArrayList

class OnlineClassTeacherResourcesFilter(private val mFilter: Filter) : BaseDialogFragment(),
    SingleSelectionBottomSheetFragment.OnChooseReasonListener,
    View.OnClickListener {

    private lateinit var binding: FragmentOnlineClassTeacherResourcesFilterBinding
    private val viewModel by lazy {
        getViewModel<OnlineClassTeacherResourcesViewModel>(
            requireActivity()
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            FragmentOnlineClassTeacherResourcesFilterBinding.inflate(inflater, container, false)
        return binding.root
    }

    interface Filter {
        fun onDone(filterClicked: Boolean)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel

        initTopAppBar()
        setClickListeners()
        initTextForFilters()

        val classTypeList = ArrayList<ClassTypeModel>()
        classTypeList.add(ClassTypeModel(0, "Compulsory Class"))
        classTypeList.add(ClassTypeModel(1, "Optional Class"))
        classTypeList.add(ClassTypeModel(2, "Special Class"))
        classTypeList.add(ClassTypeModel(3, "Parent Class"))

        viewModel.classTypeList.value = Resource.success(classTypeList)

        viewModel.fromDate.addOnPropertyChangedCallback(object :
            Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {

                val calendar = Calendar.getInstance()
                calendar.timeInMillis = viewModel.fromDate.get()!!

                calendar.add(Calendar.DAY_OF_YEAR, 6)
                viewModel.toDate.set(calendar.timeInMillis)

            }
        })
    }


    override fun onClick(view: View?) {
        when (view?.id) {

            binding.etClassType.id -> {

                if (viewModel.classTypeList.value!!.data != null &&
                    viewModel.classTypeList.value!!.data!!.size > 0
                ) {
                    viewModel.classTypeList.value!!.data!!.getBottomSheetList()
                        .showSingleSelectionBottomSheetWithArrayList(
                            childFragmentManager,
                            ClassTypeBottomSheetID,
                            viewModel.classTypeId.get()
                        )
                } else binding.root.snackBar(resources.getString(R.string.please_wait))

            }

            binding.etAcademicYear.id -> {

                if (viewModel.academicYearList.value?.data != null &&
                    viewModel.academicYearList.value?.data?.size!! > 0
                ) {
                    viewModel.academicYearList.value!!.data!!.getBottomSheetList()
                        .showSingleSelectionBottomSheetWithArrayList(
                            childFragmentManager,
                            AcademicYearBottomSheetID,
                            viewModel.academicYearId.get()
                        )
                } else binding.root.snackBar(resources.getString(R.string.please_wait))

            }

            binding.etBranch.id -> {

                if (viewModel.branchList.value?.data != null &&
                    viewModel.branchList.value?.data?.size!! > 0
                ) {

                    showSingleSelectionBottomSheetWithArrayList(
                        viewModel.branchList.value!!.data!!.getBottomSheetList(),
                        childFragmentManager,
                        BranchBottomSheetID,
                        getSelectedItemFromBottomSheetList(viewModel.branchList.value!!.data!!.getBottomSheetList())
                    )
                } else binding.root.snackBar(resources.getString(R.string.please_wait))

            }

            binding.etGrade.id -> {

                if (viewModel.gradeList.value?.data != null &&
                    viewModel.gradeList.value?.data?.size!! > 0
                ) {
                    viewModel.gradeList.value!!.data!!.getBottomSheetList()
                        .showSingleSelectionBottomSheetWithArrayList(
                            childFragmentManager,
                            GradeBottomSheetID,
                            getSelectedItemFromBottomSheetList(
                                viewModel.gradeList.value!!.data!!.getBottomSheetList()
                            )
                        )
                } else binding.root.snackBar(resources.getString(R.string.please_wait))

            }

            binding.etSection.id -> {

                if (viewModel.sectionList.value?.data != null &&
                    viewModel.sectionList.value?.data!!.size > 0
                ) {
                    viewModel.sectionList.value!!.data!!.getBottomSheetList()
                        .showSingleSelectionBottomSheetWithArrayList(
                            childFragmentManager,
                            SectionBottomSheetID,
                            getSelectedItemFromBottomSheetList(
                                viewModel.sectionList.value!!.data!!.getBottomSheetList()
                            )
                        )
                } else binding.root.snackBar(resources.getString(R.string.please_wait))
            }

            binding.etSubject.id -> {

                if (viewModel.subjectList.value?.data != null &&
                    viewModel.subjectList.value?.data!!.size > 0
                ) {
                    viewModel.subjectList.value!!.data!!.getBottomSheetList()
                        .showSingleSelectionBottomSheetWithArrayList(
                            childFragmentManager,
                            SubjectBottomSheetID,
                            getSelectedItemFromBottomSheetList(
                                viewModel.subjectList.value!!.data!!.getBottomSheetList()
                            )
                        )
                } else binding.root.snackBar(resources.getString(R.string.please_wait))
            }

            binding.etCourse.id -> {

                if (viewModel.coursesList.value?.data != null &&
                    viewModel.coursesList.value?.data!!.size > 0
                ) {
                    viewModel.coursesList.value!!.data!!.getBottomSheetList()
                        .showSingleSelectionBottomSheetWithArrayList(
                            childFragmentManager,
                            CourseBottomSheetID,
                            getSelectedItemFromBottomSheetList(
                                viewModel.coursesList.value!!.data!!.getBottomSheetList()
                            )
                        )
                } else binding.root.snackBar(resources.getString(R.string.please_wait))
            }

            binding.etDateRange.id -> view.datePickerDialog(viewModel.fromDate)


            binding.btnApply.id -> {
                mFilter.onDone(true)
                dismiss()
            }

            binding.btnReset.id -> {
                clearAllFilters()
                mFilter.onDone(false)
                dismiss()
            }
        }
    }


    private fun setClickListeners() {
        binding.etClassType.setOnClickListener(this)
        binding.etAcademicYear.setOnClickListener(this)
        binding.etBranch.setOnClickListener(this)
        binding.etGrade.setOnClickListener(this)
        binding.etSection.setOnClickListener(this)
        binding.etSubject.setOnClickListener(this)
        binding.etCourse.setOnClickListener(this)
        binding.etDateRange.setOnClickListener(this)

        binding.btnReset.setOnClickListener(this)
        binding.btnApply.setOnClickListener(this)
    }


    private fun initTopAppBar() {
        binding.appBarFilter.toolbar.setNavigationOnClickListener { dismiss() }
    }

    private val BranchBottomSheetID = 0
    private val GradeBottomSheetID = 1
    private val CourseBottomSheetID = 2
    private val BatchLimitBottomSheetID = 3
    private val ClassTypeBottomSheetID = 4
    private val AcademicYearBottomSheetID = 5
    private val SectionBottomSheetID = 6
    private val SubjectBottomSheetID = 7

    override fun onChooseSingleItem(BOTTOM_SHEET_ID: Int, selectedItem: BottomSheetItem) {
        when (BOTTOM_SHEET_ID) {

            ClassTypeBottomSheetID -> {
                if (selectedItem.id != null) {
                    binding.etClassType.setText(selectedItem.name)
                    viewModel.classTypeId.set(selectedItem.id)
                }
            }

            AcademicYearBottomSheetID -> {
                if (selectedItem.id != null) {
                    binding.etAcademicYear.setText(selectedItem.name)
                    viewModel.academicYearId.set(selectedItem.id)

                    viewModel.getBranches()
                    binding.etBranch.setText("")
                }
            }


            BranchBottomSheetID -> {
                if (selectedItem.id != null) {
                    binding.etBranch.setText(selectedItem.name)

                    val tempArray = ArrayList<BranchModel>()

                    viewModel.branchList.value?.data?.forEach {
                        if (it.id == selectedItem.id) {
                            it.selected = true
                            tempArray.add(it)
                        } else {
                            it.selected = false
                            tempArray.add(it)
                        }
                    }

                    viewModel.branchList.value = Resource.success(tempArray)

                    viewModel.getGrades()
                    binding.etGrade.setText("")
                }
            }

            GradeBottomSheetID -> {
                if (selectedItem.id != null) {
                    binding.etGrade.setText(selectedItem.name)
                    val tempArray = ArrayList<GradeMappingModel>()

                    viewModel.gradeList.value?.data?.forEach {
                        if (it.gradeId == selectedItem.id) {
                            it.selected = true
                            tempArray.add(it)
                        } else {
                            it.selected = false
                            tempArray.add(it)
                        }
                    }

                    viewModel.gradeList.value = Resource.success(tempArray)

                    viewModel.getCourses()
                    viewModel.getSection()
                    binding.etCourse.setText("")
                    binding.etSection.setText("")
                }
            }

            SectionBottomSheetID -> {
                if (selectedItem.id != null) {
                    binding.etSection.setText(selectedItem.name)

                    val tempArray = ArrayList<SectionMappingModel>()

                    viewModel.sectionList.value?.data?.forEach {
                        if (it.sectionId == selectedItem.id) {
                            it.selected = true
                            tempArray.add(it)
                        } else {
                            it.selected = false
                            tempArray.add(it)
                        }
                    }

                    viewModel.sectionList.value = Resource.success(tempArray)

                    viewModel.getSubjectMapping()
                    binding.etSubject.setText("")
                }
            }


            SubjectBottomSheetID -> {
                if (selectedItem.id != null) {
                    binding.etSubject.setText(selectedItem.name)
                    val tempArray = ArrayList<SubjectModel>()

                    viewModel.subjectList.value?.data?.forEach {
                        if (it.subjectId == selectedItem.id) {
                            it.selected = true
                            tempArray.add(it)
                        } else {
                            it.selected = false
                            tempArray.add(it)
                        }
                    }

                    viewModel.subjectList.value = Resource.success(tempArray)
                }
            }

            CourseBottomSheetID -> {
                if (selectedItem.id != null) {
                    binding.etCourse.setText(selectedItem.name)
                    val tempArray = ArrayList<CourseModel>()

                    viewModel.coursesList.value?.data?.forEach {
                        if (it.id == selectedItem.id) {
                            it.selected = true
                            tempArray.add(it)
                        } else {
                            it.selected = false
                            tempArray.add(it)
                        }
                    }

                    viewModel.coursesList.value = Resource.success(tempArray)
                }
            }
        }
    }

    private fun initTextForFilters() {
        viewModel.classTypeList.value?.data?.let {
            onChooseSingleItem(
                ClassTypeBottomSheetID,
                viewModel.classTypeList.value?.data.getBottomSheetItem(viewModel.classTypeId.get())
            )
        }

        viewModel.academicYearList.value?.data?.let {
            for(model in it){
                if(model.id == viewModel.academicYearId.get()){
                    binding.etAcademicYear.setText(model.sessionYear)
                    break
                }
            }
        }

        viewModel.branchList.value?.data?.let {
            binding.etBranch.setText(
                getSelectedItemFromBottomSheetItemList(
                    viewModel.branchList.value?.data!!.getBottomSheetList()
                ).name
            )
        }

        viewModel.branchList.value?.data?.let {
            binding.etBranch.setText(
                getSelectedItemFromBottomSheetItemList(
                    viewModel.branchList.value?.data!!.getBottomSheetList()
                ).name
            )
        }

        viewModel.gradeList.value?.data?.let {
            binding.etGrade.setText(
                getSelectedItemFromBottomSheetItemList(
                    viewModel.gradeList.value?.data!!.getBottomSheetList()
                ).name
            )
        }

        viewModel.sectionList.value?.data?.let {
            binding.etSection.setText(
                getSelectedItemFromBottomSheetItemList(
                    viewModel.sectionList.value?.data!!.getBottomSheetList()
                ).name
            )
        }

        viewModel.subjectList.value?.data?.let {
            binding.etSubject.setText(
                getSelectedItemFromBottomSheetItemList(
                    viewModel.subjectList.value?.data!!.getBottomSheetList()
                ).name
            )
        }

        viewModel.coursesList.value?.data?.let {
            binding.etCourse.setText(
                getSelectedItemFromBottomSheetItemList(
                    viewModel.coursesList.value?.data!!.getBottomSheetList()
                ).name
            )
        }

        viewModel.setFilterDate()
    }

    private fun clearAllFilters() {
        viewModel.classTypeId.set(-1)
        viewModel.academicYearId.set(-1)

        binding.etBranch.setText("")
        binding.etGrade.setText("")
        binding.etSection.setText("")
        binding.etSubject.setText("")
        binding.etCourse.setText("")

        viewModel.setFilterDate()
    }

    private fun getSelectedItemFromBottomSheetList(bottomSheetList: ArrayList<BottomSheetItem>): Int {
        bottomSheetList.forEach {
            if (it.selected)
                return it.id!!
        }
        return -1
    }

    private fun getSelectedItemFromBottomSheetItemList(bottomSheetList: ArrayList<BottomSheetItem>): BottomSheetItem {
        bottomSheetList.forEach {
            if (it.selected)
                return it
        }
        return BottomSheetItem(-1, "")
    }


    fun showSingleSelectionBottomSheetWithArrayList(
        bottomSheetList: ArrayList<BottomSheetItem>,
        fragmentManager: FragmentManager,
        bottomSheetId: Int,
        selectedItemId: Int
    ) {

        for (ele in bottomSheetList) {
            if (ele.id == selectedItemId) {
                ele.selected = true
                break
            }
        }
        val fragment = SingleSelectionBottomSheetFragment(bottomSheetId, bottomSheetList)
        fragment.show(fragmentManager, fragment.tag)

    }

    override fun onStart() {
        super.onStart()

        val dialog = dialog
        if (dialog != null) {
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.MATCH_PARENT
            dialog.window?.also {
                it.setLayout(width, height)
                it.setBackgroundDrawable(ColorDrawable(Color.WHITE))
                it.setWindowAnimations(R.style.DialogAnimation)
            }
        }
    }


}