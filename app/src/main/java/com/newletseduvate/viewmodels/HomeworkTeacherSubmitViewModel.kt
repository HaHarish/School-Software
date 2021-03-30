package com.newletseduvate.viewmodels

import androidx.databinding.ObservableBoolean
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.newletseduvate.model.general.UploadFileModel
import com.newletseduvate.model.homeWork.HomeworkTeacherDetailsEvaluatedResponse
import com.newletseduvate.model.homeWork.HomeworkTeacherEvaluateOneAttachmentModel
import com.newletseduvate.model.homeWork.HomeworkTeacherEvaluateOneQuestionEvaluateAttachmentsModel
import com.newletseduvate.model.homeWork.HomeworkTeacherEvaluateResponse
import com.newletseduvate.repository.HomeWorkTeacherRepository
import com.newletseduvate.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class HomeworkTeacherSubmitViewModel @Inject constructor(private val repository: HomeWorkTeacherRepository) :
    ViewModel() {

    var homeworkTeacherEvaluatedModel =
        MutableLiveData<HomeworkTeacherDetailsEvaluatedResponse.Submitted>()

    var isPageLoadingTeacher = MutableLiveData(true)
    val isBulkUpload = ObservableBoolean(true)

    var teacherHomeWorkEvaluateResponse =
        MutableLiveData<Resource<HomeworkTeacherEvaluateResponse>>()

    var attachmentListForQuestionWise =
        ArrayList<MutableLiveData<ArrayList<HomeworkTeacherEvaluateOneQuestionEvaluateAttachmentsModel>>>(ArrayList(ArrayList()))

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

    var attachmentListForBulk =
        MutableLiveData<ArrayList<HomeworkTeacherEvaluateOneQuestionEvaluateAttachmentsModel>>(
            ArrayList()
        )

    lateinit var currentUploadFilesList: MutableLiveData<ArrayList<HomeworkTeacherEvaluateOneQuestionEvaluateAttachmentsModel>>

    fun postUploadQuestionFile(file: UploadFileModel) {

        GlobalScope.launch {

            val response = repository.postUploadQuestionFile(file)

            withContext(Dispatchers.Main) {
                when {
                    response.isSuccessful -> {

                        val tempCurrUploadList = currentUploadFilesList.value
                        tempCurrUploadList?.add(
                            HomeworkTeacherEvaluateOneQuestionEvaluateAttachmentsModel(
                                response.body()?.get("data")?.asString.toString()
                            )
                        )
                        tempCurrUploadList?.let {
                            currentUploadFilesList.value = it
                        }
                    }
                }
            }
        }
    }

    var homeworkTeacherEvaluationForBulkType = MutableLiveData(false)

    fun postTeacherEvaluation(
        remark: String,
        comment: String,
        homeworkId: Int?,
        correctedFiles: ArrayList<HomeworkTeacherEvaluateOneAttachmentModel>
    ) {

        GlobalScope.launch {

            val response = repository.postTeacherEvaluation(homeworkId.toString(), comment, remark, correctedFiles, attachmentListForBulk.value)

            withContext(Dispatchers.Main) {
                when {
                    response.isSuccessful -> {
                        homeworkTeacherEvaluationForBulkType.value = true
                    }
                    else -> homeworkTeacherEvaluationForBulkType.value = false
                }
            }
        }

    }

    fun postTeacherEvaluationQuestionWise(
        remark: String,
        comment: String,
        homeworkId: Int?,
        correctedFiles: ArrayList<HomeworkTeacherEvaluateOneQuestionEvaluateAttachmentsModel>,
        evaluatedFiles: ArrayList<String>,
        ) {

        GlobalScope.launch {

            val response = repository.postTeacherEvaluationQuestionWise(homeworkId.toString(), comment, remark, correctedFiles, evaluatedFiles)

            withContext(Dispatchers.Main) {
                when {
                    response.isSuccessful -> {
                        homeworkTeacherEvaluationForBulkType.value = true
                    }
                    else -> homeworkTeacherEvaluationForBulkType.value = false
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

    fun clear(){
        homeworkTeacherEvaluatedModel =
            MutableLiveData<HomeworkTeacherDetailsEvaluatedResponse.Submitted>()

        isPageLoadingTeacher = MutableLiveData(true)
        isBulkUpload.set(true)

        teacherHomeWorkEvaluateResponse =
            MutableLiveData<Resource<HomeworkTeacherEvaluateResponse>>()

        attachmentListForBulk =
            MutableLiveData<ArrayList<HomeworkTeacherEvaluateOneQuestionEvaluateAttachmentsModel>>(
                ArrayList()
            )

        attachmentListForQuestionWise =
            ArrayList<MutableLiveData<ArrayList<HomeworkTeacherEvaluateOneQuestionEvaluateAttachmentsModel>>>(ArrayList(ArrayList()))
    }

}