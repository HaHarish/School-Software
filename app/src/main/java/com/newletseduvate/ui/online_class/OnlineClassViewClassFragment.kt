package com.newletseduvate.ui.online_class

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import com.newletseduvate.R
import com.newletseduvate.adapter.dynamic_recycler_adapter.CallBackModel
import com.newletseduvate.base.BaseActivity
import com.newletseduvate.base.BaseFragment
import com.newletseduvate.databinding.AdapterOnlineClassAttendBinding
import com.newletseduvate.databinding.FragmentOnlineClassViewClassBinding
import com.newletseduvate.model.online_class.StudentOnlineClassModel
import com.newletseduvate.utils.constants.Constants
import com.newletseduvate.utils.extensions.setUpRecyclerView
import com.newletseduvate.utils.rx_component_observables.RxButtonObservable
import com.newletseduvate.viewmodels.OnlineClassViewClassViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class OnlineClassViewClassFragment : BaseFragment(R.layout.fragment_online_class_view_class) {
    private lateinit var binding: FragmentOnlineClassViewClassBinding
    private val viewModel by lazy { getViewModel<OnlineClassViewClassViewModel>(this) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentOnlineClassViewClassBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewModel = viewModel

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

        val callbacks = ArrayList<CallBackModel<StudentOnlineClassModel>>()
        callbacks.add(CallBackModel(R.id.root) { model, position ->
            Log.i("card", "clicked")
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
    }

    private fun goToFilter() {
        val filterFragment = OnlineClassViewClassFilter(object : OnlineClassViewClassFilter.Filter {
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

    companion object {
        @JvmStatic
        fun newInstance() = OnlineClassViewClassFragment()
    }

    override fun onResume() {
        super.onResume()
        (activity as BaseActivity).setToolbarTitle(
                getString(R.string.view_class),
                backIcon = true
        )
        (activity as BaseActivity).showHomeButton()
    }
}