package com.newletseduvate.ui.homework

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.Observable
import com.newletseduvate.R
import com.newletseduvate.base.BaseDialogFragment
import com.newletseduvate.databinding.FragmentTeacherHomeworkFilterBinding
import com.newletseduvate.model.BottomSheetItem
import com.newletseduvate.ui.bottom_sheets.SingleSelectionBottomSheetFragment
import com.newletseduvate.utils.NetworkCheck
import com.newletseduvate.utils.extensions.*
import com.newletseduvate.viewmodels.HomeworkTeacherViewModel
import java.util.*
import kotlin.collections.ArrayList

class HomeworkTeacherFilterFragment : BaseDialogFragment(),
    SingleSelectionBottomSheetFragment.OnChooseReasonListener,
    View.OnClickListener {

    private lateinit var mFilter: Filter

    private lateinit var binding: FragmentTeacherHomeworkFilterBinding
    private val viewModel by lazy { getViewModel<HomeworkTeacherViewModel>(requireActivity()) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTeacherHomeworkFilterBinding.inflate(inflater, container, false)
        return binding.root
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

        viewModel.fromDate.addOnPropertyChangedCallback(object :
            Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {

                val calendar = Calendar.getInstance()
                calendar.timeInMillis = viewModel.fromDate.get()!!

                calendar.add(Calendar.DAY_OF_YEAR, 6)
                viewModel.toDate.set(calendar.timeInMillis)

                if (viewModel.fromDate.get() != 0L && viewModel.toDate.get() != 0L)
                    getTeacherHomeWork()
            }
        })
    }

    private fun getTeacherHomeWork() {
        try {
            if (NetworkCheck.isInternetAvailable(requireContext())) {
                viewModel.getTeacherHomeWork()
            } else {
                if(::binding.isInitialized){
                    binding.root.snackBar(getString(R.string.check_internet), getString(R.string.retry), true) {
                        getTeacherHomeWork()
                    }
                }
            }
        }catch (ex :Exception){}
    }


    override fun onClick(view: View?) {
        when (view?.id) {

            binding.etFromDate.id -> view.datePickerDialog(viewModel.fromDate)

            binding.btnApply.id -> {
                mFilter.onDone(true)
                dismiss()
            }

            binding.btnReset.id -> {
                mFilter.onDone(false)
                clearAllFilters()
                dismiss()
            }

            binding.etSubject.id -> {

                if (viewModel.subjectList.value != null &&
                    viewModel.subjectList.value!!.size > 0
                ) {
                    val arrayList = ArrayList<BottomSheetItem>()
                    viewModel.subjectList.value!!.forEach {
                        if(it.id != viewModel.subjectId.get())
                            it.selected = false
                        arrayList.add(it)
                    }

                    arrayList.showSingleSelectionBottomSheetWithArrayList(
                            childFragmentManager,
                            SubejctBottomSheetID,
                            viewModel.subjectId.get()
                        )
                } else binding.root.snackBar(resources.getString(R.string.please_wait))

            }
        }
    }

    private fun setClickListeners() {
        binding.etSubject.setOnClickListener(this)
        binding.etFromDate.setOnClickListener(this)
//        binding.etToDate.setOnClickListener(this)

        binding.btnReset.setOnClickListener(this)
        binding.btnApply.setOnClickListener(this)
    }


    private fun initTopAppBar() {
        binding.appBarFilter.toolbar.setNavigationOnClickListener { dismiss() }
    }

    private val SubejctBottomSheetID = 0

    override fun onChooseSingleItem(BOTTOM_SHEET_ID: Int, selectedItem: BottomSheetItem) {
        when (BOTTOM_SHEET_ID) {

            SubejctBottomSheetID -> {
                if (selectedItem.id != null) {
                    binding.etSubject.setText(selectedItem.name)
                    viewModel.subjectId.set(selectedItem.id)
                    viewModel.subjectName.set(selectedItem.name)
                }
            }
        }
    }

    private fun initTextForFilters() {
        viewModel.subjectList.value?.let {
            onChooseSingleItem(
                SubejctBottomSheetID,
                getBottomSheetItem(viewModel.subjectList.value, viewModel.subjectId.get())
            )
        }

    }

    private fun clearAllFilters() {
        viewModel.subjectId.set(-1)
    }

    private fun getBottomSheetItem(subjectList: ArrayList<BottomSheetItem>?, id: Int): BottomSheetItem {

        subjectList?.forEach {
            if (it.id == id)
                return it
        }
        return BottomSheetItem(-1, "", false)
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