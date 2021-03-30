package com.newletseduvate.viewmodels

import androidx.databinding.ObservableBoolean
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
import javax.inject.Inject
import kotlin.collections.ArrayList

class BlogStudentViewModel @Inject constructor(private val repository: BlogRepository) :
    ViewModel() {

    var moduleId = ObservableInt()

    val filterClicked = ObservableBoolean(false)
    var swipeRefreshLayout = ObservableBoolean(false)

    val isPageLoadingPendingReview = MutableLiveData(true)
    val isPageLoadingReviewed = MutableLiveData(true)
    val isPageLoadingDrafted = MutableLiveData(true)
    val isPageLoadingDeleted = MutableLiveData(true)

    val mediatorIsPageLoading = isPageLoadingPendingReview.combineWith(isPageLoadingReviewed, isPageLoadingDrafted, isPageLoadingDeleted) {
            isPageLoadingOrchids, isPageLoadingMyBranch,isPageLoadingMyGrade, isPageLoadingMySection  ->
        isPageLoadingOrchids!! && isPageLoadingMyBranch!! && isPageLoadingMyGrade!! && isPageLoadingMySection!!
    }

    val isNoDataPendingReview = ObservableBoolean(false)
    val isNoDataReviewed = ObservableBoolean(false)
    val isNoDataDrafted = ObservableBoolean(false)
    val isNoDataDeleted = ObservableBoolean(false)

    val currentPagePendingReview = MutableLiveData(1)
    val currentPageReviewed = MutableLiveData(1)
    val currentPageDrafted = MutableLiveData(1)
    val currentPageDeleted = MutableLiveData(1)

    val totalPagePendingReview = ObservableInt(0)
    val totalPageReviewed = ObservableInt(0)
    val totalPageDrafted = ObservableInt(0)
    val totalPageDeleted= ObservableInt(0)

    var isDataLoadingPendingReview = MutableLiveData(false)
    var isDataLoadingReviewed = MutableLiveData(false)
    var isDataLoadingDrafted = MutableLiveData(false)
    var isDataLoadingDeleted = MutableLiveData(false)

    var pendingReviewBlogListResponse = MutableLiveData<Resource<ArrayList<StudentBlogModel>>>()
    var reviewedBlogListResponse = MutableLiveData<Resource<ArrayList<StudentBlogModel>>>()
    var draftedBlogListResponse = MutableLiveData<Resource<ArrayList<StudentBlogModel>>>()
    var deletedBlogListResponse = MutableLiveData<Resource<ArrayList<StudentBlogModel>>>()

    fun getPendingReviewBlogListFromApi(){
        getBlog(pendingReviewBlogListResponse, totalPagePendingReview, "8,5", currentPagePendingReview.value.toString())
    }

    fun getReviewedBlogListFromApi(){
        getBlog(reviewedBlogListResponse, totalPageReviewed, "3,7,6,4", currentPageReviewed.value.toString())
    }

    fun getDraftedBlogListFromApi(){
        getBlog(draftedBlogListResponse, totalPageDrafted, "2", currentPageDrafted.value.toString())
    }

    fun getDeletedBlogListFromApi(){
        getBlog(deletedBlogListResponse, totalPageDeleted, "1", currentPageDeleted.value.toString())
    }

    private fun getBlog(
        genericListResponse: MutableLiveData<Resource<ArrayList<StudentBlogModel>>>,
        totalPage: ObservableInt,
        status: String,
        pageNumber: String
    ) {

        GlobalScope.launch {
            val response = repository.getStudentBlog(
                moduleId.get().toString(),
                status,
                pageNumber
            )

            withContext(Dispatchers.Main) {
                when {
                    response.isSuccessful -> {
                        if (response.body() != null) {
                            genericListResponse.value =
                                Resource.success(response.body()!!.result?.data)

                            response.body()!!.result?.totalPages?.let { totalPage.set(it) }
                        }
                    }
                    else -> genericListResponse.value = Resource.error(response.message())

                }
            }
        }

    }


    fun refreshPage() {

        filterClicked.set(false)
        swipeRefreshLayout.set(true)

        isPageLoadingPendingReview.value = true
        isPageLoadingReviewed.value = true
        isPageLoadingDrafted.value = true
        isPageLoadingDeleted.value = true

        isNoDataPendingReview.set(false)
        isNoDataReviewed.set(false)
        isNoDataDrafted.set(false)
        isNoDataDeleted.set(false)

        currentPagePendingReview.value = 1
        currentPageReviewed.value = 1
        currentPageDrafted.value = 1
        currentPageDeleted.value = 1

        getPendingReviewBlogListFromApi()
        getReviewedBlogListFromApi()
        getDraftedBlogListFromApi()
        getDeletedBlogListFromApi()
    }

    fun clear() {

        filterClicked.set(false)
        swipeRefreshLayout.set(false)

        isPageLoadingPendingReview.value = true
        isPageLoadingReviewed.value = true
        isPageLoadingDrafted.value = true
        isPageLoadingDeleted.value = true

        isNoDataPendingReview.set(false)
        isNoDataReviewed.set(false)
        isNoDataDrafted.set(false)
        isNoDataDeleted.set(false)

        currentPagePendingReview.value = 1
        currentPageReviewed.value = 1
        currentPageDrafted.value = 1
        currentPageDeleted.value = 1

        pendingReviewBlogListResponse = MutableLiveData<Resource<ArrayList<StudentBlogModel>>>()
        reviewedBlogListResponse = MutableLiveData<Resource<ArrayList<StudentBlogModel>>>()
        draftedBlogListResponse = MutableLiveData<Resource<ArrayList<StudentBlogModel>>>()
        deletedBlogListResponse = MutableLiveData<Resource<ArrayList<StudentBlogModel>>>()
    }

}