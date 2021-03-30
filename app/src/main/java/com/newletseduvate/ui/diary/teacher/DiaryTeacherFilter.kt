package com.newletseduvate.ui.diary.teacher

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.newletseduvate.R
import com.newletseduvate.base.BaseDialogFragment
import com.newletseduvate.databinding.FragmentDiaryTeacherFilterBinding
import com.newletseduvate.model.BottomSheetItem
import com.newletseduvate.model.online_class.*
import com.newletseduvate.ui.bottom_sheets.SingleSelectionBottomSheetFragment
import com.newletseduvate.utils.NetworkCheck
import com.newletseduvate.utils.Resource
import com.newletseduvate.utils.extensions.*
import com.newletseduvate.viewmodels.DiaryTeacherViewModel

class DiaryTeacherFilter : BaseDialogFragment(),
    SingleSelectionBottomSheetFragment.OnChooseReasonListener,
    View.OnClickListener {

    private lateinit var mFilter: Filter

    private lateinit var binding: FragmentDiaryTeacherFilterBinding
    private val viewModel by lazy { getViewModel<DiaryTeacherViewModel>(requireActivity()) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDiaryTeacherFilterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getInitialData()
    }

    interface Filter {
        fun onDone(filterClicked: Boolean)
    }

    fun setOnDone(filter: Filter) {
        mFilter = filter
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel

        initTopAppBar()
        setClickListeners()
        initTextForFilters()
    }

    private val BranchBottomSheetID = 0
    private val GradeBottomSheetID = 1
    private val AcademicYearBottomSheetID = 2
    private val SectionBottomSheetID = 3


    override fun onClick(view: View?) {
        when (view?.id) {

            binding.etFromDate.id -> view.datePickerDialog(viewModel.fromDate)

            binding.etToDate.id -> view.datePickerDialog(viewModel.toDate)


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


    private fun initTextForFilters() {

        viewModel.academicYearList.value?.data?.let {
            for (model in it) {
                if (model.id == viewModel.academicYearId.get()) {
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

        viewModel.setFilterDate()
    }

    private fun clearAllFilters() {
        viewModel.academicYearId.set(-1)

        binding.etAcademicYear.setText("")
        binding.etBranch.setText("")
        binding.etGrade.setText("")
        binding.etSection.setText("")

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

    private fun setClickListeners() {
        binding.etToDate.setOnClickListener(this)
        binding.etFromDate.setOnClickListener(this)

        binding.etAcademicYear.setOnClickListener(this)
        binding.etBranch.setOnClickListener(this)
        binding.etGrade.setOnClickListener(this)
        binding.etSection.setOnClickListener(this)

        binding.btnReset.setOnClickListener(this)
        binding.btnApply.setOnClickListener(this)
    }


    private fun getInitialData() {
        try {
            if (NetworkCheck.isInternetAvailable(requireContext())) {
                viewModel.getAcademicYear()
            } else {
                if (::binding.isInitialized) {
                    binding.root.snackBar(
                        getString(R.string.check_internet),
                        getString(R.string.retry),
                        true
                    ) {
                        getInitialData()
                    }
                }
            }
        } catch (ex: Exception) {
        }
    }

    private fun initTopAppBar() {
        binding.appBarFilter.toolbar.setNavigationOnClickListener { dismiss() }
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

    override fun onChooseSingleItem(BOTTOM_SHEET_ID: Int, selectedItem: BottomSheetItem) {
        when (BOTTOM_SHEET_ID) {

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

                    viewModel.getSection()
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
                }
            }
        }
    }

}