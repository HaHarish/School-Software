package com.newletseduvate.ui.blogs

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.newletseduvate.R
import com.newletseduvate.adapter.dynamic_recycler_adapter.CallBackModel
import com.newletseduvate.adapter.dynamic_recycler_adapter.RecyclerDynamicAdapter
import com.newletseduvate.base.BaseFragment
import com.newletseduvate.databinding.AdapterBlogPublishedBinding
import com.newletseduvate.databinding.FragmentBlogPublishedOrchidsBinding
import com.newletseduvate.model.BottomSheetIconItem
import com.newletseduvate.model.blog.StudentBlogModel
import com.newletseduvate.ui.bottom_sheets.SingleSelectionIconBottomSheetFragment
import com.newletseduvate.utils.extensions.setUpRecyclerView
import com.newletseduvate.viewmodels.BlogPublishedViewModel

class BlogPublishedOrchidsFragment : BaseFragment(R.layout.fragment_blog_published_orchids) {

    lateinit var binding: FragmentBlogPublishedOrchidsBinding
    private val viewModel by lazy { getViewModel<BlogPublishedViewModel>(requireActivity()) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBlogPublishedOrchidsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewModel = viewModel

        val callbacks = ArrayList<CallBackModel<StudentBlogModel>>()
        callbacks.add(CallBackModel(R.id.root) { model, position ->
            val bundle = Bundle()
            bundle.putSerializable("model", model)
            findNavController().navigate(R.id.nav_published_blog_details, bundle)
        })

        binding.rvList.setUpRecyclerView<AdapterBlogPublishedBinding, StudentBlogModel>(
            R.layout.adapter_blog_published,
            callbacks,
            viewModel.orchidsBlogListResponse,
            viewLifecycleOwner,
            binding.root,
            { viewModel.getOrchidBlogListFromApi() },
            viewModel.isNoDataOrchids,
            viewModel.totalPageOrchids,
            viewModel.isPageLoadingOrchids,
            viewModel.isDataLoadingOrchids,
            viewModel.currentPageOrchids
        ) {}

    }

    companion object {
        @JvmStatic
        fun newInstance() = BlogPublishedOrchidsFragment()
    }
}