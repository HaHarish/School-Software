package com.newletseduvate.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.newletseduvate.R
import com.newletseduvate.model.online_class.CoursePeriod

class ViewCoursePlanAdapter(private val dataSet: List<CoursePeriod>, val context: Context) :
    RecyclerView.Adapter<ViewCoursePlanAdapter.ViewHolder>() {

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val periodNo: TextView
        val periodTitle: TextView
        val periodDescription : TextView
        val periodNoOfFiles: TextView

        init {
            // Define click listener for the ViewHolder's View.
            periodNo = view.findViewById(R.id.tv_viewCoursePlan_periodNumber)
            periodTitle = view.findViewById(R.id.tv_viewCoursePlan_periodTitle)
            periodDescription = view.findViewById(R.id.tv_viewCoursePlan_periodDescription)
            periodNoOfFiles = view.findViewById(R.id.tv_viewCoursePlan_periodNoOfFiles)
        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.adapter_view_course_plan, viewGroup, false)

        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.periodNo.text = "Period ${position + 1}"
        viewHolder.periodTitle.text = dataSet.get(position).title
        viewHolder.periodDescription.text = dataSet.get(position).description
        viewHolder.periodNoOfFiles.text = "No of Files : "+dataSet.get(position).files?.size
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size

}