package com.newletseduvate.adapter

/**
 * Created by SHASHANK BHAT on 17-Feb-21.
 *
 *
 */

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.newletseduvate.databinding.AdapterDiaryDailyBinding
import com.newletseduvate.model.diary.DailyDiaryModel

class DiaryDailyAdapter(val listener: OnItemClickListener) :
    RecyclerView.Adapter<DiaryDailyAdapter.MyViewHolder>() {

    private var list: ArrayList<DailyDiaryModel> = ArrayList()


    interface OnItemClickListener {
        fun selectItem(model: DailyDiaryModel, position: Int)
        fun showOptions(model: DailyDiaryModel, position: Int)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {

        val inflater = LayoutInflater.from(parent.context)

        val binding = AdapterDiaryDailyBinding.inflate(inflater, parent, false)
        return MyViewHolder(binding, listener)
    }

    class MyViewHolder(val binding: AdapterDiaryDailyBinding, val listener: OnItemClickListener) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindTo(model: DailyDiaryModel) {
            binding.model = model

//            binding.ibOptions.setOnClickListener {
//                listener.showOptions(model, adapterPosition)
//            }
        }
    }

    override fun getItemCount(): Int = list.size

    fun submitList(subList: ArrayList<DailyDiaryModel>) {
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