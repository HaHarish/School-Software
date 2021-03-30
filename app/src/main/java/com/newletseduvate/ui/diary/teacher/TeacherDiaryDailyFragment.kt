package com.newletseduvate.ui.diary.teacher

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.newletseduvate.R
import com.newletseduvate.adapter.dynamic_recycler_adapter.CallBackModel
import com.newletseduvate.adapter.dynamic_recycler_adapter.RecyclerDynamicAdapter
import com.newletseduvate.base.BaseFragment
import com.newletseduvate.databinding.AdapterTeacherDiaryDailyBinding
import com.newletseduvate.databinding.FragmentTeacherDailyDiaryBinding
import com.newletseduvate.model.BottomSheetIconItem
import com.newletseduvate.model.diary.DailyDiaryModel
import com.newletseduvate.ui.bottom_sheets.SingleSelectionIconBottomSheetFragment
import com.newletseduvate.utils.extensions.setUpRecyclerView
import com.newletseduvate.viewmodels.DiaryTeacherViewModel


class TeacherDiaryDailyFragment : BaseFragment(R.layout.fragment_teacher_daily_diary) {

    private lateinit var binding: FragmentTeacherDailyDiaryBinding
    private val viewModel by lazy { getViewModel<DiaryTeacherViewModel>(requireActivity()) }
    private lateinit var adapter: RecyclerDynamicAdapter<AdapterTeacherDiaryDailyBinding, DailyDiaryModel>

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        binding = FragmentTeacherDailyDiaryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewModel = viewModel

        val callbacks = ArrayList<CallBackModel<DailyDiaryModel>>()
        callbacks.add(CallBackModel(R.id.ib_options){ model, position ->
            showOptionMenu(model, position)
        })

        adapter = binding.rvList.setUpRecyclerView(
                R.layout.adapter_teacher_diary_daily,
                callbacks,
                viewModel.dailyDiaryListResponse,
                viewLifecycleOwner,
                binding.root,
                {
                    viewModel.getDailyDiaryMessages()
                },
                viewModel.isNoDataDaily,
                viewModel.totalPageDaily,
                viewModel.isPageLoadingDaily,
                viewModel.isDataLoadingDaily,
                viewModel.currentPageDaily,
        ){}
    }

    private fun showOptionMenu(model: DailyDiaryModel, positionAdapter: Int) {
        val optionsArray = ArrayList<BottomSheetIconItem>()
        optionsArray.add(
            BottomSheetIconItem(
                0,
                R.drawable.ic_delete,
                resources.getString(R.string.delete)
            )
        )
        val bottomSheet = SingleSelectionIconBottomSheetFragment(
            0,
            optionsArray,
            "",
            object : SingleSelectionIconBottomSheetFragment.OnChooseReasonListener {

                override fun onChooseSingleItem(
                    BOTTOM_SHEET_ID: Int,
                    selectedItem: BottomSheetIconItem,
                    position: Int
                ) {
                    when (position) {
                        0 -> {
                            adapter.removeItemAt(positionAdapter)
                            viewModel.deleteTeacherDiary(model.id.toString())
                        }
                    }
                }

            })
        bottomSheet.show(
            childFragmentManager,
            bottomSheet.tag
        )
    }

    companion object {

        @JvmStatic
        fun newInstance() = TeacherDiaryDailyFragment()
    }
}
