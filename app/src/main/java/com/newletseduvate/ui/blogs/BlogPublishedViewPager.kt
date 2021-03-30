package com.newletseduvate.ui.blogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import com.google.android.material.tabs.TabLayout
import com.newletseduvate.R
import com.newletseduvate.adapter.MyViewPagerAdapter
import com.newletseduvate.base.BaseFragment
import com.newletseduvate.databinding.FragmentBlogPublishedViewPagerBinding
import com.newletseduvate.ui.diary.student.DiaryStudentFilter
import com.newletseduvate.utils.constants.Constants
import com.newletseduvate.utils.rx_component_observables.RxButtonObservable
import com.newletseduvate.viewmodels.BlogPublishedViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class BlogPublishedViewPager : BaseFragment(R.layout.fragment_blog_published_view_pager) {

    private lateinit var binding: FragmentBlogPublishedViewPagerBinding
    private val viewModel by lazy { getViewModel<BlogPublishedViewModel>(requireActivity()) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBlogPublishedViewPagerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel

        viewModel.moduleId.set(requireArguments().getInt(Constants.MODULE_ID))
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
        adapter.addFragment(
            BlogPublishedOrchidsFragment.newInstance(),
            resources.getString(R.string.orchids)
        )
        adapter.addFragment(
            BlogPublishedMyBranchFragment.newInstance(),
            resources.getString(R.string.my_branch)
        )
        adapter.addFragment(
            BlogPublishedMyGradeFragment.newInstance(),
            resources.getString(R.string.my_grade)
        )
        adapter.addFragment(
            BlogPublishedMySectionFragment.newInstance(),
            resources.getString(R.string.my_section)
        )

        binding.tabLayoutBlogPublished.tabMode = TabLayout.MODE_SCROLLABLE
        binding.viewPagerBlogPublished.adapter = adapter
        binding.tabLayoutBlogPublished.setupWithViewPager(binding.viewPagerBlogPublished)
        Constants.allotEachTabWithEqualWidth(binding.tabLayoutBlogPublished)
        binding.viewPagerBlogPublished.offscreenPageLimit = 4
    }

    companion object {

        @JvmStatic
        fun newInstance() = BlogPublishedViewPager()
    }
}