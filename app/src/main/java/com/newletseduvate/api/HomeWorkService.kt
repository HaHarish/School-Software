package com.newletseduvate.api

import com.google.gson.JsonObject
import com.newletseduvate.model.homeWork.*
import com.newletseduvate.utils.constants.ApiConstants
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*

interface HomeWorkService {

    @GET(ApiConstants.homeworkStudentUrl)
    suspend fun getHomeWorkStudent(
        @Header(ApiConstants.AUTHORIZATION) token: String,
        @Query("module_id") module_id: String,
        @Query("start_date") start_date: String,
        @Query("end_date") end_date: String
    ): Response<HomeworkStudentResponse>

    @GET(ApiConstants.homeworkStudentDetailsUrl)
    suspend fun getHomeWorkStudentDetail(
        @Header(ApiConstants.AUTHORIZATION) token: String,
        @Path("hw_details_id") hw_details_id: String,
        @Query("module_id") module_id: String,
        @Query("hw_status") hw_status: String
    ): Response<HomeworkStudentDetailsSubmittedResponse>

    @GET(ApiConstants.homeworkStudentDetailsUrl)
    suspend fun getHomeWorkStudentDetailOpened(
        @Header(ApiConstants.AUTHORIZATION) token: String,
        @Path("hw_details_id") hw_details_id: String,
        @Query("module_id") module_id: String,
        @Query("hw_status") hw_status: String
    ): Response<HomeworkStudentDetailsOpenedResponse>

    @GET(ApiConstants.homeworkStudentDetailsUrl)
    suspend fun getHomeWorkStudentDetailEvaluated(
        @Header(ApiConstants.AUTHORIZATION) token: String,
        @Path("hw_details_id") hw_details_id: String,
        @Query("module_id") module_id: String,
        @Query("hw_status") hw_status: String
    ): Response<HomeworkStudentDetailsEvaluatedResponse>

    @POST(ApiConstants.homeworkStudentUploadQuestionFile)
    @Multipart
    suspend fun postUploadQuestionFile(
        @Header(ApiConstants.AUTHORIZATION) token: String,
        @Part file: MultipartBody.Part,
    ): Response<JsonObject>


    @POST(ApiConstants.homeworkStudentDeleteFile)
    suspend fun postDeleteFile(
        @Header(ApiConstants.AUTHORIZATION) token: String,
        @Body body: JsonObject
    ): Response<JsonObject>

    @POST(ApiConstants.homeworkStudentSubmission)
    suspend fun postHomeworkSubmission(
        @Header(ApiConstants.AUTHORIZATION) token: String,
        @Body body: JsonObject
    ): Response<JsonObject>

    //Teacher

    @GET(ApiConstants.homeworkStudentUrl)
    suspend fun getHomeWorkTeacher(
        @Header(ApiConstants.AUTHORIZATION) token: String,
        @Query("module_id") module_id: String,
        @Query("start_date") start_date: String,
        @Query("end_date") end_date: String
    ): Response<HomeworkTeacherResponse>


    @GET(ApiConstants.homeworkTeacherSubmittedUrl)
    suspend fun getHomeWorkTeacherDetailsSubmitted(
        @Header(ApiConstants.AUTHORIZATION) token: String,
        @Path("hw_details_id") hw_details_id: String,
        @Query("hw_status") hw_status: String,
    ): Response<HomeworkTeacherDetailsSubmittedResponse>

    @GET(ApiConstants.homeworkTeacherEvaluationCompletedUrl)
    suspend fun putTeacherEvaluationCompleted(
        @Header(ApiConstants.AUTHORIZATION) token: String,
        @Body body: JsonObject
    ): Response<JsonObject>

    @GET(ApiConstants.homeworkTeacherSubmittedDataUrl)
    suspend fun getHomeworkTeacherDetailsEvaluated(
        @Header(ApiConstants.AUTHORIZATION) token: String,
        @Query("homework") homework: String,
        @Query("subject") subject: String
    ): Response<HomeworkTeacherDetailsEvaluatedResponse>

    @GET(ApiConstants.homeworkTeacherStudentHomeworkUrl)
    suspend fun getHomeworkTeacherEvaluate(
        @Header(ApiConstants.AUTHORIZATION) token: String,
        @Query("student_homework") student_homework: String
    ): Response<HomeworkTeacherEvaluateResponse>


    @POST(ApiConstants.homeworkTeacherUploadHomeworkUrl)
    suspend fun postUploadHomework(
        @Header(ApiConstants.AUTHORIZATION) token: String,
        @Body body: JsonObject
    ): Response<JsonObject>

    @PUT(ApiConstants.homeworkEvaluationCompleted)
    suspend fun postEvaluationCompleted(
        @Header(ApiConstants.AUTHORIZATION) token: String,
        @Path("student_homework") student_homework: String,
        @Body body: JsonObject
    ): Response<JsonObject>

    @PUT(ApiConstants.homeworkTeacherEvaluationUrl)
    suspend fun postTeacherEvaluation(
        @Header(ApiConstants.AUTHORIZATION) token: String,
        @Path("question_id") question_id: String,
        @Body body: JsonObject
    ): Response<JsonObject>

}