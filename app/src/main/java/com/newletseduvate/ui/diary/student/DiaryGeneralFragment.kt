package com.newletseduvate.ui.diary.student

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.newletseduvate.R
import com.newletseduvate.adapter.dynamic_recycler_adapter.CallBackModel
import com.newletseduvate.adapter.dynamic_recycler_adapter.RecyclerDynamicAdapter
import com.newletseduvate.base.BaseFragment
import com.newletseduvate.databinding.AdapterDiaryGeneralBinding
import com.newletseduvate.databinding.FragmentGeneralDiaryBinding
import com.newletseduvate.model.diary.GeneralDiaryModel
import com.newletseduvate.utils.extensions.setUpRecyclerView
import com.newletseduvate.viewmodels.DiaryStudentViewModel

class DiaryGeneralFragment : BaseFragment(R.layout.fragment_general_diary) {

    private lateinit var binding: FragmentGeneralDiaryBinding
    private val viewModel by lazy { getViewModel<DiaryStudentViewModel>(requireActivity()) }

    lateinit var adapter: RecyclerDynamicAdapter<AdapterDiaryGeneralBinding, GeneralDiaryModel>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentGeneralDiaryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewModel = viewModel

        val callbacks = ArrayList<CallBackModel<GeneralDiaryModel>>()

        adapter = binding.rvList.setUpRecyclerView(
            R.layout.adapter_diary_general,
            callbacks,
            viewModel.generalDiaryListResponse,
            viewLifecycleOwner,
            binding.root,
            { viewModel.getGeneralDiaryMessages() },
            viewModel.isNoDataGeneral,
            viewModel.totalPageGeneral,
            viewModel.isPageLoadingGeneral,
            viewModel.isDataLoadingGeneral,
            viewModel.currentPageGeneral
        ) {}
    }

    companion object {
        @JvmStatic
        fun newInstance() = DiaryGeneralFragment()
    }
}