package com.newletseduvate.utils.oes_tool

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.newletseduvate.R
import com.newletseduvate.databinding.RowEditingToolsBinding
import java.util.*

internal class EditingToolsAdapter(
    private val mOnItemSelected: OnItemSelected
) : RecyclerView.Adapter<EditingToolsAdapter.ViewHolder>() {

    private val mToolList: MutableList<ToolModel> = ArrayList()

    interface OnItemSelected {
        fun onToolSelected(toolType: ToolType?)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = RowEditingToolsBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding, mToolList, mOnItemSelected)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindTo(mToolList[position], this)
    }

    override fun getItemCount(): Int {
        return mToolList.size
    }

    class ViewHolder(
        var binding: RowEditingToolsBinding,
        var mToolList: MutableList<ToolModel>,
        var mOnItemSelected: OnItemSelected
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bindTo(item: ToolModel, adapter: EditingToolsAdapter){
            val itemViewModel = Custom_BottomToolViewModel(item, binding.root.context)
            binding.itemmodel = itemViewModel
            binding.imgToolIcon.setImageResource(item.toolIcon)

            binding.root.setOnClickListener {
                for (tool in mToolList) {
                    tool.selected = false
                }
                val model = mToolList[layoutPosition]
                model.selected = true
                mOnItemSelected.onToolSelected(model.toolType)
                adapter.notifyDataSetChanged()
            }
        }
    }

    init {
        mToolList.add(ToolModel("Brush", R.drawable.ic_drawing, false, ToolType.BRUSH))
        mToolList.add(ToolModel("Text", R.drawable.ic_pencil, false, ToolType.TEXT))
        mToolList.add(ToolModel("Eraser", R.drawable.ic_rubber, false, ToolType.ERASER))
        mToolList.add(ToolModel("Rotate Left", R.drawable.ic_baseline_rotate_left_24, false, ToolType.LEFT))
        mToolList.add(ToolModel("Rotate Right", R.drawable.ic_baseline_rotate_right_24, false, ToolType.RIGHT))
        mToolList.add(ToolModel("Undo", R.drawable.ic_undo, false, ToolType.UNDO))
        mToolList.add(ToolModel("Redo", R.drawable.ic_redo, false, ToolType.REDO))
        mToolList.add(ToolModel("Reset", R.drawable.ic_reset, false, ToolType.RESET))
    }
}
