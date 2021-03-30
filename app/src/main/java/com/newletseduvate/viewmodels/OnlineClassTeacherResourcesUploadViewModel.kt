package com.newletseduvate.viewmodels

import androidx.databinding.ObservableBoolean
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.newletseduvate.model.general.UploadFileModel
import com.newletseduvate.model.online_class.TeacherViewClassModel
import com.newletseduvate.repository.OnlineClassRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class OnlineClassTeacherResourcesUploadViewModel @Inject constructor(private val repository: OnlineClassRepository) :
    ViewModel() {

    val isPageLoadingStudent = MutableLiveData(true)
    val isNoDataStudent = ObservableBoolean(false)

    var isViewCoursePlan = ObservableBoolean(false)

    lateinit var onlineClassModel: TeacherViewClassModel
    var date: String = ""


    var uploadedFiles = MutableLiveData<ArrayList<String>>(ArrayList())
    var uploadFilesList = MutableLiveData<ArrayList<String>>(ArrayList())

    fun getResourceFiles() {
        GlobalScope.launch {
            val response = repository.getTeacherOnlineResourcesFiles(
                onlineClassModel.onlineClass?.id.toString(),
                date
            )
            withContext(Dispatchers.Main) {
                when {

                    response?.isSuccessful == true -> {

                        response.body()?.result?.forEach { result ->
                            result.files?.forEach {
                                uploadedFiles.value?.add(it)
                            }
                        }

                        uploadedFiles.value = uploadedFiles.value
                    }

                    else -> {}

                }
            }
        }
    }

    var homeworkUploadFileResponse = MutableLiveData(false)

    fun postUploadQuestionFile(file: UploadFileModel) {

        GlobalScope.launch {
            val response = repository.postUploadResourceFile(
                file,
                onlineClassModel.onlineClass?.id.toString(),
                date
            )
            withContext(Dispatchers.Main) {
                when {
                    response.isSuccessful -> {

                        val currList = uploadFilesList.value
                        currList?.add(response.body()?.get("result")?.asString.toString())

                        currList?.let {
                            uploadFilesList.value = it
                        }
                        homeworkUploadFileResponse.value = false
                    }
                    else -> homeworkUploadFileResponse.value = false

                }
            }
        }
    }

    var homeworkUploadSubmitResponse = MutableLiveData(false)

    fun postUploadResourceFile(files: ArrayList<String>) {

        GlobalScope.launch {
            val response = repository.putSubmitResourcesFile(
                files,
                onlineClassModel.onlineClass?.id.toString(),
                date
            )
            withContext(Dispatchers.Main) {
                when {
                    response.isSuccessful -> {
                        homeworkUploadSubmitResponse.value = true
                    }
                    else -> homeworkUploadSubmitResponse.value = false
                }
            }
        }
    }

    fun postDeleteFile(fileName: String) {
        GlobalScope.launch {
            repository.postDeleteFile(fileName)
        }
    }

    fun clear(){
        isPageLoadingStudent.value = true
        isNoDataStudent.set(false)

        isViewCoursePlan.set(false)

        uploadedFiles = MutableLiveData<ArrayList<String>>(ArrayList())
        uploadFilesList = MutableLiveData<ArrayList<String>>(ArrayList())
    }
}