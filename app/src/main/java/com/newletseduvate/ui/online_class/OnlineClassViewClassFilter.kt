package com.newletseduvate.ui.online_class

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.newletseduvate.R
import com.newletseduvate.base.BaseDialogFragment
import com.newletseduvate.databinding.FragmentOnlineClassViewClassFilterBinding
import com.newletseduvate.model.BottomSheetItem
import com.newletseduvate.model.online_class.GradeModel
import com.newletseduvate.ui.bottom_sheets.SingleSelectionBottomSheetFragment
import com.newletseduvate.utils.Resource
import com.newletseduvate.utils.extensions.*
import com.newletseduvate.viewmodels.OnlineClassViewClassViewModel
import kotlin.collections.ArrayList

class OnlineClassViewClassFilter(private val mFilter: Filter) : BaseDialogFragment(),
    SingleSelectionBottomSheetFragment.OnChooseReasonListener,
    View.OnClickListener {

    private lateinit var binding: FragmentOnlineClassViewClassFilterBinding
    private val viewModel by lazy { getViewModel<OnlineClassViewClassViewModel>(requireActivity()) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentOnlineClassViewClassFilterBinding.inflate(inflater, container, false)
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

        val l = ArrayList<GradeModel>()
        l.add(GradeModel(1, "AOL"))
        l.add(GradeModel(2, "ABC"))
        l.add(GradeModel(3, "EFG"))
        l.add(GradeModel(4, "ZXS"))
        viewModel.branchList.value = Resource.success(l)
        viewModel.gradeList.value = Resource.success(l)
        viewModel.courseList.value = Resource.success(l)
        viewModel.batchLimitList.value = Resource.success(l)

    }


    override fun onClick(view: View?) {
        when (view?.id) {

            binding.etBranch.id -> {

                if (viewModel.branchList.value!!.data != null &&
                    viewModel.branchList.value!!.data!!.size > 0
                ) {
                    viewModel.branchList.value!!.data!!.getBottomSheetList()
                        .showSingleSelectionBottomSheetWithArrayList(
                            childFragmentManager,
                            BranchBottomSheetID,
                            viewModel.branchId.get()
                        )
                } else binding.root.snackBar(resources.getString(R.string.please_wait))

            }

            binding.etGrade.id -> {

                if (viewModel.gradeList.value!!.data != null &&
                    viewModel.gradeList.value!!.data!!.size > 0
                ) {
                    viewModel.gradeList.value!!.data!!.getBottomSheetList()
                        .showSingleSelectionBottomSheetWithArrayList(
                            childFragmentManager,
                            GradeBottomSheetID,
                            viewModel.gradeId.get()
                        )
                } else binding.root.snackBar(resources.getString(R.string.please_wait))

            }

            binding.etCourse.id -> {

                if (viewModel.courseList.value!!.data != null &&
                    viewModel.courseList.value!!.data!!.size > 0
                ) {
                    viewModel.courseList.value!!.data!!.getBottomSheetList()
                        .showSingleSelectionBottomSheetWithArrayList(
                            childFragmentManager,
                            CourseBottomSheetID,
                            viewModel.courseId.get()
                        )
                } else binding.root.snackBar(resources.getString(R.string.please_wait))

            }


            binding.etBatchLimit.id -> {

                if (viewModel.batchLimitList.value!!.data != null &&
                    viewModel.batchLimitList.value!!.data!!.size > 0
                ) {
                    viewModel.batchLimitList.value!!.data!!.getBottomSheetList()
                        .showSingleSelectionBottomSheetWithArrayList(
                            childFragmentManager,
                            BatchLimitBottomSheetID,
                            viewModel.batchLimitId.get()
                        )
                } else binding.root.snackBar(resources.getString(R.string.please_wait))

            }

            binding.etDateRange.id -> {

                binding.etDateRange.showMaterialDatePicker(
                    requireActivity() as AppCompatActivity,
                    viewModel.startDate,
                    viewModel.endDate
                ) {

                }

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

    private fun setClickListeners() {
        binding.etBranch.setOnClickListener(this)
        binding.etGrade.setOnClickListener(this)
        binding.etCourse.setOnClickListener(this)
        binding.etBatchLimit.setOnClickListener(this)
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

    override fun onChooseSingleItem(BOTTOM_SHEET_ID: Int, selectedItem: BottomSheetItem) {
        when (BOTTOM_SHEET_ID) {

            BranchBottomSheetID -> {
                if (selectedItem.id != null) {
                    binding.etBranch.setText(selectedItem.name)
                    viewModel.branchId.set(selectedItem.id)
//                    viewModel.getSubjectFromApi()
                }
            }

            GradeBottomSheetID -> {
                if (selectedItem.id != null) {
                    binding.etGrade.setText(selectedItem.name)
                    viewModel.gradeId.set(selectedItem.id)
//                    viewModel.getSubjectFromApi()
                }
            }

            CourseBottomSheetID -> {
                if (selectedItem.id != null) {
                    binding.etCourse.setText(selectedItem.name)
                    viewModel.courseId.set(selectedItem.id)
//                    viewModel.getSubjectFromApi()
                }
            }

            BatchLimitBottomSheetID -> {
                if (selectedItem.id != null) {
                    binding.etBatchLimit.setText(selectedItem.name)
                    viewModel.batchLimitId.set(selectedItem.id)
//                    viewModel.getSubjectFromApi()
                }
            }

        }
    }

    private fun initTextForFilters() {
        viewModel.branchList.value?.data.let {
            onChooseSingleItem(
                BranchBottomSheetID,
                viewModel.branchList.value?.data.getBottomSheetItem(viewModel.branchId.get())
            )
        }

        viewModel.gradeList.value?.data.let {
            onChooseSingleItem(
                GradeBottomSheetID,
                viewModel.branchList.value?.data.getBottomSheetItem(viewModel.branchId.get())
            )
        }

        viewModel.courseList.value?.data.let {
            onChooseSingleItem(
                CourseBottomSheetID,
                viewModel.branchList.value?.data.getBottomSheetItem(viewModel.branchId.get())
            )
        }

        viewModel.batchLimitList.value?.data.let {
            onChooseSingleItem(
                BatchLimitBottomSheetID,
                viewModel.branchList.value?.data.getBottomSheetItem(viewModel.branchId.get())
            )
        }
    }

    private fun clearAllFilters() {
        viewModel.branchId.set(-1)
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