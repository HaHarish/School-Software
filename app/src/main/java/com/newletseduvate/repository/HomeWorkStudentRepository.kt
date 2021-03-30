package com.newletseduvate.repository

import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.newletseduvate.BuildConfig.MEDIA_HOMEWORK_URL
import com.newletseduvate.api.ForgotPasswordService
import com.newletseduvate.api.HomeWorkService
import com.newletseduvate.api.LoginServices
import com.newletseduvate.model.general.UploadFileModel
import com.newletseduvate.model.homeWork.*
import com.newletseduvate.utils.constants.Constants
import com.newletseduvate.utils.extensions.getToken
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.collections.ArrayList

@Singleton
class HomeWorkStudentRepository @Inject constructor(
    private val apiService: HomeWorkService,
    private val pref: SharedPreferences
) {

    /*lateinit var apiService: HomeWorkService

    init {

        val loggingInterceptor = HttpLoggingInterceptor(object : HttpLoggingInterceptor.Logger {
            override fun log(message: String) {
                Log.d("OkHttp",message)
            }
        })
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        val builder = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .retryOnConnectionFailure(true)
            .readTimeout(3, TimeUnit.MINUTES)
            .connectTimeout(3, TimeUnit.MINUTES)
            .callTimeout(3, TimeUnit.MINUTES)
            .writeTimeout(3, TimeUnit.MINUTES)

        val retrofit = Retrofit.Builder()
            .baseUrl(pref.getUrlValue())
            .client(builder.build())
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        retrofit.create(LoginServices::class.java)

        apiService = retrofit.create(HomeWorkService::class.java)
    }*/

    suspend fun getHomeWorkStudent(
        moduleId: String,
        startDate: Long,
        toDate: Long,
    ): Response<HomeworkStudentResponse> {
        val requestDateFormat = SimpleDateFormat(Constants.DateFormat.YYYYMMDD)

        return apiService.getHomeWorkStudent(
            pref.getToken().toString(),
            moduleId,
            requestDateFormat.format(Date(startDate)),
            requestDateFormat.format(Date(toDate)),
        )
    }

    suspend fun getHomeWorkStudentDetail(
        hwDetailsId: String,
        module_id: String,
        hw_status: String
    ): Response<HomeworkStudentDetailsSubmittedResponse> {

        return apiService.getHomeWorkStudentDetail(
            pref.getToken().toString(),
            hwDetailsId,
            module_id,
            hw_status
        )
    }

    suspend fun getHomeWorkStudentDetailOpened(
        hwDetailsId: String,
        module_id: String,
        hw_status: String
    ): Response<HomeworkStudentDetailsOpenedResponse> {

        return apiService.getHomeWorkStudentDetailOpened(
            pref.getToken().toString(),
            hwDetailsId,
            module_id,
            hw_status
        )
    }

    suspend fun getHomeWorkStudentDetailEvaluated(
        hwDetailsId: String,
        module_id: String,
        hw_status: String
    ): Response<HomeworkStudentDetailsEvaluatedResponse> {

        return apiService.getHomeWorkStudentDetailEvaluated(
            pref.getToken().toString(),
            hwDetailsId,
            module_id,
            hw_status
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

    suspend fun postDeleteFile(fileName: String): Response<JsonObject> {

        // Todo : doubt in url
        val body = JsonObject()
        body.addProperty("file_name", fileName)
        return apiService.postDeleteFile(
            pref.getToken().toString(),
            body
        )
    }


    suspend fun postHomeworkSubmission(
        isUploadQuestionWise: Boolean,
        homeworkId: Int?,
        comment: String,
        uploadBulkFilesList: MutableLiveData<ArrayList<HomeworkUploadQuestionFileModel>>,
        uploadQuestionWiseList: ArrayList<HomeworkStudentDetailsOpenedModifiedModel>
    ): Response<JsonObject> {

        val body = JsonObject()
        body.addProperty("is_question_wise", isUploadQuestionWise)
        body.addProperty("homework", homeworkId)
        body.addProperty("comment", comment)

        if (!isUploadQuestionWise) {
            val questionArray = JsonArray()
            val zeroThObject = JsonObject()
            val attachments = JsonArray()

            uploadBulkFilesList.value?.forEach {
                attachments.add(it.url.toString())
            }
            zeroThObject.add("attachments", attachments)
            questionArray.add(zeroThObject)

            body.add("questions", questionArray)

        } else {

            val questionArray = JsonArray()

            uploadQuestionWiseList.forEach {

                val questionModel = JsonObject()

                questionModel.addProperty("homework_question", it.id)

                val attachments = JsonArray()

                questionModel.add("attachments", attachments)

                it.uploadHomeworkList.value?.forEach { model ->
                    attachments.add(model.url.toString())
                }

                questionArray.add(questionModel)
            }

            body.add("questions", questionArray)
        }

        return apiService.postHomeworkSubmission(
            pref.getToken().toString(),
            body
        )
    }
}