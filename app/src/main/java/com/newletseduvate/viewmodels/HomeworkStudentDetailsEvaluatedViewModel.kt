package com.newletseduvate.viewmodels

import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableInt
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.newletseduvate.model.homeWork.*
import com.newletseduvate.repository.HomeWorkStudentRepository
import com.newletseduvate.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class HomeworkStudentDetailsEvaluatedViewModel @Inject constructor(private val repository: HomeWorkStudentRepository) :
    ViewModel() {

    var moduleId = ObservableInt(1)

    val isHWQuestionObject = ObservableBoolean(true)

    var homeworkStudentModel = MutableLiveData<HomeWorkStudentModel>()

    var homeworkStudentDetails = MutableLiveData<Resource<HomeworkStudentDetailsEvaluatedResponse>>()
//    var homeworkStudentDetailsListResponse = MutableLiveData<Resource<ArrayList<HomeworkStudentDetailsEvaluatedSecondModel>>>()

    fun getHomeworkStudentDetails() {

        GlobalScope.launch {
            val response = repository.getHomeWorkStudentDetailEvaluated(
                homeworkStudentModel.value!!.id.toString(),
                "1",
                "3"
            )

            withContext(Dispatchers.Main) {
                when {
                    response.isSuccessful -> {
                        if (response.body() != null && response.body()!!.data != null) {

                            homeworkStudentDetails.value = Resource.success(response.body())
//                            homeworkStudentDetailsListResponse.value =
//                                Resource.success(response.body()!!.data?.hwQuestions?.questions)
                        }
                    }
                    else -> homeworkStudentDetails.value = Resource.error(response.message())

                }
            }
        }

    }

}