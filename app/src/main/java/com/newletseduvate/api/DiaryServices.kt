package com.newletseduvate.api

import com.newletseduvate.model.circular.teacher.CircularBranchResponse
import com.newletseduvate.model.circular.teacher.CircularTeacherDeleteFileRequest
import com.newletseduvate.model.circular.teacher.CircularTeacherDeleteFileResponse
import com.newletseduvate.model.circular.teacher.CircularTeacherUploadFileResponse
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.newletseduvate.model.diary.DailyDiaryResponse
import com.newletseduvate.model.diary.GeneralDiaryResponse
import com.newletseduvate.model.diary.teacher.*
import com.newletseduvate.utils.constants.ApiConstants
import com.newletseduvate.model.online_class.OnlineClassTeacherAcademicYearResponse
import com.newletseduvate.model.online_class.TeacherViewClassBranchResponse
import com.newletseduvate.model.online_class.TeacherViewClassGradeMappingResponse
import com.newletseduvate.model.online_class.TeacherViewClassSectionMappingResponse
import com.newletseduvate.utils.constants.ApiConstants.AUTHORIZATION
import com.newletseduvate.utils.constants.ApiConstants.deleteDiaryUrl
import com.newletseduvate.utils.constants.ApiConstants.generalDiaryMessagesUrl
import com.newletseduvate.utils.constants.ApiConstants.getTeacherDiaryActiveStudentsList
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

/**
 * Created by SHASHANK BHAT on 17-Feb-21.
 *
 *
 */
interface DiaryServices {

    @GET(generalDiaryMessagesUrl)
    suspend fun getGeneralDiaryMessages(
        @Header(AUTHORIZATION) token: String,
        @Query("module_id") module_id: String,
        @Query("start_date") start_date: String,
        @Query("end_date") end_date: String,
        @Query("page") page: String,
        @Query("dairy_type") dairy_type: String
    ): Response<GeneralDiaryResponse>

    @GET(generalDiaryMessagesUrl)
    suspend fun getDailyDiaryMessages(
        @Header(AUTHORIZATION) token: String,
        @Query("module_id") module_id: String,
        @Query("start_date") start_date: String,
        @Query("end_date") end_date: String,
        @Query("page") page: String,
        @Query("dairy_type") dairy_type: String,
    ): Response<DailyDiaryResponse>

    @GET(getTeacherDiaryActiveStudentsList)
    suspend fun getTeacherDiaryActiveStudents(
        @Header(AUTHORIZATION) token: String,
        @Query("academic_year") academic_year: Int,
        @Query("active") active: Int,
        @Query("page") page: Int,
        @Query("page_size") page_size: Int,
        @Query("bgs_mapping") bgs_mapping: Int,
        @Query("module_id") module_id: Int,
    ): Response<TeacherDiaryActiveStudentsResponse>

    @GET(ApiConstants.teacherDiaryBranch)
    suspend fun getDiaryBranch(
        @Header(ApiConstants.AUTHORIZATION) token: String,
        @Query ("session_year") session_year: Int,
        @Query ("module_id") module_id: Int
    ): Response<TeacherDiaryBranchResponse>

    @POST(ApiConstants.teacherDiaryFileUpload)
    @Multipart
    suspend fun teacherDiaryFileUpload(
        @Header(ApiConstants.AUTHORIZATION) token: String,
        @Part("branch") branch: RequestBody,
        @Part("grade") grade: RequestBody,
        @Part("section") section: RequestBody,
        @Part file: MultipartBody.Part
    ): Response<TeacherDiaryFileUploadResponse>

    @POST(ApiConstants.teacherDiaryCreate)
    suspend fun teacherDiaryCreate(
        @Header(ApiConstants.AUTHORIZATION) token: String,
        @Body teacherDiaryCreateRequest: TeacherDiaryCreateRequest
    ): Response<TeacherDiaryCreateResponse>

    @POST(ApiConstants.teacherDiaryCreate)
    suspend fun teacherDiaryCreateDaily(
        @Header(ApiConstants.AUTHORIZATION) token: String,
        @Body teacherDiaryCreateDailyRequest: TeacherDiaryCreateDailyRequest
    ): Response<TeacherDiaryCreateDailyResponse>

    /* Subjects */
    @GET(ApiConstants.teacherDiarySubjects)
    suspend fun teacherDiarySubjects(
        @Header(AUTHORIZATION) token: String,
        @Query("session_year") session_year: Int,
        @Query("branch") branch: Int,
        @Query("grade") grade: Int,
        @Query("section") section: Int,
        @Query("module_id") module_id: Int
    ): Response<TeacherDiarySubjectResponse>

    /* Chapters */
    @GET(ApiConstants.teacherDiaryChapters)
    suspend fun teacherDiaryChapters(
        @Header(AUTHORIZATION) token: String,
        @Query("session_year") session_year: Int,
        @Query("subject") subject: Int,
        @Query("module_id") module_id: Int
    ): Response<TeacherDiaryChapterResponse>

    @GET(ApiConstants.teacherAcademicYearUrl)
    suspend fun getAcademicYear(
        @Header(AUTHORIZATION) token: String
    ): Response<OnlineClassTeacherAcademicYearResponse>

    @GET(ApiConstants.teacherViewClassBranchUrl)
    suspend fun getBranches(
        @Header(AUTHORIZATION) token: String,
        @Query("module_id") module_id: String,
        @Query("session_year") session_year: String
    ): Response<TeacherViewClassBranchResponse>

    @GET(ApiConstants.teacherViewClassGradeMappingUrl)
    suspend fun getGradeMapping(
        @Header(AUTHORIZATION) token: String,
        @Query("module_id") module_id: String,
        @Query("session_year") session_year: String,
        @Query("branch_id") branch_id: String
    ): Response<TeacherViewClassGradeMappingResponse>

    @GET(ApiConstants.teacherViewClassSectionMappingUrl)
    suspend fun getSectionMapping(
        @Header(AUTHORIZATION) token: String,
        @Query("module_id") module_id: String,
        @Query("session_year") session_year: String,
        @Query("branch_id") branch_id: String,
        @Query("grade_id") grade_id: String
    ): Response<TeacherViewClassSectionMappingResponse>

    @GET(generalDiaryMessagesUrl)
    suspend fun getTeacherGeneralDiaryMessages(
        @Header(AUTHORIZATION) token: String,
        @Query("module_id") module_id: String?,
        @Query("start_date") start_date: String?,
        @Query("end_date") end_date: String?,
        @Query("page") page: String?,
        @Query("dairy_type") dairy_type: String?,
        @Query("branch") branch: String?,
        @Query("grades") grades: String?,
        @Query("sections") sections: String?,
        @Query("page_size") page_size: Int = 6,
    ): Response<GeneralDiaryResponse>

    @GET(generalDiaryMessagesUrl)
    suspend fun getTeacherDailyDiaryMessages(
        @Header(AUTHORIZATION) token: String,
        @Query("module_id") module_id: String?,
        @Query("start_date") start_date: String?,
        @Query("end_date") end_date: String?,
        @Query("page") page: String?,
        @Query("dairy_type") dairy_type: String?,
        @Query("branch") branch: String?,
        @Query("grades") grades: String?,
        @Query("sections") sections: String?,
        @Query("page_size") page_size: Int = 6,
    ): Response<DailyDiaryResponse>

    @DELETE(deleteDiaryUrl)
    suspend fun deleteTeacherDiary(
        @Header(AUTHORIZATION) token: String,
        @Path("diary_id") diary_id: String?
    ): Response<JsonElement>
}