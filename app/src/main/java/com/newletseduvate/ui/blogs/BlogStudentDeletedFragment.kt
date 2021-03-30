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
import com.newletseduvate.databinding.FragmentBlogStudentDeletedBinding
import com.newletseduvate.model.blog.StudentBlogModel
import com.newletseduvate.utils.extensions.setUpRecyclerView
import com.newletseduvate.viewmodels.BlogStudentViewModel

class BlogStudentDeletedFragment : BaseFragment(R.layout.fragment_blog_student_deleted) {

    private lateinit var binding: FragmentBlogStudentDeletedBinding
    private val viewModel by lazy { getViewModel<BlogStudentViewModel>(requireActivity()) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBlogStudentDeletedBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewModel = viewModel

        val callbacks = ArrayList<CallBackModel<StudentBlogModel>>()
        callbacks.add(CallBackModel(R.id.root) { model, _ ->
            val bundle = Bundle()
            bundle.putSerializable("model", model)
            bundle.putInt("type", 4)
            findNavController().navigate(R.id.nav_student_blog_details, bundle)
        })

        binding.rvList.setUpRecyclerView<AdapterBlogStudentBinding, StudentBlogModel>(
            R.layout.adapter_blog_student,
            callbacks,
            viewModel.deletedBlogListResponse,
            viewLifecycleOwner,
            binding.root,
            { viewModel.getDeletedBlogListFromApi() },
            viewModel.isNoDataDeleted,
            viewModel.totalPageDeleted,
            viewModel.isPageLoadingDeleted,
            viewModel.isDataLoadingDeleted,
            viewModel.currentPageDeleted
        ) {}

    }

    companion object {

        @JvmStatic
        fun newInstance() = BlogStudentDeletedFragment()
    }
}