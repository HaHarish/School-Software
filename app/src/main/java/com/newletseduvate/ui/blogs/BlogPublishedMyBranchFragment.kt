package com.newletseduvate.ui.blogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.newletseduvate.R
import com.newletseduvate.adapter.BlogPublishedAdapter
import com.newletseduvate.base.BaseFragment
import com.newletseduvate.databinding.FragmentBlogPublishedMyBranchBinding
import com.newletseduvate.model.BottomSheetIconItem
import com.newletseduvate.utils.Status
import com.newletseduvate.model.blog.StudentBlogModel
import com.newletseduvate.ui.bottom_sheets.SingleSelectionIconBottomSheetFragment
import com.newletseduvate.utils.EndlessScrollListener
import com.newletseduvate.utils.NetworkCheck
import com.newletseduvate.utils.extensions.snackBar
import com.newletseduvate.viewmodels.BlogPublishedViewModel

class BlogPublishedMyBranchFragment : BaseFragment(R.layout.fragment_blog_published_my_branch) {

    lateinit var binding: FragmentBlogPublishedMyBranchBinding
    private val viewModel by lazy { getViewModel<BlogPublishedViewModel>(requireActivity()) }
    private lateinit var adapter: BlogPublishedAdapter
    private lateinit var linearLayoutManager: LinearLayoutManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBlogPublishedMyBranchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewModel = viewModel

        initRecyclerView()
        initDataObserver()
        getMyBranchBlogListFromApi()

        binding.rvList.addOnScrollListener(object :
            EndlessScrollListener(linearLayoutManager) {

            override fun loadMoreItems() {
                viewModel.isDataLoadingMyBranch.value = true
                viewModel.currentPageMyBranch.value = viewModel.currentPageMyBranch.value?.plus(1)
                getMyBranchBlogListFromApi()
            }

            override fun getTotalPageCount(): Int {
                return viewModel.totalPageMyBranch.get()
            }

            override fun isLastPage(): Boolean {
                val totalPage = getTotalPageCount()
                return viewModel.currentPageMyBranch.value!! >= totalPage
            }

            override fun isLoading(): Boolean {
                return viewModel.isDataLoadingMyBranch.value!!
            }
        })

    }

    private fun initDataObserver() {
        viewModel.myBranchBlogListResponse.observe(viewLifecycleOwner, {
            viewModel.isDataLoadingMyBranch.value = false

            it?.let {

                when (it.status) {
                    Status.Success -> {
                        if (viewModel.currentPageMyBranch.value == 1)
                            adapter.clearList()
                        it.data?.let { it1 -> adapter.submitList(it1) }
                        if (adapter.itemCount == 0)
                            viewModel.isNoDataMyBranch.set(true)
                        else
                            viewModel.isNoDataMyBranch.set(false)
                    }
                    else -> {
                        binding.root.snackBar(resources.getString(R.string.something_went_wrong)){}
                    }
                }
            }

            viewModel.isPageLoadingMyBranch.value = false
        })
    }

    private fun initRecyclerView() {
        linearLayoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
        binding.rvList.layoutManager = linearLayoutManager
        adapter = BlogPublishedAdapter(object : BlogPublishedAdapter.OnItemClickListener{
            override fun selectItem(model: StudentBlogModel, position: Int) {
                val bundle = Bundle()
                bundle.putSerializable("model", model)
                findNavController().navigate(R.id.nav_published_blog_details, bundle)
            }

            override fun showOptions(model: StudentBlogModel, position: Int) {
                showOptionMenu()
            }

        })
        binding.rvList.adapter = adapter
        binding.rvList.setHasFixedSize(true)
    }

    private fun getMyBranchBlogListFromApi() {
        if (NetworkCheck.isInternetAvailable(requireContext())) {
            if (viewModel.currentPageMyBranch.value == 1)
                adapter.clearList()
            viewModel.getMyBranchBlogListFromApi()
        } else {
            if(::binding.isInitialized){
                binding.root.snackBar(getString(R.string.check_internet), getString(R.string.retry), true) {
                    getMyBranchBlogListFromApi()
                }
            }
        }
    }

    private fun showOptionMenu(){
        val optionsArray = ArrayList<BottomSheetIconItem>()
        optionsArray.add(
            BottomSheetIconItem(
                0,
                R.drawable.ic_edit,
                resources.getString(R.string.read_more)
            )
        )
        val bottomSheet = SingleSelectionIconBottomSheetFragment(
            0,
            optionsArray,
            "",
            object : SingleSelectionIconBottomSheetFragment.OnChooseReasonListener {

                override fun onChooseSingleItem(
                    BOTTOM_SHEET_ID: Int,
                    selectedItem: BottomSheetIconItem,
                    position: Int
                ) {
                    when (BOTTOM_SHEET_ID) {
                        0 -> { }
                        1 -> { }
                    }
                }

            })
        bottomSheet.show(
            childFragmentManager,
            bottomSheet.tag
        )
    }

    companion object {
        @JvmStatic
        fun newInstance() = BlogPublishedMyBranchFragment()
    }
}