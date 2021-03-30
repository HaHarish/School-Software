package com.newletseduvate.ui.diary.student

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import com.google.android.material.tabs.TabLayout
import com.newletseduvate.R
import com.newletseduvate.adapter.MyViewPagerAdapter
import com.newletseduvate.base.BaseFragment
import com.newletseduvate.databinding.FragmentStudentDiaryViewPagerBinding
import com.newletseduvate.utils.constants.Constants
import com.newletseduvate.utils.constants.Constants.FILTER_DEBOUNCE_TIME
import com.newletseduvate.utils.constants.Constants.MODULE_ID
import com.newletseduvate.utils.rx_component_observables.RxButtonObservable
import com.newletseduvate.viewmodels.DiaryStudentViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class DiaryStudentViewPager : BaseFragment(R.layout.fragment_student_diary_view_pager) {

    private lateinit var binding: FragmentStudentDiaryViewPagerBinding
    private val viewModel by lazy { getViewModel<DiaryStudentViewModel>(requireActivity()) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.clear()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentStudentDiaryViewPagerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel

        viewModel.moduleId.set(requireArguments().getInt(MODULE_ID))
        initViewPager()

        CompositeDisposable().add(
            RxButtonObservable.fromView(binding.ivFilter)
                .debounce(FILTER_DEBOUNCE_TIME, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    goToFilter()
                },{

                })
        )

        viewModel.mediatorIsPageLoading.observe(viewLifecycleOwner, {
            it?.let {
                if(it)
                    displayProgress()
                else
                    dismissProgress()
            }
        })
    }

    private fun goToFilter() {
        val filterFragment = DiaryStudentFilter()
        filterFragment.setOnDone(object : DiaryStudentFilter.Filter {
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

    private fun initViewPager() {
        val adapter = MyViewPagerAdapter(childFragmentManager)
        adapter.addFragment(DiaryDailyFragment.newInstance(), resources.getString(R.string.daily_diary))
        adapter.addFragment(DiaryGeneralFragment.newInstance(), resources.getString(R.string.general_diary))

        binding.tabLayoutStudentDiary.tabMode = TabLayout.MODE_SCROLLABLE
        binding.viewPagerStudentDiary.adapter = adapter
        binding.tabLayoutStudentDiary.setupWithViewPager(binding.viewPagerStudentDiary)
        Constants.allotEachTabWithEqualWidth(binding.tabLayoutStudentDiary)
        binding.viewPagerStudentDiary.offscreenPageLimit = 2
    }

    companion object {

        @JvmStatic
        fun newInstance() = DiaryStudentViewPager()
    }

    override fun onResume() {
        super.onResume()
        setToolBarTitle(getString(R.string.menu_student_diary))
    }
}