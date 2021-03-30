package com.newletseduvate.viewmodels


import androidx.databinding.ObservableBoolean
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.newletseduvate.model.homeWork.HomeworkTeacherDetailsEvaluatedResponse
import com.newletseduvate.model.homeWork.HomeworkTeacherEvaluateResponse
import com.newletseduvate.repository.HomeWorkTeacherRepository
import com.newletseduvate.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class HomeworkTeacherEvaluateViewModel @Inject constructor(private val repository: HomeWorkTeacherRepository) :
    ViewModel() {

    val homeworkTeacherEvaluatedModel =
        MutableLiveData<HomeworkTeacherDetailsEvaluatedResponse.Evaluated>()

    val isPageLoadingTeacher = MutableLiveData(true)
    val isBulkUpload = ObservableBoolean(true)

    var teacherHomeWorkEvaluateResponse =
        MutableLiveData<Resource<HomeworkTeacherEvaluateResponse>>()


    fun getHomeworkTeacherEvaluate() {

        GlobalScope.launch {
            val response = repository.getHomeworkTeacherEvaluate(
                homeworkTeacherEvaluatedModel.value?.studentHomeworkId.toString()
            )

            withContext(Dispatchers.Main) {
                when {
                    response.isSuccessful -> {

                        if (response.body() != null && response.body()!!.statusCode == 200) {
                            teacherHomeWorkEvaluateResponse.value =
                                Resource.success(response.body())

                        }

                    }
                    else -> {
                        teacherHomeWorkEvaluateResponse.value = Resource.error(response.message())
                    }
                }
            }
        }

    }

    var homeworkEvaluationDone = MutableLiveData(false)

    fun postEvaluationDone(
        remark: String,
        score: String
    ) {

        GlobalScope.launch {

            val response = repository.postEvaluationCompleted(
                remark,
                score.toInt(),
                homeworkTeacherEvaluatedModel.value?.studentHomeworkId.toString()
            )

            withContext(Dispatchers.Main) {
                when {
                    response.isSuccessful -> {

                        homeworkEvaluationDone.value = true
                    }
                    else -> homeworkEvaluationDone.value = false

                }
            }
        }

    }

}