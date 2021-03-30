package com.newletseduvate.ui.diary.student

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.newletseduvate.R
import com.newletseduvate.adapter.dynamic_recycler_adapter.CallBackModel
import com.newletseduvate.adapter.dynamic_recycler_adapter.RecyclerDynamicAdapter
import com.newletseduvate.base.BaseFragment
import com.newletseduvate.databinding.AdapterDiaryDailyBinding
import com.newletseduvate.databinding.FragmentDailyDiaryBinding
import com.newletseduvate.model.diary.DailyDiaryModel
import com.newletseduvate.utils.extensions.setUpRecyclerView
import com.newletseduvate.viewmodels.DiaryStudentViewModel


class DiaryDailyFragment : BaseFragment(R.layout.fragment_daily_diary) {

    private lateinit var binding: FragmentDailyDiaryBinding
    private val viewModel by lazy { getViewModel<DiaryStudentViewModel>(requireActivity()) }
    private lateinit var adapter: RecyclerDynamicAdapter<AdapterDiaryDailyBinding, DailyDiaryModel>

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        binding = FragmentDailyDiaryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewModel = viewModel

        val callbacks = ArrayList<CallBackModel<DailyDiaryModel>>()

        adapter = binding.rvList.setUpRecyclerView(
                R.layout.adapter_diary_daily,
                callbacks,
                viewModel.dailyDiaryListResponse,
                viewLifecycleOwner,
                binding.root,
                { viewModel.getDailyDiaryMessages() },
                viewModel.isNoDataDaily,
                viewModel.totalPageDaily,
                viewModel.isPageLoadingDaily,
                viewModel.isDataLoadingDaily,
                viewModel.currentPageDaily,
        ){}
    }

    companion object {

        @JvmStatic
        fun newInstance() = DiaryDailyFragment()
    }
}
