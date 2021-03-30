package com.newletseduvate.adapter

/**
 * Created by SHASHANK BHAT on 18-Feb-21.
 *
 *
 */
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.newletseduvate.databinding.AdapterOnlineClassAttendBinding
import com.newletseduvate.model.online_class.StudentOnlineClassModel

class OnlineClassAttendAdapter : RecyclerView.Adapter<OnlineClassAttendAdapter.MyViewHolder>() {

    private var list: ArrayList<StudentOnlineClassModel> = ArrayList()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {

        val inflater = LayoutInflater.from(parent.context)

        val binding = AdapterOnlineClassAttendBinding.inflate(inflater, parent, false)
        return MyViewHolder(binding)
    }

    class MyViewHolder(val binding: AdapterOnlineClassAttendBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindTo(model: StudentOnlineClassModel) {
            binding.model = model
        }
    }

    override fun getItemCount(): Int = list.size

    fun submitList(subList: ArrayList<StudentOnlineClassModel>) {
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