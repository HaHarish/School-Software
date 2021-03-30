package com.newletseduvate.ui.bottom_sheets

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.newletseduvate.adapter.BottomSheetAdapter
import com.newletseduvate.databinding.BottomSheetSingleSelectionBinding
import com.newletseduvate.model.BottomSheetItem

class SingleSelectionBottomSheetFragment(
    private val BOTTOM_SHEET_ID: Int,
    private var arrayList: ArrayList<BottomSheetItem>
) : BottomSheetDialogFragment() {

    private lateinit var listener: OnChooseReasonListener

    lateinit var binding: BottomSheetSingleSelectionBinding
    private lateinit var bottomSheetAdapter: BottomSheetAdapter

    private lateinit var bottomSheetItemClickListener: BottomSheetAdapter.OnItemClickListener

    interface OnChooseReasonListener {
        fun onChooseSingleItem(BOTTOM_SHEET_ID: Int, selectedItem: BottomSheetItem)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = parentFragment as OnChooseReasonListener
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = BottomSheetSingleSelectionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerAdapter()
    }

    private fun initRecyclerAdapter() {
        bottomSheetItemClickListener = object : BottomSheetAdapter.OnItemClickListener {
            override fun onItemClick(item: BottomSheetItem, position: Int) {
                listener.onChooseSingleItem(BOTTOM_SHEET_ID, arrayList[position])
                dismiss()
            }
        }

        bottomSheetAdapter = BottomSheetAdapter(bottomSheetItemClickListener, arrayList)
        binding.rvOptions.layoutManager =
            LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
        binding.rvOptions.adapter = bottomSheetAdapter
    }
}