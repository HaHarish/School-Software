package com.newletseduvate.ui.online_class

import com.newletseduvate.databinding.FragmentOnlineAttendClassFilterBinding
import com.newletseduvate.viewmodels.OnlineClassAttendViewModel

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.newletseduvate.R
import com.newletseduvate.base.BaseDialogFragment
import com.newletseduvate.model.BottomSheetItem
import com.newletseduvate.model.online_class.ClassTypeModel
import com.newletseduvate.ui.bottom_sheets.SingleSelectionBottomSheetFragment
import com.newletseduvate.utils.Resource
import com.newletseduvate.utils.extensions.*

class OnlineClassAttendFilterFragment(private val mFilter: Filter) : BaseDialogFragment(),
    SingleSelectionBottomSheetFragment.OnChooseReasonListener,
    View.OnClickListener {

    private lateinit var binding: FragmentOnlineAttendClassFilterBinding
    private val viewModel by lazy { getViewModel<OnlineClassAttendViewModel>(requireActivity()) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentOnlineAttendClassFilterBinding.inflate(inflater, container, false)
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

        val classTypeList = ArrayList<ClassTypeModel>()
        classTypeList.add(ClassTypeModel(0, "Compulsory Class"))
        classTypeList.add(ClassTypeModel(1, "Optional Class"))
        classTypeList.add(ClassTypeModel(2, "Special Class"))
        classTypeList.add(ClassTypeModel(3, "Parent Class"))

        viewModel.classTypeList.value = Resource.success(classTypeList)

        initTextForFilters()
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
        binding.btnReset.setOnClickListener(this)
        binding.btnApply.setOnClickListener(this)
    }


    private fun initTopAppBar() {
        binding.appBarFilter.toolbar.setNavigationOnClickListener { dismiss() }
    }

    private val ClassTypeBottomSheetID = 0

    override fun onChooseSingleItem(BOTTOM_SHEET_ID: Int, selectedItem: BottomSheetItem) {
        when (BOTTOM_SHEET_ID) {

            ClassTypeBottomSheetID -> {
                if (selectedItem.id != null) {
                    binding.etClassType.setText(selectedItem.name)
                    viewModel.classTypeId.set(selectedItem.id)
                }
            }

        }
    }

    private fun initTextForFilters() {
        viewModel.classTypeList.value?.data.let {
            onChooseSingleItem(
                ClassTypeBottomSheetID,
                viewModel.classTypeList.value?.data.getBottomSheetItem(viewModel.classTypeId.get())
            )
        }
    }

    private fun clearAllFilters() {
        viewModel.classTypeId.set(0)
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