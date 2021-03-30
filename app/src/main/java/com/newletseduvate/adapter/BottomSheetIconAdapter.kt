package com.newletseduvate.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.newletseduvate.databinding.AdapterSingleSelectionIconItemBinding
import com.newletseduvate.model.BottomSheetIconItem

class BottomSheetIconAdapter(
    val listener: OnItemClickListener,
    private val listItems: ArrayList<BottomSheetIconItem>
) : RecyclerView.Adapter<BottomSheetIconAdapter.MyViewHolder>() {

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun getItemCount(): Int {
        return listItems.size
    }

    interface OnItemClickListener {
        fun onItemClick(item: BottomSheetIconItem, position: Int)
    }

    class MyViewHolder(
        var binding: AdapterSingleSelectionIconItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bindTo(bottomSheetIconItem: BottomSheetIconItem, listener: OnItemClickListener) {
            binding.name.text = bottomSheetIconItem.name
            binding.name.setCompoundDrawablesWithIntrinsicBounds(bottomSheetIconItem.iconId, 0, 0, 0)

            binding.name.setOnClickListener {
                listener.onItemClick(bottomSheetIconItem, adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = AdapterSingleSelectionIconItemBinding.inflate(inflater, parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bindTo(listItems[position], listener)
    }

}