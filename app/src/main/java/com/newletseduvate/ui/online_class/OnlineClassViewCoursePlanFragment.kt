package com.newletseduvate.ui.online_class

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.newletseduvate.R
import com.newletseduvate.adapter.CircularAdapter
import com.newletseduvate.adapter.ViewCoursePlanAdapter
import com.newletseduvate.base.BaseFragment
import com.newletseduvate.utils.Status
import com.newletseduvate.viewmodels.ViewCoursePlanViewModel
import kotlinx.android.synthetic.main.fragment_circular_details.*
import kotlinx.android.synthetic.main.fragment_view_course_plan.*

class OnlineClassViewCoursePlanFragment : BaseFragment(R.layout.fragment_view_course_plan) {

    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var adapter: ViewCoursePlanAdapter

    private val viewModel by lazy { getViewModel<ViewCoursePlanViewModel>(this) }

    var courseId: Int = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        linearLayoutManager = LinearLayoutManager(requireContext())
        recyclerView_view_course_plan.layoutManager = linearLayoutManager

        courseId = requireArguments().getInt("COURSE_ID")

        viewModel.getViewCoursePlan(courseId)
        viewModel.viewCoursePlanResponse.observe(viewLifecycleOwner, {
            it?.let {
                when(it.status){
                    Status.Success -> {
                        adapter = ViewCoursePlanAdapter(it.data?.result?.get(0)?.courseId?.coursePeriod!!, requireContext())
                        recyclerView_view_course_plan.adapter = adapter
                        adapter.notifyDataSetChanged()
                    }
                    Status.Error -> {
                        Toast.makeText(activity,it.message, Toast.LENGTH_SHORT).show()
                    }
                    else -> {
                        Toast.makeText(activity,it.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })
    }

    override fun onResume() {
        super.onResume()
        setToolBarTitle(getString(R.string.view_course_plan))
    }

}