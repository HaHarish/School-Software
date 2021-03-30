package com.newletseduvate.ui.blogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.newletseduvate.R
import com.newletseduvate.base.BaseFragment
import com.newletseduvate.databinding.FragmentBlogPublishedDetailsBinding
import com.newletseduvate.model.blog.StudentBlogModel
import com.newletseduvate.viewmodels.BlogStudentDetailsViewModel

class BlogPublishedStudentDetailsFragment : BaseFragment(R.layout.fragment_blog_published_details){

    private lateinit var binding: FragmentBlogPublishedDetailsBinding
    private val viewModel by lazy { getViewModel<BlogStudentDetailsViewModel>(requireActivity()) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBlogPublishedDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val studentModel = requireArguments().get("model") as StudentBlogModel

        binding.model = studentModel
    }


    companion object {
        @JvmStatic
        fun newInstance() = BlogPublishedStudentDetailsFragment()
    }
}