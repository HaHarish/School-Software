package com.newletseduvate.ui.blogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.tabs.TabLayout
import com.newletseduvate.R
import com.newletseduvate.adapter.MyViewPagerAdapter
import com.newletseduvate.base.BaseFragment
import com.newletseduvate.databinding.FragmentBlogStudentViewPagerBinding
import com.newletseduvate.utils.constants.Constants
import com.newletseduvate.utils.extensions.CustomProgressBar
import com.newletseduvate.viewmodels.BlogStudentViewModel

class BlogStudentViewPager : BaseFragment(R.layout.fragment_blog_student_view_pager),
    View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {

    private lateinit var binding: FragmentBlogStudentViewPagerBinding
    private val viewModel by lazy { getViewModel<BlogStudentViewModel>(requireActivity()) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBlogStudentViewPagerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewModel = viewModel

        viewModel.moduleId.set(requireArguments().getInt(Constants.MODULE_ID))

        initViewPager()
        setClickListeners()

        viewModel.mediatorIsPageLoading.observe(viewLifecycleOwner, {
            it?.let {
                if(it)
                    displayProgress()
                else {
                    viewModel.swipeRefreshLayout.set(false)
                    dismissProgress()
                }
            }
        })
    }

    private fun setClickListeners() {
        binding.btnPublishedBlog.setOnClickListener(this)
        binding.btnCreateNew.setOnClickListener(this)
        binding.swipeRefreshLayout.setOnRefreshListener(this)
    }

    private fun initViewPager() {
        val adapter = MyViewPagerAdapter(childFragmentManager)
        adapter.addFragment(
            BlogStudentPendingReviewFragment.newInstance(),
            resources.getString(R.string.pending_review)
        )
        adapter.addFragment(
            BlogStudentReviewedFragment.newInstance(),
            resources.getString(R.string.reviewed)
        )
        adapter.addFragment(
            BlogStudentDraftedFragment.newInstance(),
            resources.getString(R.string.drafted)
        )
        adapter.addFragment(
            BlogStudentDeletedFragment.newInstance(),
            resources.getString(R.string.deleted)
        )

        binding.tabLayoutStudentBlog.tabMode = TabLayout.MODE_SCROLLABLE
        binding.viewPagerStudentBlog.adapter = adapter
        binding.tabLayoutStudentBlog.setupWithViewPager(binding.viewPagerStudentBlog)
        Constants.allotEachTabWithEqualWidth(binding.tabLayoutStudentBlog)
        binding.viewPagerStudentBlog.offscreenPageLimit = 4
    }

    companion object {

        @JvmStatic
        fun newInstance() = BlogStudentViewPager()
    }

    override fun onResume() {
        super.onResume()
        setToolBarTitle(getString(R.string.menu_student_blog))
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            binding.btnPublishedBlog.id -> {
                findNavController().navigate(R.id.nav_student_blog_published, requireArguments())
            }
            binding.btnCreateNew.id -> {
                findNavController().navigate(R.id.nav_student_blog_create_new, requireArguments())
            }
        }
    }

    override fun onRefresh() {
        viewModel.refreshPage()
    }
}