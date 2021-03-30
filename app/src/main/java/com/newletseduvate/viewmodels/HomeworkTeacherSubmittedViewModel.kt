package com.newletseduvate.viewmodels

import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.newletseduvate.model.homeWork.HomeworkTeacherDetailsSubmittedModel
import com.newletseduvate.model.homeWork.HomeworkTeacherResponse
import com.newletseduvate.repository.HomeWorkTeacherRepository
import com.newletseduvate.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.collections.ArrayList

class HomeworkTeacherSubmittedViewModel @Inject constructor(private val repository: HomeWorkTeacherRepository) :
    ViewModel() {

    var hwDetailsID = ObservableField("")

    val isPageLoadingTeacher = MutableLiveData(true)
    val isNoDataTeacher = ObservableBoolean(false)

    var teacherHomeWorkListResponse =
        MutableLiveData<Resource<HomeworkTeacherResponse>>()
    var teacherHomeWorkList =
        MutableLiveData<Resource<ArrayList<HomeworkTeacherDetailsSubmittedModel>>>()

    fun getTeacherHomeWork() {

        GlobalScope.launch {
            val response = repository.getHomeWorkTeacherDetailsSubmitted(
                "1",
                hwDetailsID.get().toString()
            )

            withContext(Dispatchers.Main) {
                when {
                    response.isSuccessful -> {

                        if (response.body() != null && response.body()!!.data != null) {
                            teacherHomeWorkList.value = Resource.success(response.body()?.data?.hwQuestions)

                        }

                    }
                    else -> {
                        teacherHomeWorkListResponse.value = Resource.error(response.message())
                    }
                }
            }
        }

    }

}