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
import com.newletseduvate.databinding.AdapterOnlineClassAttendBinding
import com.newletseduvate.databinding.FragmentOnlineClassAttendDetailsBinding
import com.newletseduvate.model.online_class.OnlineClassStudentOcDetailsModifiedModel
import com.newletseduvate.model.online_class.StudentOnlineClassModel
import com.newletseduvate.utils.extensions.openInChrome
import com.newletseduvate.utils.extensions.setUpRecyclerView
import com.newletseduvate.utils.extensions.snackBar
import com.newletseduvate.viewmodels.OnlineClassAttendDetailsViewModel
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList

class OnlineClassAttendDetailsFragment :
    BaseFragment(R.layout.fragment_online_class_attend_details),
    View.OnClickListener {

    var courseId : Int = 0
    var currentServerTime: String = ""

    private lateinit var binding: FragmentOnlineClassAttendDetailsBinding
    private val viewModel by lazy { getViewModel<OnlineClassAttendDetailsViewModel>(this) }

    private lateinit var onlineClassModel: StudentOnlineClassModel
    private lateinit var adapter: RecyclerDynamicAdapter<AdapterOnlineClassAttendBinding, OnlineClassStudentOcDetailsModifiedModel>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentOnlineClassAttendDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewModel = viewModel

        setClickListeners()

        currentServerTime = requireArguments().getString("CURRENT_SERVER_TIME", null)

        courseId = requireArguments().getInt("COURSE_ID")
        onlineClassModel =
            requireArguments().getSerializable(OnlineClassAttendFragment.modelConstant) as StudentOnlineClassModel
        viewModel.isViewCoursePlan = requireArguments().getBoolean("isViewCoursePlan")
        binding.time = viewModel.onlineClassListResponse.value?.data?.size.toString()
        binding.tvTitle.text = onlineClassModel.onlineClass.title

        val callbacks = ArrayList<CallBackModel<OnlineClassStudentOcDetailsModifiedModel>>()
        callbacks.add(CallBackModel(R.id.btn_accept) { model, position ->
            try {
                val format1 = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                if (currentServerTime.isNotEmpty() && model.date.isNotEmpty()) {
                    val date1 = format1.parse(currentServerTime)
                    val date2 = format1.parse(model.date)
                    if (date1?.compareTo(date2) == 0) {

                        try {
                            if (currentServerTime.isNotEmpty() && model.date.isNotEmpty()) {

                                val timeFormat = SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.getDefault())

                                val currServerTime = timeFormat.parse(currentServerTime)
                                val classEndTime = timeFormat.parse("${model.date} ${model.endTime}")

                                if (currServerTime.before(classEndTime)) {
                                    viewModel.putMarkAttendance(
                                        model.date,
                                        "is_accepted",
                                        onlineClassModel.id.toString()
                                    )
                                    model.isAccepted.set(true)
                                }else
                                    binding.root.snackBar("Class got over")

                            }
                        } catch (e: Exception) {}

                    } else {
                        binding.root.snackBar("Please accept on the same day of class starts.")
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

        })
        callbacks.add(CallBackModel(R.id.btn_reject) { model, position ->
            try {
                if (currentServerTime.isNotEmpty() && model.date.isNotEmpty()) {

                    val timeFormat = SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.getDefault())

                    val currServerTime = timeFormat.parse(currentServerTime)
                    val classEndTime = timeFormat.parse("${model.date} ${model.endTime}")

                    if (currServerTime.before(classEndTime)) {
                        viewModel.putMarkAttendance(
                            model.date,
                            "is_restricted",
                            onlineClassModel.id.toString()
                        )

                        model.isRestricted.set(true)
                    }else
                        binding.root.snackBar("Class got over")

                }
            } catch (e: Exception) {}

        })

        callbacks.add(CallBackModel(R.id.btn_join) { model, position ->
            requireContext().openInChrome(onlineClassModel.joinUrl)
        })

        adapter = binding.rvList.setUpRecyclerView(
            R.layout.adapter_online_class_attend_details,
            callbacks,
            viewModel.onlineClassListResponse,
            viewLifecycleOwner,
            binding.root,
            {
                viewModel.getStudentOnlineClassOcDetails(onlineClassModel.id.toString(), currentServerTime)
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
        binding.btnResources.setOnClickListener(this)
        binding.btnViewCoursePlan.setOnClickListener(this)
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            OnlineClassAttendDetailsFragment()
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            binding.btnResources.id -> {
                val bundle = Bundle()
                bundle.putString("ID", onlineClassModel.onlineClass.id.toString())
                bundle.putSerializable("LIST", adapter.getItemList())
                findNavController().navigate(R.id.nav_attend_online_class_resource, bundle)
            }
            binding.btnViewCoursePlan.id -> {
                val bundle = Bundle()
                bundle.putInt("COURSE_ID", courseId)
                findNavController().navigate(R.id.nav_view_course_plan, bundle)
            }
        }
    }
}