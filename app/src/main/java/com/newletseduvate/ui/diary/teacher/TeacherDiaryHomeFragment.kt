package com.newletseduvate.ui.diary.teacher

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayout
import com.newletseduvate.R
import com.newletseduvate.adapter.MyViewPagerAdapter
import com.newletseduvate.base.BaseFragment
import com.newletseduvate.databinding.FragmentTeacherDiaryHomeBinding
import com.newletseduvate.utils.constants.Constants
import com.newletseduvate.viewmodels.TeacherDiaryCreateFilterViewModel
import com.newletseduvate.utils.rx_component_observables.RxButtonObservable
import com.newletseduvate.viewmodels.DiaryTeacherViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class TeacherDiaryHomeFragment: BaseFragment(R.layout.fragment_teacher_diary_home) {

    private lateinit var binding: FragmentTeacherDiaryHomeBinding
    private val viewModelFilter by lazy { getViewModel<DiaryTeacherViewModel>(requireActivity()) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModelFilter.clear()
    }
    private val viewModel by lazy { getViewModel<TeacherDiaryCreateFilterViewModel>(requireActivity()) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTeacherDiaryHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.moduleId = requireArguments().getInt(Constants.MODULE_ID)

        viewModel.isGeneralClicked.set(false)
        viewModel.isDailyClicked.set(false)
        viewModel.isFilterClicked.set(false)

        binding.viewModel = viewModelFilter

        viewModelFilter.moduleId.set(requireArguments().getInt(Constants.MODULE_ID))
        initViewPager()

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

        viewModelFilter.mediatorIsPageLoading.observe(viewLifecycleOwner, {
            it?.let {
                if(it)
                    displayProgress()
                else
                    dismissProgress()
            }
        })

        // Button click for Create Daily Diary
        binding.buttonTeacherDiaryCreateDailyDiary.setOnClickListener {
            viewModel.isGeneralClicked.set(false)
            viewModel.isDailyClicked.set(true)
            viewModel.isFilterClicked.set(false)
            findNavController().navigate(R.id.nav_teacher_diary_create_filter)
        }

        // Button click for Create General Diary
        binding.buttonTeacherDiaryCreateGeneralDiary.setOnClickListener {
            viewModel.isGeneralClicked.set(true)
            viewModel.isDailyClicked.set(false)
            viewModel.isFilterClicked.set(false)
            findNavController().navigate(R.id.nav_teacher_diary_create_filter)
        }
    }


    private fun goToFilter() {
        val filterFragment = DiaryTeacherFilter()
        filterFragment.setOnDone(object : DiaryTeacherFilter.Filter {
            override fun onDone(
                filterClicked: Boolean
            ) {
                viewModelFilter.filterClicked.set(filterClicked)
                if(filterClicked)
                    viewModelFilter.refreshPage()
                else
                    viewModelFilter.clear()
            }
        })

        // Button click for Filter
        binding.ivFilter.setOnClickListener {
            viewModel.isGeneralClicked.set(false)
            viewModel.isDailyClicked.set(false)
            viewModel.isFilterClicked.set(true)
            findNavController().navigate(R.id.nav_teacher_diary_create_filter)
        }
        filterFragment.show(
            (context as FragmentActivity).supportFragmentManager,
            filterFragment.tag
        )
    }

    private fun initViewPager() {
        val adapter = MyViewPagerAdapter(childFragmentManager)
        adapter.addFragment(TeacherDiaryDailyFragment.newInstance(), resources.getString(R.string.daily_diary))
        adapter.addFragment(TeacherDiaryGeneralFragment.newInstance(), resources.getString(R.string.general_diary))

        binding.tabLayoutStudentDiary.tabMode = TabLayout.MODE_SCROLLABLE
        binding.viewPagerStudentDiary.adapter = adapter
        binding.tabLayoutStudentDiary.setupWithViewPager(binding.viewPagerStudentDiary)
        Constants.allotEachTabWithEqualWidth(binding.tabLayoutStudentDiary)
        binding.viewPagerStudentDiary.offscreenPageLimit = 2
    }

    override fun onResume() {
        super.onResume()
        setToolBarTitle(resources.getString(R.string.teacher_diary))
    }
}