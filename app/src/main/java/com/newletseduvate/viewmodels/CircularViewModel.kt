package com.newletseduvate.viewmodels

import android.content.SharedPreferences
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.newletseduvate.model.circular.student.CircularResultModel
import com.newletseduvate.model.circular.teacher.CircularTeacherDeleteRequest
import com.newletseduvate.model.circular.teacher.CircularTeacherDeleteResponse
import com.newletseduvate.utils.Resource
import com.newletseduvate.repository.CircularRepository
import com.newletseduvate.utils.constants.ModulesConstant
import com.newletseduvate.utils.extensions.getRoleId
import com.newletseduvate.utils.extensions.getToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CircularViewModel @Inject constructor(private val sharedPreferences: SharedPreferences,
                                            private val circularRepository: CircularRepository): ViewModel(){

    val fromDate = ObservableField(0L)
    val toDate = ObservableField(0L)

    var currentPageStudent = MutableLiveData(1)
    val isPageLoadingStudent = MutableLiveData(true)
    val isNoDataStudent = ObservableBoolean(false)
    val totalPageStudent = ObservableInt(0)
    var isDataLoadingStudent = MutableLiveData(false)

    var circularFilterResponse = MutableLiveData<Resource<ArrayList<CircularResultModel>>>()

    var moduleId : Int = 0

    fun circularFilter(){
        GlobalScope.launch {
            val response = circularRepository.circularFilterResponse(fromDate.get()!!,
                toDate.get()!!,
                currentPageStudent.value.toString(),
                totalPageStudent.get().toString(),
                sharedPreferences.getRoleId(),
                moduleId,
                ModulesConstant.Circular.studentCircular)
            withContext(Dispatchers.Main){
                when(response.body()?.statusCode){
                    200 -> {
                        circularFilterResponse.value = Resource.success(response.body()?.result!!)
                    }
                    // 401 -> circularFilter.value = response.body()?.message?.let { Resource.error(it) }
                    else -> circularFilterResponse.value = Resource.error("Something went wrong")
                }
            }
        }
    }

    fun refreshPage() {

        isPageLoadingStudent.value = true
        isNoDataStudent.set(false)
        currentPageStudent.value = 1

        circularFilter()
    }


}