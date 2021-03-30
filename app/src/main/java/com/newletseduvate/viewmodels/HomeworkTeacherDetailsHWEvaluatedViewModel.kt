package com.newletseduvate.viewmodels

import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.newletseduvate.model.homeWork.HomeworkTeacherDetailsEvaluatedResponse
import com.newletseduvate.model.homeWork.HomeworkTeacherModel
import com.newletseduvate.repository.HomeWorkTeacherRepository
import com.newletseduvate.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class HomeworkTeacherDetailsHWEvaluatedViewModel @Inject constructor(private val repository: HomeWorkTeacherRepository) :
    ViewModel() {

    var subjectID = ObservableField("")
    val homeworkTeacherModel = MutableLiveData<HomeworkTeacherModel>()

    val isPageLoadingTeacher = MutableLiveData(true)
    val isNoDataTeacher = ObservableBoolean(false)

    var teacherHomeWorkEvaluatedResponse =
        MutableLiveData<Resource<HomeworkTeacherDetailsEvaluatedResponse>>()

    fun getHomeworkTeacherDetailsEvaluated() {

        GlobalScope.launch {
            val response = repository.getHomeworkTeacherDetailsEvaluated(
                homeworkTeacherModel.value?.id.toString(),
                subjectID.get().toString()
            )

            withContext(Dispatchers.Main) {
                when {
                    response.isSuccessful -> {

                        if (response.body() != null && response.body()!!.statusCode == 200) {
                            teacherHomeWorkEvaluatedResponse.value =
                                Resource.success(response.body())

                        }

                    }
                    else -> {
                        teacherHomeWorkEvaluatedResponse.value = Resource.error(response.message())
                    }
                }
            }
        }

    }

}