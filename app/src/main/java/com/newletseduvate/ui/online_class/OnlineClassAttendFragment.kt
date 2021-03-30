package com.newletseduvate.ui.online_class

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.navigation.fragment.findNavController
import com.newletseduvate.R
import com.newletseduvate.adapter.dynamic_recycler_adapter.CallBackModel
import com.newletseduvate.base.BaseActivity
import com.newletseduvate.base.BaseFragment
import com.newletseduvate.databinding.AdapterOnlineClassAttendBinding
import com.newletseduvate.databinding.FragmentOnlineClassAttendBinding
import com.newletseduvate.model.BottomSheetItem
import com.newletseduvate.model.online_class.ClassTypeModel
import com.newletseduvate.model.online_class.StudentOnlineClassModel
import com.newletseduvate.ui.bottom_sheets.SingleSelectionBottomSheetFragment
import com.newletseduvate.utils.Resource
import com.newletseduvate.utils.constants.Constants
import com.newletseduvate.utils.extensions.*
import com.newletseduvate.utils.rx_component_observables.RxButtonObservable
import com.newletseduvate.viewmodels.OnlineClassAttendViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class OnlineClassAttendFragment : BaseFragment(R.layout.fragment_online_class_attend),
        SingleSelectionBottomSheetFragment.OnChooseReasonListener,
        View.OnClickListener {

    private lateinit var binding: FragmentOnlineClassAttendBinding
    private val viewModel by lazy { getViewModel<OnlineClassAttendViewModel>(requireActivity()) }

    private var isViewCoursePlan = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentOnlineClassAttendBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewModel = viewModel

        val classTypeList = ArrayList<ClassTypeModel>()
        classTypeList.add(ClassTypeModel(0, "Compulsory Class"))
        classTypeList.add(ClassTypeModel(1, "Optional Class"))
        classTypeList.add(ClassTypeModel(2, "Special Class"))
        classTypeList.add(ClassTypeModel(3, "Parent Class"))

        viewModel.classTypeList.value = Resource.success(classTypeList)

        setClickListeners()
        initTextForFilters()

        /*CompositeDisposable().add(
            RxButtonObservable.fromView(binding.ivFilter)
                .debounce(Constants.FILTER_DEBOUNCE_TIME, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    goToFilter()
                }, {

                })
        )*/

        val callbacks = ArrayList<CallBackModel<StudentOnlineClassModel>>()
        callbacks.add(CallBackModel(R.id.btn_view_more) { model, position ->
            val bundle = Bundle()
            bundle.putSerializable(modelConstant, model)
            bundle.putBoolean("isViewCoursePlan", isViewCoursePlan)
            bundle.putInt("COURSE_ID",model.onlineClass.courseId)
            bundle.putString("CURRENT_SERVER_TIME", viewModel.onlineClassResponse.value?.data?.currentServerTime)
            bundle.putString("CLASS_START_TIME",viewModel.onlineClassListResponse.value?.data?.
                                                get(position)?.onlineClass?.startTime)
            findNavController().navigate(R.id.nav_attend_online_class_details, bundle)
        })

        binding.rvList.setUpRecyclerView<AdapterOnlineClassAttendBinding, StudentOnlineClassModel>(
            R.layout.adapter_online_class_attend,
            callbacks,
            viewModel.onlineClassListResponse,
            viewLifecycleOwner,
            binding.root,
            { viewModel.getStudentOnlineClass() },
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

    private fun setClickListeners() {
        binding.etClassType.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        when (view?.id) {

            binding.etClassType.id -> {

                if (viewModel.classTypeList.value!!.data != null &&
                        viewModel.classTypeList.value!!.data!!.size > 0
                ) {
                    viewModel.classTypeList.value!!.data!!.getBottomSheetList()
                            .showSingleSelectionBottomSheetWithArrayList(
                                    childFragmentManager,
                                    ClassTypeBottomSheetID,
                                    viewModel.classTypeId.get()
                            )
                } else binding.root.snackBar(resources.getString(R.string.please_wait))

            }
        }
    }

    private fun initTextForFilters() {
        viewModel.classTypeList.value?.data.let {
            onChooseSingleItem(
                    ClassTypeBottomSheetID,
                    viewModel.classTypeList.value?.data.getBottomSheetItem(viewModel.classTypeId.get())
            )
        }
    }

    private val ClassTypeBottomSheetID = 0

    override fun onChooseSingleItem(BOTTOM_SHEET_ID: Int, selectedItem: BottomSheetItem) {
        when (BOTTOM_SHEET_ID) {

            ClassTypeBottomSheetID -> {
                if (selectedItem.id != null) {

                    when(selectedItem.id){
                        0 -> isViewCoursePlan = false
                        1 -> isViewCoursePlan = true
                        2 -> isViewCoursePlan = true
                        3 -> isViewCoursePlan = true
                    }

                    binding.etClassType.setText(selectedItem.name)
                    viewModel.classTypeId.set(selectedItem.id)
                    viewModel.refreshPage()
                }
            }

        }
    }

    private fun goToFilter() {
        val filterFragment =
            OnlineClassAttendFilterFragment(object : OnlineClassAttendFilterFragment.Filter {
                override fun onDone(
                    filterClicked: Boolean
                ) {
                    viewModel.filterClicked.set(filterClicked)

                    Log.i("filter", "$filterClicked : ${viewModel.classTypeId.get()}")
                    if (filterClicked)
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


    companion object {

        const val modelConstant = "model"

        @JvmStatic
        fun newInstance() = OnlineClassAttendFragment()
    }

    override fun onResume() {
        super.onResume()
        setToolBarTitle(getString(R.string.attend_online_class))
    }
}