package com.newletseduvate.viewmodels

import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableInt
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.newletseduvate.model.online_class.OnlineClassStudentOcDetailsModifiedModel
import com.newletseduvate.repository.OnlineClassRepository
import com.newletseduvate.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class OnlineClassAttendDetailsViewModel @Inject constructor(private val repository: OnlineClassRepository) :
    ViewModel() {

    val isPageLoadingStudent = MutableLiveData(true)
    val isNoDataStudent = ObservableBoolean(false)

    var isViewCoursePlan : Boolean = false

    val onlineClassListSize = ObservableInt(0)
    val onlineClassListResponse =
        MutableLiveData<Resource<ArrayList<OnlineClassStudentOcDetailsModifiedModel>>>()

    fun getStudentOnlineClassOcDetails(zoomId: String, currentServerTime: String) {

        GlobalScope.launch {
            val response = repository.getStudentOnlineClassOcDetails(zoomId)

            withContext(Dispatchers.Main) {
                when {
                    response.isSuccessful -> {

                        if (response.body() != null && response.body()!!.data != null) {
                            val tempList = ArrayList<OnlineClassStudentOcDetailsModifiedModel>()

                            response.body()!!.data!!.forEach {
                                tempList.add(it.convertToModified(currentServerTime))
                            }

                            onlineClassListSize.set(tempList.size)
                            onlineClassListResponse.value = Resource.success(tempList)
                        }

                    }
                    else -> {
                        onlineClassListResponse.value = Resource.error(response.message())
                    }
                }
            }

        }

    }

    fun putMarkAttendance(
        classDate: String,
        acceptedOrRejected: String,
        zoomId: String
    ) {

        GlobalScope.launch {
            val response = repository.putMarkAttendance(classDate, acceptedOrRejected, zoomId)

            withContext(Dispatchers.Main) {
                when {
                    response.isSuccessful -> {

                        if (response.body() != null) {

                        }

                    }
                    else -> {
                        onlineClassListResponse.value = Resource.error(response.message())
                    }
                }
            }

        }

    }

}