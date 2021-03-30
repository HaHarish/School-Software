package com.newletseduvate.adapter

/**
 * Created by SHASHANK BHAT on 25-Feb-21.
 *
 *
 */

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.newletseduvate.databinding.AdapterBlogPublishedBinding
import com.newletseduvate.model.blog.StudentBlogModel

class BlogPublishedAdapter(val listener: OnItemClickListener) :
    RecyclerView.Adapter<BlogPublishedAdapter.MyViewHolder>() {

    private var list: ArrayList<StudentBlogModel> = ArrayList()


    interface OnItemClickListener {
        fun selectItem(model: StudentBlogModel, position: Int)
        fun showOptions(model: StudentBlogModel, position: Int)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {

        val inflater = LayoutInflater.from(parent.context)

        val binding = AdapterBlogPublishedBinding.inflate(inflater, parent, false)
        return MyViewHolder(binding, listener)
    }

    class MyViewHolder(val binding: AdapterBlogPublishedBinding, val listener: OnItemClickListener) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindTo(model: StudentBlogModel) {
            binding.model = model

            binding.root.setOnClickListener {
                listener.selectItem(model, adapterPosition)
            }
        }
    }

    override fun getItemCount(): Int = list.size

    fun submitList(subList: ArrayList<StudentBlogModel>) {
        list.addAll(subList)
        notifyDataSetChanged()
    }

    fun clearList() {
        list.clear()
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bindTo(list[position])
    }
}