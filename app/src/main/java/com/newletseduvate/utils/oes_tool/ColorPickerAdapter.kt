package com.newletseduvate.utils.oes_tool

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.newletseduvate.R
import com.newletseduvate.databinding.OesToolCustomColorBinding

class ColorPickerAdapter(private var context: Context, private var listitems: ArrayList<Int>) :
        RecyclerView.Adapter<ColorPickerAdapter.MyViewHolder>() {
    private var onColorPickerClickListener: OnColorPickerClickListener? = null
    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun getItemCount(): Int {
        return listitems.size
    }

    interface OnColorPickerClickListener {
        fun onColorPickerClickListener(colorCode: Int)
    }

    class MyViewHolder(var binding: OesToolCustomColorBinding) :
            RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding: OesToolCustomColorBinding = DataBindingUtil.inflate(
                inflater,
                R.layout.oes_tool_custom_color,
                parent,
                false
        )
        return MyViewHolder(binding)
    }

    fun setOnColorPickerClickListener(onColorPickerClickListener: OnColorPickerClickListener?) {
        this.onColorPickerClickListener = onColorPickerClickListener
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        var itemviewModel = Custom_ColorViewModel(listitems.get(position))
        holder.binding.itemmodel = itemviewModel
        holder.binding.colorPickerView.setOnClickListener {
            if (onColorPickerClickListener != null) {
                onColorPickerClickListener!!.onColorPickerClickListener(listitems.get(position))
            }

        }
    }

}