package com.newletseduvate.repository

import android.content.SharedPreferences
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.newletseduvate.BuildConfig.MEDIA_HOMEWORK_URL
import com.newletseduvate.api.HomeWorkService
import com.newletseduvate.model.general.UploadFileModel
import com.newletseduvate.model.homeWork.*
import com.newletseduvate.utils.constants.Constants
import com.newletseduvate.utils.extensions.getToken
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.collections.ArrayList

@Singleton
class HomeWorkTeacherRepository @Inject constructor(
    private val apiService: HomeWorkService,
    private val pref: SharedPreferences
) {

    suspend fun getTeacherHomeWork(
        moduleId: String,
        startDate: Long,
        toDate: Long,
    ): Response<HomeworkTeacherResponse> {
        val requestDateFormat = SimpleDateFormat(Constants.DateFormat.YYYYMMDD)

        return apiService.getHomeWorkTeacher(
            pref.getToken().toString(),
            moduleId,
            requestDateFormat.format(Date(startDate)),
            requestDateFormat.format(Date(toDate)),
        )
    }

    suspend fun getHomeWorkTeacherDetailsSubmitted(
        hw_status: String,
        hw_details_id: String
    ): Response<HomeworkTeacherDetailsSubmittedResponse> {

        return apiService.getHomeWorkTeacherDetailsSubmitted(
            pref.getToken().toString(),
            hw_details_id,
            hw_status
        )
    }

    suspend fun getHomeworkTeacherDetailsEvaluated(
        homework: String,
        subject: String
    ): Response<HomeworkTeacherDetailsEvaluatedResponse> {

        return apiService.getHomeworkTeacherDetailsEvaluated(
            pref.getToken().toString(),
            homework,
            subject
        )
    }

    suspend fun getHomeworkTeacherEvaluate(
        student_homework: String
    ): Response<HomeworkTeacherEvaluateResponse> {

        return apiService.getHomeworkTeacherEvaluate(
            pref.getToken().toString(),
            student_homework
        )
    }


    suspend fun postUploadQuestionFile(file: UploadFileModel): Response<JsonObject> {

        val requestFile = file.file
            .asRequestBody("multipart/form-data".toMediaTypeOrNull())

        return apiService.postUploadQuestionFile(
            pref.getToken().toString(),
            MultipartBody.Part.createFormData(
                "file",
                "photo-" + System.currentTimeMillis().toString() + file.file.name,
                requestFile
            )
        )
    }

    suspend fun postUploadHomework(
        name: String,
        description: String,
        subject: String,
        date: String,
        questionList: ArrayList<HomeworkTeacherCreateHomeworkQuestionModel>
    ): Response<JsonObject> {

//        {"name":"aaa","description":"aa","subject":"9","date":"2021-03-27","questions":[{"question":"aaa","attachments":[],"is_attachment_enable":true,"max_attachment":6,"penTool":true}]}

        val body = JsonObject()
        body.addProperty("name", name)
        body.addProperty("description", description)
        body.addProperty("subject", subject)
        body.addProperty("date", date)

        val questionArray = JsonArray()

        questionList.forEach {
            val question = JsonObject()
            question.addProperty("question", it.question.get().toString())

            val attachmentArray = JsonArray()
            it.questionAttachments?.value?.forEach { attachments ->
                attachmentArray.add(attachments)
            }

            question.add("attachments", attachmentArray)
            question.addProperty("is_attachment_enable", it.isFileUpload.get())
            question.addProperty("max_attachment", it.maximumNumberOfFiles.get())
            question.addProperty("penTool", it.isPenToolEnabled.get())

            questionArray.add(question)
        }

        body.add("questions", questionArray)

        return apiService.postUploadHomework(
            pref.getToken().toString(),
            body
        )
    }



    suspend fun postEvaluationCompleted(
        remark: String,
        score: Int,
        studentHomeworkId: String
    ): Response<JsonObject> {

        val body = JsonObject()
        body.addProperty("remark", remark)
        body.addProperty("score", score)

        return apiService.postEvaluationCompleted(
            pref.getToken().toString(),
            studentHomeworkId,
            body
        )
    }

    suspend fun postTeacherEvaluation(
        question_id: String,
        comment: String,
        remarks: String,
        correctedFiles: ArrayList<HomeworkTeacherEvaluateOneAttachmentModel>,
        evaluatedFiles: ArrayList<HomeworkTeacherEvaluateOneQuestionEvaluateAttachmentsModel>?
    ): Response<JsonObject>{

        val body = JsonObject()
        body.addProperty("remarks", remarks)
        body.addProperty("comments", comment)

        val corrected_submission = JsonArray()
        val evaluated_files = JsonArray()

        correctedFiles.forEach {
            corrected_submission.add(it.url)
        }

        evaluatedFiles?.forEach {
            evaluated_files.add(it.url)
        }

        body.add("corrected_submission", corrected_submission)
        body.add("evaluated_files", evaluated_files)


        return apiService.postTeacherEvaluation(
            pref.getToken().toString(),
            question_id,
            body
        )
    }

    suspend fun postTeacherEvaluationQuestionWise(
        question_id: String,
        comment: String,
        remarks: String,
        correctedFiles: ArrayList<HomeworkTeacherEvaluateOneQuestionEvaluateAttachmentsModel>,
        evaluatedFiles: ArrayList<String>,
    ): Response<JsonObject>{

        val body = JsonObject()
        body.addProperty("remarks", remarks)
        body.addProperty("comments", comment)

        val corrected_submission = JsonArray()
        val evaluated_files = JsonArray()

        correctedFiles.forEach {
            corrected_submission.add(it.url)
        }

        evaluatedFiles.forEach {
            evaluated_files.add(it)
        }

        body.add("corrected_submission", corrected_submission)
        body.add("evaluated_files", evaluated_files)


        return apiService.postTeacherEvaluation(
            pref.getToken().toString(),
            question_id,
            body
        )
    }

    suspend fun postDeleteFile(fileName: String): Response<JsonObject> {

        val body = JsonObject()
        body.addProperty(
            "file_name",
            "$MEDIA_HOMEWORK_URL$fileName"
        )
        return apiService.postDeleteFile(
            pref.getToken().toString(),
            body
        )
    }

}