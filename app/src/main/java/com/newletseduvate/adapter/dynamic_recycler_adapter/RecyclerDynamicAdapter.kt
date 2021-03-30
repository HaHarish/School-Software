package com.newletseduvate.adapter.dynamic_recycler_adapter


/**
 * Created by SHASHANK BHAT on 27-Feb-21.
 *
 *
 */

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.databinding.library.baseAdapters.BR
import androidx.recyclerview.widget.RecyclerView

class RecyclerDynamicAdapter<BIND_TYPE : ViewDataBinding, MODEL_TYPE>(
    private val layoutId: Int,
    private val callbacks: ArrayList<CallBackModel<MODEL_TYPE>>,
    val variableId: Int = BR.model
) :
    RecyclerView.Adapter<RecyclerViewHolder<BIND_TYPE>>() {

    private var list: ArrayList<MODEL_TYPE> = ArrayList()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerViewHolder<BIND_TYPE> {
        val inflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<BIND_TYPE>(inflater, layoutId, parent, false)
        return RecyclerViewHolder(binding)
    }

    override fun getItemCount(): Int = list.size

    fun submitList(subList: ArrayList<MODEL_TYPE>) {
        list.addAll(subList)
        notifyDataSetChanged()
    }

    fun replaceList(subList: ArrayList<MODEL_TYPE>) {
        list = subList
        notifyDataSetChanged()
    }

    fun clearList() {
        list.clear()
        notifyDataSetChanged()
    }

    fun removeItemAt(position: Int) {
        list.removeAt(position)
        notifyDataSetChanged()
    }

    fun addItemAt(position: Int, model: MODEL_TYPE) {
        list.add(position, model)
        notifyDataSetChanged()
    }

    fun add(model: MODEL_TYPE) {
        list.add(model)
        notifyDataSetChanged()
    }

    fun getItemList(): ArrayList<MODEL_TYPE>{
        return list
    }

    override fun onBindViewHolder(holder: RecyclerViewHolder<BIND_TYPE>, position: Int) {
        val item = list[position]
        holder.bindTo(item, variableId)
        holder.bindClickListener(item, callbacks)
    }

}