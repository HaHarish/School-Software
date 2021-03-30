package com.newletseduvate.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.newletseduvate.R
import com.newletseduvate.model.diary.teacher.TeacherDiaryActiveStudentsResponse
import com.newletseduvate.ui.circular.teacher.CircularTeacherUploadFileInterface
import com.newletseduvate.ui.diary.teacher.TeacherDiaryActiveStudentsInterface

class TeacherDiaryActiveStudentsAdapter (
    private val dataSet: ArrayList<TeacherDiaryActiveStudentsResponse.Result.NameResults>,
    val context: Context,
    val listener: TeacherDiaryActiveStudentsInterface,
    val isChecked: Boolean) :
    RecyclerView.Adapter<TeacherDiaryActiveStudentsAdapter.ViewHolder>() {

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val checkBox: CheckBox

        init {
            // Define click listener for the ViewHolder's View.
            checkBox = view.findViewById(R.id.cb_teacher_diary_active_students)
        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.adapter_teacher_diary_active_students, viewGroup, false)

        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element

        viewHolder.checkBox.isChecked = isChecked

        viewHolder.checkBox.text = "${dataSet.get(position).name}\n\n${dataSet.get(position).erpId}"

        viewHolder.checkBox.setOnClickListener {
            listener.onCheckBoxClicked(position)
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size

}