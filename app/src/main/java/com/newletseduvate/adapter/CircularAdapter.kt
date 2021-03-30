package com.newletseduvate.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import com.newletseduvate.R
import com.newletseduvate.utils.extensions.openMediaFile

class CircularAdapter(private val dataSet: List<String>, val context: Context, val branchName: String) :
    RecyclerView.Adapter<CircularAdapter.ViewHolder>() {

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val viewMediaButton: Button

        init {
            // Define click listener for the ViewHolder's View.
            viewMediaButton = view.findViewById(R.id.button_circular_details_media)
        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.adapter_circular_details_media_file, viewGroup, false)

        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.viewMediaButton.setOnClickListener {
            context.openMediaFile("https://erp-revamp.s3.ap-south-1.amazonaws.com/" +
                    "dev/circular_files/"+branchName+"/"+dataSet.get(position))
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size

}
