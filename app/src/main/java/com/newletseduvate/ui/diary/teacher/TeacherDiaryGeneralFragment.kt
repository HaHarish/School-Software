package com.newletseduvate.ui.diary.teacher

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.newletseduvate.R
import com.newletseduvate.adapter.dynamic_recycler_adapter.CallBackModel
import com.newletseduvate.adapter.dynamic_recycler_adapter.RecyclerDynamicAdapter
import com.newletseduvate.base.BaseFragment
import com.newletseduvate.databinding.AdapterTeacherDiaryGeneralBinding
import com.newletseduvate.databinding.FragmentTeacherDailyDiaryBinding
import com.newletseduvate.model.BottomSheetIconItem
import com.newletseduvate.model.diary.GeneralDiaryModel
import com.newletseduvate.ui.bottom_sheets.SingleSelectionIconBottomSheetFragment
import com.newletseduvate.utils.extensions.setUpRecyclerView
import com.newletseduvate.viewmodels.DiaryTeacherViewModel

class TeacherDiaryGeneralFragment : BaseFragment(R.layout.fragment_teacher_general_diary) {

    private lateinit var binding: FragmentTeacherDailyDiaryBinding
    private val viewModel by lazy { getViewModel<DiaryTeacherViewModel>(requireActivity()) }

    lateinit var adapter: RecyclerDynamicAdapter<AdapterTeacherDiaryGeneralBinding, GeneralDiaryModel>

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

        val callbacks = ArrayList<CallBackModel<GeneralDiaryModel>>()
        callbacks.add(CallBackModel(R.id.ib_options){ model, position ->
            showOptionMenu(model, position)
        })

        adapter = binding.rvList.setUpRecyclerView(
            R.layout.adapter_teacher_diary_general,
            callbacks,
            viewModel.generalDiaryListResponse,
            viewLifecycleOwner,
            binding.root,
            {
                viewModel.getGeneralDiaryMessages()
            },
            viewModel.isNoDataGeneral,
            viewModel.totalPageGeneral,
            viewModel.isPageLoadingGeneral,
            viewModel.isDataLoadingGeneral,
            viewModel.currentPageGeneral
        ) {}
    }

    private fun showOptionMenu(model: GeneralDiaryModel, positionAdapter: Int) {
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
        fun newInstance() = TeacherDiaryGeneralFragment()
    }
}