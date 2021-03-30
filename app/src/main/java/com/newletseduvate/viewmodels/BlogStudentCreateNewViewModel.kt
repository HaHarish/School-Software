package com.newletseduvate.viewmodels

import androidx.databinding.ObservableInt
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.newletseduvate.model.blog.StudentBlogCreateResponse
import com.newletseduvate.model.blog.StudentBlogGenreModel
import com.newletseduvate.model.general.UploadFileModel
import com.newletseduvate.repository.BlogRepository
import com.newletseduvate.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class BlogStudentCreateNewViewModel @Inject constructor(private val repository: BlogRepository) :
    ViewModel() {

    var genreList = MutableLiveData<Resource<ArrayList<StudentBlogGenreModel>>>()
    var genreId = ObservableInt(-1)

    fun getStudentBlogGenre() {

        GlobalScope.launch {
            val response = repository.getStudentBlogGenre()

            withContext(Dispatchers.Main) {
                when {
                    response.isSuccessful -> {
                        if (response.body() != null) {
                            genreList.value =
                                Resource.success(response.body()!!.result)
                        }
                    }
                    else -> genreList.value = Resource.error(response.message())

                }
            }
        }

    }

    var postCreateBlogResponse = MutableLiveData<Resource<StudentBlogCreateResponse>>()

    fun postCreateBlog(
        status: String,
        title: String,
        content: String,
        wordCount: Int,
        file: UploadFileModel
    ) {

        GlobalScope.launch {
            val response = repository.postCreateBlog(status, title, content, wordCount, genreId.get(), file)

            withContext(Dispatchers.Main) {
                when {
                    response.isSuccessful -> {
                        if (response.body() != null) {
                            postCreateBlogResponse.value =
                                Resource.success(response.body()!!)
                        }
                    }
                    else -> postCreateBlogResponse.value = Resource.error(response.message())

                }
            }
        }

    }

}