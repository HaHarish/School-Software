package com.newletseduvate.ui.bottom_sheets

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

import com.newletseduvate.adapter.BottomSheetIconAdapter
import com.newletseduvate.databinding.BottomSheetSingleSelectionIconBottomSheetBinding
import com.newletseduvate.model.BottomSheetIconItem

class SingleSelectionIconBottomSheetFragment(
    private val BOTTOM_SHEET_ID: Int,
    private var arrayList: ArrayList<BottomSheetIconItem>,
    private var title: String,
    private var listener: OnChooseReasonListener
) : BottomSheetDialogFragment() {

    lateinit var binding: BottomSheetSingleSelectionIconBottomSheetBinding
    private lateinit var bottomSheetAdapter: BottomSheetIconAdapter

    private lateinit var bottomSheetItemClickListener: BottomSheetIconAdapter.OnItemClickListener

    interface OnChooseReasonListener {
        fun onChooseSingleItem(
            BOTTOM_SHEET_ID: Int,
            selectedItem: BottomSheetIconItem,
            position:Int
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = BottomSheetSingleSelectionIconBottomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerAdapter()
    }

    private fun initRecyclerAdapter() {

        bottomSheetItemClickListener = object : BottomSheetIconAdapter.OnItemClickListener {
            override fun onItemClick(item: BottomSheetIconItem, position: Int) {
                listener.onChooseSingleItem(BOTTOM_SHEET_ID, item, position)
                dismiss()
            }
        }

        if(title.isNotEmpty()){
            binding.tvTitle.visibility = View.VISIBLE
            binding.tvTitle.text = title
        }else
            binding.tvTitle.visibility = View.GONE

        bottomSheetAdapter = BottomSheetIconAdapter(bottomSheetItemClickListener, arrayList)
        binding.rvOptions.layoutManager =
            LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
        binding.rvOptions.adapter = bottomSheetAdapter
    }
}
