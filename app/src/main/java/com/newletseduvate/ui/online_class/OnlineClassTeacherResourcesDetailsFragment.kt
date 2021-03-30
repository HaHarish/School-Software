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
import com.newletseduvate.databinding.FragmentOnlineClassTeacherResourcesDetailsBinding
import com.newletseduvate.model.online_class.TeacherViewClassDetailsModel
import com.newletseduvate.model.online_class.TeacherViewClassModel
import com.newletseduvate.utils.extensions.openMediaFile
import com.newletseduvate.utils.extensions.setUpRecyclerView
import com.newletseduvate.viewmodels.OnlineClassTeacherViewClassDetailsViewModel
import kotlin.collections.ArrayList

class OnlineClassTeacherResourcesDetailsFragment :
    BaseFragment(R.layout.fragment_online_class_teacher_resources_details),
    View.OnClickListener {

    var courseId: Int = 0


    private lateinit var binding: FragmentOnlineClassTeacherResourcesDetailsBinding
    private val viewModel by lazy { getViewModel<OnlineClassTeacherViewClassDetailsViewModel>(this) }


    private lateinit var adapter: RecyclerDynamicAdapter<AdapterOnlineClassTeacherDetailsBinding, TeacherViewClassDetailsModel>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            FragmentOnlineClassTeacherResourcesDetailsBinding.inflate(inflater, container, false)
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
//        callbacks.add(CallBackModel(R.id.btn_download) { model, position ->
//
//        })
        callbacks.add(CallBackModel(R.id.btn_upload) { model, position ->
            val bundle = Bundle()
            bundle.putSerializable(OnlineClassAttendFragment.modelConstant, viewModel.onlineClassModel)
            bundle.putString("date", model.date)
            findNavController().navigate(R.id.nav_online_class_teacher_resources_upload, bundle)

        })

        adapter = binding.rvList.setUpRecyclerView(
            R.layout.adapter_online_class_resources_details,
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
//        binding.btnResources.setOnClickListener(this)
//        binding.btnViewCoursePlan.setOnClickListener(this)
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            OnlineClassTeacherResourcesDetailsFragment()
    }

    override fun onClick(v: View?) {
        when (v!!.id) {

//            binding.btnViewCoursePlan.id -> {
//                val bundle = Bundle()
//                bundle.putInt("COURSE_ID", courseId)
//                findNavController().navigate(R.id.nav_view_course_plan, bundle)
//            }
        }
    }
}