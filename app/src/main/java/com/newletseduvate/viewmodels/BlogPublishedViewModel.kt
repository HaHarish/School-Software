package com.newletseduvate.viewmodels

import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.newletseduvate.model.blog.StudentBlogModel
import com.newletseduvate.repository.BlogRepository
import com.newletseduvate.utils.Resource
import com.newletseduvate.utils.extensions.combineWith
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

class BlogPublishedViewModel @Inject constructor(private val repository: BlogRepository) :
    ViewModel() {

    var moduleId = ObservableInt()

    val filterClicked = ObservableBoolean(false)

    val isPageLoadingOrchids = MutableLiveData(true)
    val isPageLoadingMyBranch = MutableLiveData(true)
    val isPageLoadingMyGrade = MutableLiveData(true)
    val isPageLoadingMySection = MutableLiveData(true)

    val mediatorIsPageLoading = isPageLoadingOrchids.combineWith(isPageLoadingMyBranch, isPageLoadingMyGrade, isPageLoadingMySection) {
            isPageLoadingOrchids, isPageLoadingMyBranch,isPageLoadingMyGrade, isPageLoadingMySection  ->
        isPageLoadingOrchids!! && isPageLoadingMyBranch!! && isPageLoadingMyGrade!! && isPageLoadingMySection!!
    }

    val isNoDataOrchids = ObservableBoolean(false)
    val isNoDataMyBranch = ObservableBoolean(false)
    val isNoDataMyGrade = ObservableBoolean(false)
    val isNoDataMySection = ObservableBoolean(false)

    val currentPageOrchids = MutableLiveData(1)
    val currentPageMyBranch = MutableLiveData(1)
    val currentPageMyGrade = MutableLiveData(1)
    val currentPageMySection = MutableLiveData(1)

    val totalPageOrchids = ObservableInt(0)
    val totalPageMyBranch = ObservableInt(0)
    val totalPageMyGrade = ObservableInt(0)
    val totalPageMySection= ObservableInt(0)

    var isDataLoadingOrchids = MutableLiveData(false)
    var isDataLoadingMyBranch = MutableLiveData(false)
    var isDataLoadingMyGrade = MutableLiveData(false)
    var isDataLoadingMySection = MutableLiveData(false)

    val fromDate = ObservableField(0L)
    val toDate = ObservableField(0L)

    init {
        setFilterDate()
    }

    var orchidsBlogListResponse = MutableLiveData<Resource<ArrayList<StudentBlogModel>>>()
    var myBranchBlogListResponse = MutableLiveData<Resource<ArrayList<StudentBlogModel>>>()
    var myGradeBlogListResponse = MutableLiveData<Resource<ArrayList<StudentBlogModel>>>()
    var mySectionBlogListResponse = MutableLiveData<Resource<ArrayList<StudentBlogModel>>>()

    fun getOrchidBlogListFromApi(){
        getPublishedBlog(orchidsBlogListResponse, totalPageOrchids, "", currentPageOrchids.value.toString(),"1")
    }

    fun getMyBranchBlogListFromApi(){
        getPublishedBlog(myBranchBlogListResponse, totalPageMyBranch, "", currentPageMyBranch.value.toString(),"2")
    }

    fun getMyGradeBlogListFromApi(){
        getPublishedBlog(myGradeBlogListResponse, totalPageMyGrade, "", currentPageMyGrade.value.toString(),"3")
    }

    fun getMySectionBlogListFromApi(){
        getPublishedBlog(mySectionBlogListResponse, totalPageMySection, "", currentPageMySection.value.toString(),"4")
    }

    private fun getPublishedBlog(
        genericListResponse: MutableLiveData<Resource<ArrayList<StudentBlogModel>>>,
        totalPageOrchids: ObservableInt,
        status: String,
        pageNumber: String,
        publishedLevel: String
    ) {

        GlobalScope.launch {
            val response = repository.getPublishedBlog(
                moduleId.get().toString(),
                "4",
                pageNumber,
                publishedLevel
            )

            withContext(Dispatchers.Main) {
                when {
                    response.isSuccessful -> {
                        if (response.body() != null) {
                            genericListResponse.value =
                                Resource.success(response.body()!!.result?.data)

                            response.body()!!.result?.totalPages?.let { totalPageOrchids.set(it) }
                        }
                    }
                    else -> genericListResponse.value = Resource.error(response.message())

                }
            }
        }

    }

    private fun setFilterDate() {
        val calendar = Calendar.getInstance()
        toDate.set(calendar.timeInMillis)

        calendar.add(Calendar.DAY_OF_YEAR, -6)
        fromDate.set(calendar.timeInMillis)
    }

    fun refreshPage() {

        filterClicked.set(false)

        isPageLoadingOrchids.value = true
        isPageLoadingMyBranch.value = true
        isPageLoadingMyGrade.value = true
        isPageLoadingMySection.value = true

        isNoDataOrchids.set(false)
        isNoDataMyBranch.set(false)
        isNoDataMyGrade.set(false)
        isNoDataMySection.set(false)

        currentPageOrchids.value = 1
        currentPageMyBranch.value = 1
        currentPageMyGrade.value = 1
        currentPageMySection.value = 1

    }

    fun clear() {

        filterClicked.set(false)

        isPageLoadingOrchids.value = true
        isPageLoadingMyBranch.value = true
        isPageLoadingMyGrade.value = true
        isPageLoadingMySection.value = true

        isNoDataOrchids.set(false)
        isNoDataMyBranch.set(false)
        isNoDataMyGrade.set(false)
        isNoDataMySection.set(false)

        currentPageOrchids.value = 1
        currentPageMyBranch.value = 1
        currentPageMyGrade.value = 1
        currentPageMySection.value = 1

        orchidsBlogListResponse = MutableLiveData<Resource<ArrayList<StudentBlogModel>>>()
        myBranchBlogListResponse = MutableLiveData<Resource<ArrayList<StudentBlogModel>>>()
        myGradeBlogListResponse = MutableLiveData<Resource<ArrayList<StudentBlogModel>>>()
        mySectionBlogListResponse = MutableLiveData<Resource<ArrayList<StudentBlogModel>>>()

        setFilterDate()
    }

}