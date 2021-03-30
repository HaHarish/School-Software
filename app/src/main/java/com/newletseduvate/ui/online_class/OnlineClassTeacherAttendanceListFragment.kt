package com.newletseduvate.ui.online_class

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.Observable
import com.newletseduvate.R
import com.newletseduvate.adapter.dynamic_recycler_adapter.CallBackModel
import com.newletseduvate.adapter.dynamic_recycler_adapter.RecyclerDynamicAdapter
import com.newletseduvate.base.BaseFragment
import com.newletseduvate.databinding.AdapterOnlineClassTeacherAttendanceListBinding
import com.newletseduvate.databinding.FragmentOnlineClassTeacherAttendanceListBinding
import com.newletseduvate.model.online_class.OnlineClassTeacherAttendanceListModel
import com.newletseduvate.model.online_class.TeacherViewClassModel
import com.newletseduvate.utils.extensions.datePickerDialog
import com.newletseduvate.utils.extensions.setUpRecyclerView
import com.newletseduvate.viewmodels.OnlineClassTeacherAttendanceViewModel
import java.util.*
import kotlin.collections.ArrayList

class OnlineClassTeacherAttendanceListFragment : BaseFragment(R.layout.fragment_online_class_teacher_attendance_list)  {

    private lateinit var binding: FragmentOnlineClassTeacherAttendanceListBinding
    private val viewModel by lazy { getViewModel<OnlineClassTeacherAttendanceViewModel>(this) }

    lateinit var adapter: RecyclerDynamicAdapter<AdapterOnlineClassTeacherAttendanceListBinding, OnlineClassTeacherAttendanceListModel>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentOnlineClassTeacherAttendanceListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewModel = viewModel

        val callbacks = ArrayList<CallBackModel<OnlineClassTeacherAttendanceListModel>>()
        callbacks.add(CallBackModel(R.id.cb_attendance){model, position ->
            model.isAttended.set(!model.isAttended.get())
            viewModel.putMarkAttendanceTeacher(model.isAttended.get(),
                model.id ?:-1
            )
        })

        viewModel.onlineClassModel =
            requireArguments().getSerializable(OnlineClassAttendFragment.modelConstant) as TeacherViewClassModel

        adapter = binding.rvList.setUpRecyclerView(
            R.layout.adapter_online_class_teacher_attendance_list,
            callbacks,
            viewModel.generalDiaryListResponse,
            viewLifecycleOwner,
            binding.root,
            { viewModel.getAttendanceList() },
            viewModel.isNoDataDaily,
            viewModel.totalPageDaily,
            viewModel.isPageLoadingDaily,
            viewModel.isDataLoadingDaily,
            viewModel.currentPageDaily
        ) {}

        binding.etDate.setOnClickListener {
            view.datePickerDialog(viewModel.date)
        }

        viewModel.date.addOnPropertyChangedCallback(object :
            Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                viewModel.refreshPage()
            }
        })
    }
    companion object {

        @JvmStatic
        fun newInstance() = OnlineClassTeacherAttendanceListFragment()
    }
}