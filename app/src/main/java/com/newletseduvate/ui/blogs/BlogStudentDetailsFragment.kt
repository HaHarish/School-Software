package com.newletseduvate.ui.blogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.newletseduvate.R
import com.newletseduvate.base.BaseFragment
import com.newletseduvate.databinding.FragmentBlogStudentDetailsBinding
import com.newletseduvate.model.blog.StudentBlogModel
import com.newletseduvate.viewmodels.BlogStudentDetailsViewModel

class BlogStudentDetailsFragment : BaseFragment(R.layout.fragment_blog_student_details){

    private lateinit var binding: FragmentBlogStudentDetailsBinding
    private val viewModel by lazy { getViewModel<BlogStudentDetailsViewModel>(requireActivity()) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBlogStudentDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val studentModel = requireArguments().get("model") as StudentBlogModel
        val type = requireArguments().getInt("type", 1)

        binding.model = studentModel

        binding.ibDelete.setOnClickListener {
            viewModel.putDeleteBlog(studentModel.id)
            findNavController().popBackStack()
        }

        binding.ibRestore.setOnClickListener {
            viewModel.putRestoreBlog(studentModel.id)
            findNavController().popBackStack()
        }

        when(type){

            1 -> {
                binding.ibDelete.visibility = View.VISIBLE
                binding.ibRestore.visibility = View.GONE
            }

            2 -> {
                binding.ibDelete.visibility = View.VISIBLE
                binding.ibRestore.visibility = View.GONE
            }

            3 -> {
                binding.ibDelete.visibility = View.VISIBLE
                binding.ibRestore.visibility = View.GONE
            }

            4 -> {
                binding.ibDelete.visibility = View.GONE
                binding.ibRestore.visibility = View.VISIBLE
            }

        }
    }


    companion object {
        @JvmStatic
        fun newInstance() = BlogStudentDetailsFragment()
    }
}