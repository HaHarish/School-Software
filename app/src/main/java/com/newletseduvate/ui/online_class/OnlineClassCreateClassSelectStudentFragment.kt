package com.newletseduvate.ui.online_class

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableInt
import androidx.navigation.fragment.findNavController
import com.newletseduvate.R
import com.newletseduvate.adapter.dynamic_recycler_adapter.CallBackModel
import com.newletseduvate.adapter.dynamic_recycler_adapter.RecyclerDynamicAdapter
import com.newletseduvate.base.BaseFragment
import com.newletseduvate.databinding.AdapterOnlineClassCreateClassStudentFilterBinding
import com.newletseduvate.databinding.FragmentCrateClassSelectedStudentBinding
import com.newletseduvate.model.online_class.CreateClassRequest
import com.newletseduvate.model.online_class.CreateClassStudentFilterModel
import com.newletseduvate.utils.Resource
import com.newletseduvate.utils.extensions.hideKeyboard
import com.newletseduvate.utils.extensions.setUpRecyclerView
import com.newletseduvate.utils.extensions.snackBar
import com.newletseduvate.viewmodels.OnlineClassCreateClassViewModel
import kotlin.collections.ArrayList

class OnlineClassCreateClassSelectStudentFragment :
    BaseFragment(R.layout.fragment_crate_class_selected_student) {

    private lateinit var binding: FragmentCrateClassSelectedStudentBinding
    private val viewModel by lazy { getViewModel<OnlineClassCreateClassViewModel>(this) }

    lateinit var adapter: RecyclerDynamicAdapter<AdapterOnlineClassCreateClassStudentFilterBinding, CreateClassStudentFilterModel>

    lateinit var createClassModel: CreateClassRequest

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCrateClassSelectedStudentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewModel = viewModel

        val callbacks = ArrayList<CallBackModel<CreateClassStudentFilterModel>>()
        callbacks.add(CallBackModel(R.id.cb_attendance) { model, position ->
        })

        createClassModel =
            requireArguments().getSerializable(OnlineClassAttendFragment.modelConstant) as CreateClassRequest

        val branchId = requireArguments().getInt("branchId", -1)
        val gradeId = requireArguments().getInt("gradeId", -1)

        adapter = binding.rvList.setUpRecyclerView(
            R.layout.adapter_online_class_create_class_student_filter,
            callbacks,
            viewModel.studentList,
            viewLifecycleOwner,
            binding.root,
            {
                if(createClassModel.classType == 0){
                    viewModel.getStudentFilter(
                        createClassModel.sectionMappingIds?.toInt(),
                        createClassModel.subjectId?.toInt(),
                        null,
                        null
                    )
                }else{
                    viewModel.getStudentFilter(
                        null,
                        null,
                        branchId,
                        gradeId
                    )
                }

            },
            viewModel.isNoDataStudent,
            ObservableInt(0),
            viewModel.isPageLoadingStudent,
            viewModel.isDataLoadingStudent,
            viewModel.currentPageStudent
        ) {}

        binding.btnDeSelectAll.setOnClickListener {
            val tempArray = ArrayList<CreateClassStudentFilterModel>()
            viewModel.studentList.value?.data?.forEach {
                it.isSelected = ObservableBoolean(false)
                tempArray.add(it)
            }

            viewModel.studentList.value = Resource.success(tempArray)
        }

        binding.btnSelectAll.setOnClickListener {
            val tempArray = ArrayList<CreateClassStudentFilterModel>()
            viewModel.studentList.value?.data?.forEach {
                it.isSelected = ObservableBoolean(true)
                tempArray.add(it)
            }

            viewModel.studentList.value = Resource.success(tempArray)
        }

        binding.btnCreateClass.setOnClickListener {
            view.hideKeyboard()

            val tempArray = ArrayList<Int>()

            viewModel.studentList.value?.data?.forEach {
                if(it.isSelected.get())
                    tempArray.add(it.id!!)
            }
            createClassModel.studentIds = tempArray

            if(createClassModel.classType == 0){
                viewModel.postCreateClassOnlineRecurring(
                    createClassModel
                )
            }else{
                viewModel.postCreateClassOnlineErpClass(
                    createClassModel
                )
            }
        }
        
        viewModel.createClassSuccessful.observe(viewLifecycleOwner, {
            if(it == "SUCCESS"){
                binding.root.snackBar("Class created")
                viewModel.createClassSuccessful.value = ""
                findNavController().popBackStack()
                findNavController().popBackStack()
            }else if(it.isNotEmpty()){
                binding.root.snackBar(it)
            }
        })

    }

    companion object {

        @JvmStatic
        fun newInstance() = OnlineClassCreateClassSelectStudentFragment()
    }
}