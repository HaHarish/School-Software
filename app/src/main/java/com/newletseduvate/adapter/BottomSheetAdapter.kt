package com.newletseduvate.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.newletseduvate.R
import com.newletseduvate.databinding.AdapterSingleSelectionItemBinding
import com.newletseduvate.model.BottomSheetItem
import com.newletseduvate.utils.constants.Constants.BOTTOM_SHEET_ITEM_TEXT_SIZE_SELECTED
import com.newletseduvate.utils.constants.Constants.BOTTOM_SHEET_ITEM_TEXT_SIZE_UNSELECTED

class BottomSheetAdapter(
    val listener: OnItemClickListener,
    private val listItems: ArrayList<BottomSheetItem>
) : RecyclerView.Adapter<BottomSheetAdapter.MyViewHolder>() {

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun getItemCount(): Int {
        return listItems.size
    }

    interface OnItemClickListener {
        fun onItemClick(item: BottomSheetItem, position: Int)
    }

    class MyViewHolder(
        var binding: AdapterSingleSelectionItemBinding,
        private val listener: OnItemClickListener
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bindTo(bottomSheetItem: BottomSheetItem) {
            binding.tvItemText.text = bottomSheetItem.name

            if (bottomSheetItem.selected) {
                binding.tvItemText.setTextColor(
                    ContextCompat.getColor(
                        binding.root.context,
                        R.color.bottom_sheet_adapter_item_text_colour_selected
                    )
                )
                binding.tvItemText.textSize = BOTTOM_SHEET_ITEM_TEXT_SIZE_SELECTED
            } else {
                binding.tvItemText.setTextColor(
                    ContextCompat.getColor(
                        binding.root.context,
                        R.color.bottom_sheet_adapter_item_text_colour_unselected
                    )
                )

                binding.tvItemText.textSize = BOTTOM_SHEET_ITEM_TEXT_SIZE_UNSELECTED
            }

            binding.clickableLayout.setOnClickListener {
                listener.onItemClick(bottomSheetItem, adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = AdapterSingleSelectionItemBinding.inflate(inflater, parent, false)
        return MyViewHolder(binding, listener)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        if (listItems[position].id != null && listItems[position].name != null) {
            holder.bindTo(listItems[position])
        }

    }

}