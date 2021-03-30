package com.newletseduvate.ui.blogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.newletseduvate.R
import com.newletseduvate.adapter.dynamic_recycler_adapter.CallBackModel
import com.newletseduvate.base.BaseFragment
import com.newletseduvate.databinding.AdapterBlogStudentBinding
import com.newletseduvate.databinding.FragmentBlogStudentPendingReviewBinding
import com.newletseduvate.model.blog.StudentBlogModel
import com.newletseduvate.utils.extensions.setUpRecyclerView
import com.newletseduvate.viewmodels.BlogStudentViewModel

class BlogStudentPendingReviewFragment : BaseFragment(R.layout.fragment_blog_student_pending_review) {

    private lateinit var binding: FragmentBlogStudentPendingReviewBinding
    private val viewModel by lazy { getViewModel<BlogStudentViewModel>(requireActivity()) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBlogStudentPendingReviewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewModel = viewModel

        val callbacks = ArrayList<CallBackModel<StudentBlogModel>>()
        callbacks.add(CallBackModel(R.id.root) { model, _ ->
            val bundle = Bundle()
            bundle.putSerializable("model", model)
            bundle.putInt("type", 1)
            findNavController().navigate(R.id.nav_student_blog_details, bundle)
        })

        binding.rvList.setUpRecyclerView<AdapterBlogStudentBinding, StudentBlogModel>(
            R.layout.adapter_blog_student,
            callbacks,
            viewModel.pendingReviewBlogListResponse,
            viewLifecycleOwner,
            binding.root,
            { viewModel.getPendingReviewBlogListFromApi() },
            viewModel.isNoDataPendingReview,
            viewModel.totalPagePendingReview,
            viewModel.isPageLoadingPendingReview,
            viewModel.isDataLoadingPendingReview,
            viewModel.currentPagePendingReview
        ) {}

    }

    companion object {

        @JvmStatic
        fun newInstance() = BlogStudentPendingReviewFragment()
    }
}