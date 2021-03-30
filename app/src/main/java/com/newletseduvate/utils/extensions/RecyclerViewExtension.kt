package com.newletseduvate.utils.extensions

import android.view.View
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableInt
import androidx.databinding.ViewDataBinding
import androidx.databinding.library.baseAdapters.BR
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.newletseduvate.R
import com.newletseduvate.adapter.dynamic_recycler_adapter.CallBackModel
import com.newletseduvate.adapter.dynamic_recycler_adapter.RecyclerDynamicAdapter
import com.newletseduvate.utils.EndlessScrollListener
import com.newletseduvate.utils.NetworkCheck
import com.newletseduvate.utils.Resource
import com.newletseduvate.utils.Status

fun <BIND_TYPE : ViewDataBinding, MODEL_TYPE> RecyclerView.setUpRecyclerView(
    layoutId: Int,
    callbacks: ArrayList<CallBackModel<MODEL_TYPE>>,
    listResponse: MutableLiveData<Resource<ArrayList<MODEL_TYPE>>>,
    viewLifecycleOwner: LifecycleOwner,
    root: View,
    apiMethodCall: () -> Unit = {},
    isNoDataDaily: ObservableBoolean? = null,
    totalPageDaily: ObservableInt? = null,
    isPageLoadingDaily: MutableLiveData<Boolean>? = null,
    isDataLoadingDaily: MutableLiveData<Boolean>? = null,
    currentPageDaily: MutableLiveData<Int>? = null,
    variableId: Int = BR.model,
    errorMessage: String = context.getString(R.string.check_internet),
    retryText: String = context.getString(R.string.retry),
    retryFunction: () -> Unit = {},
): RecyclerDynamicAdapter<BIND_TYPE, MODEL_TYPE> {

    val linearLayoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
    this.layoutManager = linearLayoutManager

    val recyclerDynamicAdapter =
        RecyclerDynamicAdapter<BIND_TYPE, MODEL_TYPE>(layoutId, callbacks, variableId)
    this.adapter = recyclerDynamicAdapter
    this.setHasFixedSize(true)

    listResponse.observe(viewLifecycleOwner, {
        isDataLoadingDaily?.value = false

        it?.let {

            when (it.status) {
                Status.Success -> {
                    if (currentPageDaily?.value == 1)
                        recyclerDynamicAdapter.clearList()
                    it.data?.let { it1 -> recyclerDynamicAdapter.submitList(it1) }
                    if (recyclerDynamicAdapter.itemCount == 0)
                        isNoDataDaily?.set(true)
                    else
                        isNoDataDaily?.set(false)
                }
                else -> {
                    root.snackBar(resources.getString(R.string.something_went_wrong)) {}
                }
            }
        }

        isPageLoadingDaily?.value = false
    })

    isDataLoadingDaily?.let {
        this.addOnScrollListener(object :
            EndlessScrollListener(linearLayoutManager) {

            override fun loadMoreItems() {
                isDataLoadingDaily.value = true
                currentPageDaily?.value = currentPageDaily?.value?.plus(1)

                if (NetworkCheck.isInternetAvailable(context)) {
                    if (currentPageDaily?.value == 1)
                        recyclerDynamicAdapter.clearList()
                    apiMethodCall()
                } else {
                    root.snackBar(
                        errorMessage,
                        retryText,
                        true
                    ) { retryFunction() }
                }
            }

            override fun getTotalPageCount(): Int {
                return totalPageDaily?.let { totalPageDaily.get() } ?: 1
            }

            override fun isLastPage(): Boolean {
                val totalPage = getTotalPageCount()
                return currentPageDaily?.value!! >= totalPage
            }

            override fun isLoading(): Boolean {
                return isDataLoadingDaily.let { isDataLoadingDaily.value } ?: false
            }
        })
    }

    if (NetworkCheck.isInternetAvailable(context)) {
        if (currentPageDaily?.value == 1)
            recyclerDynamicAdapter.clearList()
        apiMethodCall()
    } else {
        root.snackBar(
            errorMessage,
            retryText,
            true
        ) { retryFunction() }
    }

    return recyclerDynamicAdapter
}