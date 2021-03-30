package com.newletseduvate.ui.online_class

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.newletseduvate.R
import androidx.fragment.app.FragmentActivity
import androidx.navigation.fragment.findNavController
import com.newletseduvate.adapter.dynamic_recycler_adapter.CallBackModel
import com.newletseduvate.base.BaseFragment
import com.newletseduvate.databinding.AdapterTeacherOnlineViewClassBinding
import com.newletseduvate.databinding.FragmentOnlineClassTeacherViewClassBinding
import com.newletseduvate.model.online_class.TeacherViewClassModel
import com.newletseduvate.utils.NetworkCheck
import com.newletseduvate.utils.constants.Constants
import com.newletseduvate.utils.extensions.setUpRecyclerView
import com.newletseduvate.utils.extensions.snackBar
import com.newletseduvate.utils.rx_component_observables.RxButtonObservable
import com.newletseduvate.viewmodels.OnlineClassTeacherViewClassViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class OnlineClassTeacherViewClassFragment : BaseFragment(R.layout.fragment_online_class_teacher_view_class) {
    private lateinit var binding: FragmentOnlineClassTeacherViewClassBinding
    private val viewModel by lazy { getViewModel<OnlineClassTeacherViewClassViewModel>(requireActivity()) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentOnlineClassTeacherViewClassBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getInitialData()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewModel = viewModel
        viewModel.moduleId.set(requireArguments().getInt(Constants.MODULE_ID))

        CompositeDisposable().add(
            RxButtonObservable.fromView(binding.ivFilter)
                .debounce(Constants.FILTER_DEBOUNCE_TIME, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    goToFilter()
                },{

                })
        )

        val callbacks = ArrayList<CallBackModel<TeacherViewClassModel>>()
        callbacks.add(CallBackModel(R.id.btn_view_more) { model, position ->
            val bundle = Bundle()
            bundle.putSerializable(OnlineClassAttendFragment.modelConstant, model)
            bundle.putInt("COURSE_ID",model.onlineClass?.courceId ?: 0)
            bundle.putString("CURRENT_SERVER_TIME", viewModel.onlineClassResponse.value?.data?.currentServerTime)
            bundle.putString("CLASS_START_TIME",viewModel.onlineClassListResponse.value?.data?.get(position)?.onlineClass?.startTime)
            bundle.putBoolean("isViewCoursePlan", viewModel.classTypeId.get() != 0)
            findNavController().navigate(R.id.nav_online_class_teacher_view_class_details, bundle)
        })

        binding.rvList.setUpRecyclerView<AdapterTeacherOnlineViewClassBinding, TeacherViewClassModel>(
            R.layout.adapter_teacher_online_view_class,
            callbacks,
            viewModel.onlineClassListResponse,
            viewLifecycleOwner,
            binding.root,
            { },
            viewModel.isNoDataStudent,
            viewModel.totalPageStudent,
            viewModel.isPageLoadingStudent,
            viewModel.isDataLoadingStudent,
            viewModel.currentPageStudent
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

    private fun goToFilter() {
        val filterFragment = OnlineClassTeacherViewClassFilter(object : OnlineClassTeacherViewClassFilter.Filter {
            override fun onDone(
                filterClicked: Boolean
            ) {
                viewModel.filterClicked.set(filterClicked)
                if(filterClicked)
                    viewModel.refreshPage()
                else
                    viewModel.clear()
            }
        })

        filterFragment.show(
            (context as FragmentActivity).supportFragmentManager,
            filterFragment.tag
        )
    }

    private fun getInitialData() {
        if (NetworkCheck.isInternetAvailable(requireContext())) {
            viewModel.getAcademicYear()
        } else {
            if(::binding.isInitialized){
                binding.root.snackBar(getString(R.string.check_internet), getString(R.string.retry), true) {
                    getInitialData()
                }
            }
        }
    }



    companion object {

        const val modelConstant = "model"
        @JvmStatic
        fun newInstance() = OnlineClassTeacherViewClassFragment()
    }

    override fun onResume() {
        super.onResume()
        setToolBarTitle(getString(R.string.menu_teacher_view_class))
    }
}