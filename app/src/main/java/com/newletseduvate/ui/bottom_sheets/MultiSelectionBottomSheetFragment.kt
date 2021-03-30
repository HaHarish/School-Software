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
import com.newletseduvate.databinding.BottomSheetMultipleSelectionBinding
import com.newletseduvate.model.BottomSheetItem

class MultiSelectionBottomSheetFragment(
    private val BOTTOM_SHEET_ID: Int,
    originalList: ArrayList<BottomSheetItem>
) : BottomSheetDialogFragment(), View.OnClickListener {

    lateinit var binding: BottomSheetMultipleSelectionBinding
    private lateinit var listener: OnChooseReasonListener

    private lateinit var bottomSheetAdapter: BottomSheetAdapter
    private lateinit var bottomSheetItemClickListener: BottomSheetAdapter.OnItemClickListener

    private var arrayList: ArrayList<BottomSheetItem> = ArrayList()

    init {
        for (modelObject in originalList)
            arrayList.add(modelObject.copy())
    }

    interface OnChooseReasonListener {
        fun onChooseMultipleItem(BOTTOM_SHEET_ID: Int, originalList: ArrayList<BottomSheetItem>)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = parentFragment as OnChooseReasonListener
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = BottomSheetMultipleSelectionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnDone.setOnClickListener(this)

        bottomSheetItemClickListener = object : BottomSheetAdapter.OnItemClickListener {
            override fun onItemClick(item: BottomSheetItem, position: Int) {
                arrayList[position].selected = !arrayList[position].selected
                bottomSheetAdapter.notifyDataSetChanged()
            }
        }

        initRecyclerAdapter()
    }

    private fun initRecyclerAdapter() {
        val linearLayoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
        binding.rvOptions.layoutManager = linearLayoutManager
        bottomSheetAdapter = BottomSheetAdapter(bottomSheetItemClickListener, arrayList)
        binding.rvOptions.adapter = bottomSheetAdapter
    }

    override fun onClick(view: View) {
        if (view.id == binding.btnDone.id) {
            listener.onChooseMultipleItem(BOTTOM_SHEET_ID, arrayList)
            dismiss()
        }
    }
}