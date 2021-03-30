package com.newletseduvate.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.newletseduvate.R
import com.newletseduvate.ui.circular.teacher.CircularTeacherUploadFileInterface

class DiaryUploadFileAdapter (private val dataSet: ArrayList<String>, val context: Context,
                              val listener: CircularTeacherUploadFileInterface,
                              val branchName: String) :
    RecyclerView.Adapter<DiaryUploadFileAdapter.ViewHolder>() {

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val deleteButton: ImageView
        val imageFile: ImageView

        init {
            // Define click listener for the ViewHolder's View.
            deleteButton = view.findViewById(R.id.iv_delete_file)
            imageFile = view.findViewById(R.id.tv_file_item)
        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.adapter_teacher_circular_upload_file, viewGroup, false)

        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.deleteButton.setOnClickListener {
            listener.onClickedDeleteButton(position)
        }

        val imageUrl: String = "https://erp-revamp.s3.ap-south-1.amazonaws.com/"+
                "${dataSet.get(position)}"
        Log.d("TAGY","ImageURL: "+imageUrl)

        Glide.with(context)
            .load(imageUrl)
            .centerCrop()
            .placeholder(R.drawable.image_shape_empty)
            .into(viewHolder.imageFile)
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size

}