package com.newletseduvate.viewmodels

import androidx.lifecycle.ViewModel
import com.newletseduvate.repository.BlogRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class BlogStudentDetailsViewModel @Inject constructor(private val repository: BlogRepository) :
    ViewModel() {


    fun putDeleteBlog(
        blogId: Int
    ) {

        GlobalScope.launch {
            val response = repository.putDeleteBlog(blogId)

            withContext(Dispatchers.Main) {
                when {
                    response.isSuccessful -> {
                        if (response.body() != null) { }
                    }
                    else -> {}

                }
            }
        }

    }

    fun putRestoreBlog(
        blogId: Int
    ) {

        GlobalScope.launch {
            val response = repository.putRestoreBlog(blogId)

            withContext(Dispatchers.Main) {
                when {
                    response.isSuccessful -> {
                        if (response.body() != null) { }
                    }
                    else -> {}

                }
            }
        }

    }
}