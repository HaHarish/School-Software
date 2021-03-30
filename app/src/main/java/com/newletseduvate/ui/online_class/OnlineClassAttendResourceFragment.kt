package com.newletseduvate.ui.online_class

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ObservableInt
import androidx.lifecycle.MutableLiveData
import com.newletseduvate.R
import com.newletseduvate.adapter.dynamic_recycler_adapter.CallBackModel
import com.newletseduvate.base.BaseFragment
import com.newletseduvate.databinding.AdapterOnlineClassAttendResourceBinding
import com.newletseduvate.databinding.FragmentOnlineClassAttendResourceBinding
import com.newletseduvate.model.online_class.OnlineClassAttendResourceModel
import com.newletseduvate.model.online_class.OnlineClassStudentOcDetailsModifiedModel
import com.newletseduvate.utils.constants.Constants
import com.newletseduvate.utils.extensions.setUpRecyclerView
import com.newletseduvate.viewmodels.OnlineClassAttendResourceViewModel
import java.text.SimpleDateFormat


class OnlineClassAttendResourceFragment : BaseFragment(R.layout.fragment_online_class_attend_resource) {
    private lateinit var binding: FragmentOnlineClassAttendResourceBinding
    private val viewModel by lazy { getViewModel<OnlineClassAttendResourceViewModel>(this) }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        binding = FragmentOnlineClassAttendResourceBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewModel = viewModel

        val bundle = requireArguments()
        val onlineClassId = bundle.getString("ID", "")
        val classDateList = bundle.getSerializable("LIST") as ArrayList<OnlineClassStudentOcDetailsModifiedModel>

        val callbacks = ArrayList<CallBackModel<OnlineClassAttendResourceModel>>()

        binding.rvList.setUpRecyclerView<AdapterOnlineClassAttendResourceBinding, OnlineClassAttendResourceModel>(
            R.layout.adapter_online_class_attend_resource,
            callbacks,
            viewModel.onlineClassListResponse,
            viewLifecycleOwner,
            binding.root,
            {
                classDateList.forEach {
                    Log.i("TESTY","$onlineClassId ${it.date}")
                    val simpledDateFormat = SimpleDateFormat(Constants.DateFormat.DDMMYYYY_)
                    val dateFormat = SimpleDateFormat(Constants.DateFormat.YYYYMMDD)
                    val date = simpledDateFormat.format(dateFormat.parse(it.date))
                    viewModel.getStudentOnlineClassResourceFiles(onlineClassId, date)
                }

            },
            currentPageDaily = MutableLiveData(1),
            totalPageDaily = ObservableInt(1)
        ) {}
    }


    companion object {

        const val textNotAvailableConstant = "Not Available"
        @JvmStatic
        fun newInstance() =
                OnlineClassAttendResourceFragment()
    }
}