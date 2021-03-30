package com.newletseduvate.viewmodels

import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableInt
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.newletseduvate.model.online_class.ClassTypeModel
import com.newletseduvate.model.online_class.StudentAttendOnlineClassResponse
import com.newletseduvate.model.online_class.StudentOnlineClassModel
import com.newletseduvate.repository.OnlineClassRepository
import com.newletseduvate.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Created by SHASHANK BHAT on 18-Feb-21.
 *
 */
class OnlineClassAttendViewModel @Inject constructor(private val repository: OnlineClassRepository) :
    ViewModel() {

    var moduleId = ObservableInt()

    val filterClicked = ObservableBoolean(false)

    var currentPageStudent = MutableLiveData(1)
    val isPageLoadingStudent = MutableLiveData(false)
    val isNoDataStudent = ObservableBoolean(true)
    val totalPageStudent = ObservableInt(0)
    var isDataLoadingStudent = MutableLiveData(false)

    val onlineClassListResponse = MutableLiveData<Resource<ArrayList<StudentOnlineClassModel>>>()
    val onlineClassResponse = MutableLiveData<Resource<StudentAttendOnlineClassResponse>>()

    fun getStudentOnlineClass() {

        GlobalScope.launch {
            val response = repository.getStudentOnlineClass(classTypeId.get(), currentPageStudent.value)

            withContext(Dispatchers.Main) {
                when {
                    response.isSuccessful -> {

                        if (response.body() != null && response.body()!!.data != null) {
                            onlineClassResponse.value = Resource.success(response.body()!!)
                            onlineClassListResponse.value = Resource.success(response.body()!!.data!!)
                        }

                        response.body()!!.totalPages?.let { totalPageStudent.set(it) }

                    }
                    else -> {
                        onlineClassListResponse.value = Resource.error(response.message())
                    }
                }
            }

        }

    }

    var classTypeList = MutableLiveData<Resource<ArrayList<ClassTypeModel>>>()

    var classTypeId = ObservableInt(-1)


    fun refreshPage() {

        filterClicked.set(false)

        isPageLoadingStudent.value = true
        isNoDataStudent.set(false)
        currentPageStudent.value = 1

        getStudentOnlineClass()
    }

    fun clear() {

        filterClicked.set(false)

        classTypeList = MutableLiveData<Resource<ArrayList<ClassTypeModel>>>()

        isPageLoadingStudent.value = false
        isNoDataStudent.set(true)
        currentPageStudent.value = 1

        classTypeId.set(-1)
    }
}