package com.newletseduvate.adapter

/**
 * Created by SHASHANK BHAT on 17-Feb-21.
 *
 *
 */
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.newletseduvate.databinding.AdapterDiaryGeneralBinding
import com.newletseduvate.model.diary.GeneralDiaryModel

class DiaryGeneralAdapter(val listener: OnItemClickListener) :
    RecyclerView.Adapter<DiaryGeneralAdapter.MyViewHolder>() {

    private var list: ArrayList<GeneralDiaryModel> = ArrayList()

    interface OnItemClickListener {
        fun selectItem(model: GeneralDiaryModel, position: Int)
        fun showOptions(model: GeneralDiaryModel, position: Int)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {

        val inflater = LayoutInflater.from(parent.context)

        val binding = AdapterDiaryGeneralBinding.inflate(inflater, parent, false)
        return MyViewHolder(binding, listener)
    }

    class MyViewHolder(val binding: AdapterDiaryGeneralBinding, val listener: OnItemClickListener) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindTo(model: GeneralDiaryModel) {
            binding.model = model

//            binding.ibOptions.setOnClickListener {
//                listener.showOptions(model, adapterPosition)
//            }
        }
    }

    override fun getItemCount(): Int = list.size

    fun submitList(subList: ArrayList<GeneralDiaryModel>) {
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