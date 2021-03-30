package com.newletseduvate.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.newletseduvate.utils.Resource
import com.newletseduvate.model.online_class.ViewCoursePlanResponse
import com.newletseduvate.repository.OnlineClassRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ViewCoursePlanViewModel @Inject constructor(private val repository: OnlineClassRepository) :
    ViewModel() {

    var viewCoursePlanResponse = MutableLiveData<Resource<ViewCoursePlanResponse>>()

    fun getViewCoursePlan(courseId: Int){
        GlobalScope.launch {
            val response = repository.getViewCoursePlan(courseId)
            withContext(Dispatchers.Main){
                when(response.body()?.statusCode){
                    200 -> {
                        viewCoursePlanResponse.value = Resource.success(response.body())
                    }
                    // 401 -> circularFilter.value = response.body()?.message?.let { Resource.error(it) }
                    else -> viewCoursePlanResponse.value = Resource.error("Something went wrong")
                }
            }
        }
    }
}