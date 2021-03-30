package com.newletseduvate.ui.online_class

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ObservableInt
import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.findNavController
import com.newletseduvate.R
import com.newletseduvate.adapter.dynamic_recycler_adapter.CallBackModel
import com.newletseduvate.adapter.dynamic_recycler_adapter.RecyclerDynamicAdapter
import com.newletseduvate.base.BaseFragment
import com.newletseduvate.databinding.AdapterOnlineClassTeacherDetailsBinding
import com.newletseduvate.databinding.FragmentOnlineClassTeacherViewClassDetailsBinding
import com.newletseduvate.model.online_class.TeacherViewClassDetailsModel
import com.newletseduvate.model.online_class.TeacherViewClassModel
import com.newletseduvate.utils.extensions.openMediaFile
import com.newletseduvate.utils.extensions.setUpRecyclerView
import com.newletseduvate.viewmodels.OnlineClassTeacherViewClassDetailsViewModel
import kotlin.collections.ArrayList

class OnlineClassTeacherViewClassDetailsFragment :
    BaseFragment(R.layout.fragment_online_class_teacher_view_class_details),
    View.OnClickListener {

    var courseId: Int = 0


    private lateinit var binding: FragmentOnlineClassTeacherViewClassDetailsBinding
    private val viewModel by lazy { getViewModel<OnlineClassTeacherViewClassDetailsViewModel>(this) }


    private lateinit var adapter: RecyclerDynamicAdapter<AdapterOnlineClassTeacherDetailsBinding, TeacherViewClassDetailsModel>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            FragmentOnlineClassTeacherViewClassDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewModel = viewModel

        setClickListeners()

        viewModel.currentServerTime = requireArguments().getString("CURRENT_SERVER_TIME", "")

        courseId = requireArguments().getInt("COURSE_ID")
        viewModel.onlineClassModel =
            requireArguments().getSerializable(OnlineClassAttendFragment.modelConstant) as TeacherViewClassModel
        viewModel.isViewCoursePlan.set(requireArguments().getBoolean("isViewCoursePlan"))
//        binding.time = viewModel.onlineClassListResponse.value?.data?.size.toString()
        binding.tvTitle.text = viewModel.onlineClassModel.onlineClass?.title

        val callbacks = ArrayList<CallBackModel<TeacherViewClassDetailsModel>>()
        callbacks.add(CallBackModel(R.id.btn_cancel) { model, position ->
            adapter.removeItemAt(position)
            viewModel.putTeacherOnlineClassDetailsCancelClass(
                model.date.toString()
            )
        })
        callbacks.add(CallBackModel(R.id.btn_host) { model, position ->
            requireContext().openMediaFile(model.zoomUrl)
        })

        adapter = binding.rvList.setUpRecyclerView(
            R.layout.adapter_online_class_teacher_details,
            callbacks,
            viewModel.onlineClassDetailsListResponse,
            viewLifecycleOwner,
            binding.root,
            {
                viewModel.getTeacherOnlineClassDetails(viewModel.onlineClassModel.id.toString())
            },
            viewModel.isNoDataStudent,
            isPageLoadingDaily = viewModel.isPageLoadingStudent,
            currentPageDaily = MutableLiveData(1),
            totalPageDaily = ObservableInt(1)
        ) {}

        viewModel.isPageLoadingStudent.observe(viewLifecycleOwner, {
            it?.let {
                if (it)
                    displayProgress()
                else
                    dismissProgress()
            }
        })
    }

    private fun setClickListeners() {
        binding.btnAttendance.setOnClickListener(this)
        binding.btnViewCoursePlan.setOnClickListener(this)
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            OnlineClassTeacherViewClassDetailsFragment()
    }

    override fun onClick(v: View?) {
        when (v!!.id) {

            binding.btnViewCoursePlan.id -> {
                val bundle = Bundle()
                bundle.putInt("COURSE_ID", courseId)
                findNavController().navigate(R.id.nav_view_course_plan, bundle)
            }

            binding.btnAttendance.id -> {
                val bundle = Bundle()
                bundle.putSerializable(OnlineClassAttendFragment.modelConstant, viewModel.onlineClassModel)

                findNavController().navigate(R.id.nav_attend_online_class_attendance_list, bundle)
            }
        }
    }
}