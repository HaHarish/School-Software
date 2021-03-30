package com.newletseduvate.ui.diary.student

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.newletseduvate.R
import com.newletseduvate.base.BaseDialogFragment
import com.newletseduvate.databinding.FragmentDiaryStudentFilterBinding
import com.newletseduvate.utils.extensions.datePickerDialog
import com.newletseduvate.viewmodels.DiaryStudentViewModel

class DiaryStudentFilter : BaseDialogFragment(),
    View.OnClickListener {

    private lateinit var mFilter: Filter

    private lateinit var binding: FragmentDiaryStudentFilterBinding
    private val viewModel by lazy { getViewModel<DiaryStudentViewModel>(requireActivity()) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDiaryStudentFilterBinding.inflate(inflater, container, false)
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
    }


    override fun onClick(view: View?) {
        when (view?.id) {

            binding.etFromDate.id -> view.datePickerDialog(viewModel.fromDate)

            binding.etToDate.id -> view.datePickerDialog(viewModel.toDate)

            binding.btnApply.id -> {
                mFilter.onDone(true)
                dismiss()
            }

            binding.btnReset.id -> {
                mFilter.onDone(false)
                dismiss()
            }
        }
    }

    private fun setClickListeners() {
        binding.etToDate.setOnClickListener(this)
        binding.etFromDate.setOnClickListener(this)

        binding.btnReset.setOnClickListener(this)
        binding.btnApply.setOnClickListener(this)
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

}