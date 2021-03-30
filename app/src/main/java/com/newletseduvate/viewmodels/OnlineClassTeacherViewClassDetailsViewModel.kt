package com.newletseduvate.viewmodels

import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableInt
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.newletseduvate.model.online_class.TeacherViewClassDetailsModel
import com.newletseduvate.model.online_class.TeacherViewClassModel
import com.newletseduvate.repository.OnlineClassRepository
import com.newletseduvate.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class OnlineClassTeacherViewClassDetailsViewModel @Inject constructor(private val repository: OnlineClassRepository) :
    ViewModel() {

    val isPageLoadingStudent = MutableLiveData(true)
    val isNoDataStudent = ObservableBoolean(false)

    var isViewCoursePlan = ObservableBoolean(false)

    lateinit var onlineClassModel: TeacherViewClassModel
    var currentServerTime: String = ""

    val onlineClassDetailsListResponse =
        MutableLiveData<Resource<ArrayList<TeacherViewClassDetailsModel>>>()

    val onlineClassListSize = ObservableInt(0)

    fun getTeacherOnlineClassDetails(teacherClassId: String) {

        GlobalScope.launch {
            val response = repository.getTeacherOnlineClassDetails(teacherClassId)

            withContext(Dispatchers.Main) {
                when {
                    response.isSuccessful -> {

                        if (response.body() != null && response.body()?.data != null) {

                            val tempList = ArrayList<TeacherViewClassDetailsModel>()

                            response.body()?.data?.forEach {
                                it.currentServerTime = currentServerTime
                                it.zoomUrl = onlineClassModel.presenterUrl.toString()
                                tempList.add(it)
                            }

                            onlineClassDetailsListResponse.value = Resource.success(tempList)

                            onlineClassListSize.set(response.body()?.data?.size!!)
                        }

                    }
                    else -> {
                        onlineClassDetailsListResponse.value = Resource.error(response.message())
                    }
                }
            }

        }

    }


    fun putTeacherOnlineClassDetailsCancelClass(
        class_date: String
    ) {

        GlobalScope.launch {
            val response = onlineClassModel.id?.let {
                repository.putTeacherOnlineClassDetailsCancelClass(class_date,
                    it
                )
            }
            withContext(Dispatchers.Main) {
                when {
                    response?.isSuccessful == true -> {
                        if (response?.body() != null) { }
                    }
                    else -> {}
                }
            }
        }

    }

}