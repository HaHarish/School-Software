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
import com.newletseduvate.databinding.FragmentBlogStudentReviewedBinding
import com.newletseduvate.model.blog.StudentBlogModel
import com.newletseduvate.utils.extensions.setUpRecyclerView
import com.newletseduvate.viewmodels.BlogStudentViewModel

class BlogStudentReviewedFragment : BaseFragment(R.layout.fragment_blog_student_reviewed) {

    private lateinit var binding: FragmentBlogStudentReviewedBinding
    private val viewModel by lazy { getViewModel<BlogStudentViewModel>(requireActivity()) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBlogStudentReviewedBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewModel = viewModel

        val callbacks = ArrayList<CallBackModel<StudentBlogModel>>()
        callbacks.add(CallBackModel(R.id.root) { model, _ ->
            val bundle = Bundle()
            bundle.putSerializable("model", model)
            bundle.putInt("type", 2)
            findNavController().navigate(R.id.nav_student_blog_details, bundle)
        })

        binding.rvList.setUpRecyclerView<AdapterBlogStudentBinding, StudentBlogModel>(
            R.layout.adapter_blog_student,
            callbacks,
            viewModel.reviewedBlogListResponse,
            viewLifecycleOwner,
            binding.root,
            { viewModel.getReviewedBlogListFromApi() },
            viewModel.isNoDataReviewed,
            viewModel.totalPageReviewed,
            viewModel.isPageLoadingReviewed,
            viewModel.isDataLoadingReviewed,
            viewModel.currentPageReviewed
        ) {}

    }

    companion object {

        @JvmStatic
        fun newInstance() = BlogStudentReviewedFragment()
    }
}