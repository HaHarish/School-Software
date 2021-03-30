package com.newletseduvate.viewmodels

import androidx.databinding.ObservableInt
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.newletseduvate.model.homeWork.HomeWorkStudentModel
import com.newletseduvate.model.homeWork.HomeworkStudentDetailsSubmittedModel
import com.newletseduvate.model.homeWork.HomeworkStudentDetailsSubmittedResponse
import com.newletseduvate.repository.HomeWorkStudentRepository
import com.newletseduvate.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class HomeworkStudentDetailsSubmittedViewModel @Inject constructor(private val repository: HomeWorkStudentRepository) :
    ViewModel() {

    var moduleId = ObservableInt(1)

    var homeworkStudentModel = MutableLiveData<HomeWorkStudentModel>()

    var homeworkStudentDetails = MutableLiveData<Resource<HomeworkStudentDetailsSubmittedResponse>>()
//    var homeworkStudentDetailsListResponse = MutableLiveData<Resource<ArrayList<HomeworkStudentDetailsSubmittedModel>>>()

    fun getHomeworkStudentDetails() {

        GlobalScope.launch {
            val response = repository.getHomeWorkStudentDetail(
                homeworkStudentModel.value!!.id.toString(),
                "1",
                "2"
            )

            withContext(Dispatchers.Main) {
                when {
                    response.isSuccessful -> {
                        if (response.body() != null && response.body()!!.data != null && response.body()!!.statusCode != 409) {

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