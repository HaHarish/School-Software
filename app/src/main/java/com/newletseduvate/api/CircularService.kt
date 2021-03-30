package com.newletseduvate.api

import com.newletseduvate.model.blog.StudentBlogCreateResponse
import com.newletseduvate.model.circular.teacher.*
import com.newletseduvate.utils.constants.ApiConstants
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface CircularService {

    @GET(ApiConstants.circularFilterResponse)
    suspend fun getCircularFilterResponse(
        @Header(ApiConstants.AUTHORIZATION) token: String,
        @Query("user_id") userId: String,
        @Query("start_date") startDate: String,
        @Query("end_date") endDate: String,
        @Query("page") page: String,
        @Query("page_size") pageSize: String,
        @Query("role_id") roleId: Int,
        @Query("module_id") moduleId: Int,
        @Query("module_name") moduleName: String
    ): Response<CircularFilterResponse>

    @GET(ApiConstants.circularAcademicYear)
    suspend fun getCircularAcademicYear(
        @Header(ApiConstants.AUTHORIZATION) token: String
    ): Response<CircularAcademicYearResponse>

    @GET(ApiConstants.circularBranch)
    suspend fun getCircularBranch(
        @Header(ApiConstants.AUTHORIZATION) token: String,
        @Query ("session_year") session_year: Int,
        @Query ("module_id") module_id: Int
    ): Response<CircularBranchResponse>

    @GET(ApiConstants.circularGrade)
    suspend fun getCircularGrade(
        @Header(ApiConstants.AUTHORIZATION) token: String,
        @Query ("session_year") session_year: Int,
        @Query ("module_id") module_id: Int,
        @Query ("branch_id") branch_id: Int
    ): Response<CircularGradeResponse>

    @GET(ApiConstants.circularSection)
    suspend fun getCircularSection(
        @Header(ApiConstants.AUTHORIZATION) token: String,
        @Query ("session_year") session_year: Int,
        @Query ("module_id") module_id: Int,
        @Query ("branch_id") branch_id: Int,
        @Query ("grade_id") grade_id: Int
    ): Response<CircularSectionResponse>

    @GET(ApiConstants.circularTeacherFilterResponse)
    suspend fun getCircularTeacherFilterResults(
        @Header(ApiConstants.AUTHORIZATION) token: String,
        @Query ("is_superuser") is_superuser: Boolean,
        @Query ("branch") branch: Int,
        @Query ("grade") grade: Int,
        @Query ("section") section: Int,
        @Query ("academic_year") academic_year: Int,
        @Query ("start_date") start_date: String,
        @Query ("end_date") end_date: String,
        @Query ("page") page: String,
        @Query ("page_size") page_size: String
    ): Response<CircularTeacherFilterResponse>

    @POST(ApiConstants.teacherCircularCreate)
    suspend fun createTeacherCircular(
        @Header(ApiConstants.AUTHORIZATION) token: String,
        @Body createTeacherRequest: CircularCreateRequest
    ): Response<CircularCreateResponse>

    @PUT(ApiConstants.teacherCircularDelete)
    suspend fun deleteTeacherCircular(
        @Header(ApiConstants.AUTHORIZATION) token: String,
        @Body circularTeacherDeleteRequest: CircularTeacherDeleteRequest
    ): Response<CircularTeacherDeleteResponse>

    @POST(ApiConstants.teacherCircularDelete)
    suspend fun teacherCircularUploadFiles(
        @Header(ApiConstants.AUTHORIZATION) token: String,
        @Body circularTeacherDeleteRequest: CircularTeacherDeleteRequest
    ): Response<CircularTeacherDeleteResponse>

    @POST(ApiConstants.teacherCircularUploadFiles)
    @Multipart
    suspend fun teacherCircularUploadFile(
        @Header(ApiConstants.AUTHORIZATION) token: String,
        @Part ("branch") branch: RequestBody,
        @Part file: MultipartBody.Part
    ): Response<CircularTeacherUploadFileResponse>

    @POST(ApiConstants.teacherCircularDeleteFiles)
    suspend fun teacherCircularDeleteFile(
        @Header(ApiConstants.AUTHORIZATION) token: String,
        @Body circularTeacherDeleteRequest: CircularTeacherDeleteFileRequest
    ): Response<CircularTeacherDeleteFileResponse>

}