package com.newletseduvate.viewmodels

import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableInt
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.JsonObject
import com.newletseduvate.model.general.UploadFileModel
import com.newletseduvate.model.homeWork.*
import com.newletseduvate.repository.HomeWorkStudentRepository
import com.newletseduvate.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class HomeworkStudentDetailOpenedViewModel @Inject constructor(private val repository: HomeWorkStudentRepository) :
    ViewModel() {

    var moduleId = ObservableInt(1)
    lateinit var currentUploadFilesList: MutableLiveData<ArrayList<HomeworkUploadQuestionFileModel>>

    var isUploadQuestionWise = ObservableBoolean(false)

    var homeworkStudentModel = MutableLiveData<HomeWorkStudentModel>()

    var uploadFilesList = MutableLiveData<ArrayList<HomeworkUploadQuestionFileModel>>(ArrayList())

    var homeworkStudentDetailsOpened =
        MutableLiveData<Resource<HomeworkStudentDetailsOpenedResponse>>()
    var homeworkStudentDetailsListResponse =
        MutableLiveData<Resource<ArrayList<HomeworkStudentDetailsOpenedModel>>>()

    var questionFileUploadModelList =
        ArrayList<MutableLiveData<ArrayList<HomeworkUploadQuestionFileModel>>>()

    fun getHomeworkStudentDetails() {

        GlobalScope.launch {
            val response = repository.getHomeWorkStudentDetailOpened(
                homeworkStudentModel.value!!.id.toString(),
                "1",
                "1"
            )

            withContext(Dispatchers.Main) {
                when {
                    response.isSuccessful -> {
                        if (response.body() != null && response.body()!!.data != null) {
                            homeworkStudentDetailsOpened.value = Resource.success(response.body()!!)
                            homeworkStudentDetailsListResponse.value =
                                Resource.success(response.body()!!.data?.hwQuestions)
                        }
                    }
                    else -> homeworkStudentDetailsListResponse.value =
                        Resource.error(response.message())

                }
            }
        }

    }

    var homeworkUploadFileResponse = MutableLiveData(false)

    fun postUploadQuestionFile(file: UploadFileModel) {

        GlobalScope.launch {

            val response = repository.postUploadQuestionFile(file)

            withContext(Dispatchers.Main) {
                when {
                    response.isSuccessful -> {

                        val templist = currentUploadFilesList.value
                        templist?.add(
                            HomeworkUploadQuestionFileModel(
                                0,
                                response.body()?.get("data")?.asString,
                                null
                            )
                        )

                        templist?.let {
                            currentUploadFilesList.value = it
                        }

                        homeworkUploadFileResponse.value = false
                    }
                    else -> homeworkUploadFileResponse.value = false

                }
            }
        }
    }

    fun postDeleteFile(fileName: String) {

        GlobalScope.launch {
            val response = repository.postDeleteFile(fileName)

            withContext(Dispatchers.Main) {
                when {
                    response.isSuccessful -> {

                    }
                }
            }
        }

    }


    var homeworkSubmissionResponse = MutableLiveData(false)
    fun postHomeworkSubmission(
        comment: String,
        uploadQuestionWiseList: ArrayList<HomeworkStudentDetailsOpenedModifiedModel>
    ) {

        GlobalScope.launch {

            val homeworkId = homeworkStudentDetailsOpened.value?.data?.data?.id
            val response = repository.postHomeworkSubmission(
                isUploadQuestionWise.get(),
                homeworkId,
                comment,
                uploadFilesList,
                uploadQuestionWiseList
            )

            withContext(Dispatchers.Main) {
                when {
                    response.isSuccessful -> {
                        homeworkSubmissionResponse.value = true
                    }
                    else -> homeworkSubmissionResponse.value = false
                }
            }
        }

    }

    fun clear() {

        isUploadQuestionWise.set(false)

        homeworkStudentModel = MutableLiveData<HomeWorkStudentModel>()
        homeworkSubmissionResponse = MutableLiveData(false)

        uploadFilesList = MutableLiveData<ArrayList<HomeworkUploadQuestionFileModel>>(ArrayList())

        homeworkStudentDetailsOpened =
            MutableLiveData<Resource<HomeworkStudentDetailsOpenedResponse>>()
        homeworkStudentDetailsListResponse =
            MutableLiveData<Resource<ArrayList<HomeworkStudentDetailsOpenedModel>>>()

        questionFileUploadModelList = ArrayList()
    }

    fun refreshPage() {
        getHomeworkStudentDetails()
    }

}