package com.newletseduvate.api

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.newletseduvate.model.online_class.*
import com.newletseduvate.utils.constants.ApiConstants
import com.newletseduvate.utils.constants.ApiConstants.AUTHORIZATION
import com.newletseduvate.utils.constants.ApiConstants.createClassCheckTutorTimeUrl
import com.newletseduvate.utils.constants.ApiConstants.createClassOnlineClassStudentFilterUrl
import com.newletseduvate.utils.constants.ApiConstants.createClassOnlineOnlineErp
import com.newletseduvate.utils.constants.ApiConstants.createClassOnlineRecurringUrl
import com.newletseduvate.utils.constants.ApiConstants.createClassSubSectionUrl
import com.newletseduvate.utils.constants.ApiConstants.createClassTutorEmailUrl
import com.newletseduvate.utils.constants.ApiConstants.studentOnlineClassMarkAttendanceUrl
import com.newletseduvate.utils.constants.ApiConstants.studentOnlineClassOcDetailsUrl
import com.newletseduvate.utils.constants.ApiConstants.studentOnlineClassResourceFileUrl
import com.newletseduvate.utils.constants.ApiConstants.studentOnlineClassUrl
import com.newletseduvate.utils.constants.ApiConstants.teacherAcademicYearUrl
import com.newletseduvate.utils.constants.ApiConstants.teacherViewClassBranchUrl
import com.newletseduvate.utils.constants.ApiConstants.teacherViewClassCourseUrl
import com.newletseduvate.utils.constants.ApiConstants.teacherViewClassGradeMappingUrl
import com.newletseduvate.utils.constants.ApiConstants.teacherViewClassResourcesUrl
import com.newletseduvate.utils.constants.ApiConstants.teacherViewClassSectionMappingUrl
import com.newletseduvate.utils.constants.ApiConstants.teacherViewClassSubjectsUrl
import com.newletseduvate.utils.constants.ApiConstants.teacherViewClassTeacherDetailsCancelClassUrl
import com.newletseduvate.utils.constants.ApiConstants.teacherViewClassTeacherDetailsUrl
import com.newletseduvate.utils.constants.ApiConstants.teacherViewClassTeacherOnlineClassUrl
import com.newletseduvate.utils.constants.ApiConstants.viewCoursePlanUrl
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*

/**
 * Created by SHASHANK BHAT on 18-Feb-21.
 *
 *
 */
interface OnlineClassServices {

    @GET(studentOnlineClassUrl)
    suspend fun getStudentOnlineClass(
        @Header(AUTHORIZATION) token: String,
        @Query("user_id") userId: String,
        @Query("class_type") classType: String,
        @Query("page_number") pageNumber: String,
        @Query("page_size") page_size: Int = 15
    ): Response<StudentAttendOnlineClassResponse>

    @GET(studentOnlineClassOcDetailsUrl)
    suspend fun getStudentOnlineClassOcDetails(
        @Header(AUTHORIZATION) token: String,
        @Path("zoom_id") zoom_id: String,
    ): Response<OnlineClassStudentOcDetailsResponse>

    @PUT(studentOnlineClassMarkAttendanceUrl)
    suspend fun putMarkAttendance(
        @Header(AUTHORIZATION) token: String,
        @Body body: JsonObject
    ): Response<StudentAttendOnlineClassMarkAttendanceResponse>

    @GET(studentOnlineClassResourceFileUrl)
    suspend fun getStudentOnlineClassResourceFiles(
        @Header(AUTHORIZATION) token: String,
        @Query("online_class_id") online_class_id: String,
        @Query("class_date") class_date: String
    ): Response<JsonObject>

    @GET(viewCoursePlanUrl)
    suspend fun getViewCoursePlan(
        @Header(AUTHORIZATION) token: String,
        @Query("course_id") courseId: Int
    ): Response<ViewCoursePlanResponse>

    @GET(teacherAcademicYearUrl)
    suspend fun getAcademicYear(
        @Header(AUTHORIZATION) token: String
    ): Response<OnlineClassTeacherAcademicYearResponse>

    @GET(teacherViewClassBranchUrl)
    suspend fun getBranches(
        @Header(AUTHORIZATION) token: String,
        @Query("module_id") module_id: String,
        @Query("session_year") session_year: String
    ): Response<TeacherViewClassBranchResponse>

    @GET(teacherViewClassGradeMappingUrl)
    suspend fun getGradeMapping(
        @Header(AUTHORIZATION) token: String,
        @Query("module_id") module_id: String,
        @Query("session_year") session_year: String,
        @Query("branch_id") branch_id: String
    ): Response<TeacherViewClassGradeMappingResponse>

    @GET(teacherViewClassSectionMappingUrl)
    suspend fun getSectionMapping(
        @Header(AUTHORIZATION) token: String,
        @Query("module_id") module_id: String,
        @Query("session_year") session_year: String,
        @Query("branch_id") branch_id: String,
        @Query("grade_id") grade_id: String,

    ): Response<TeacherViewClassSectionMappingResponse>

    @GET(teacherViewClassSubjectsUrl)
    suspend fun getSubjectMapping(
        @Header(AUTHORIZATION) token: String,
        @Query("module_id") module_id: String,
        @Query("session_year") session_year: String,
        @Query("branch") branch_id: String,
        @Query("grade") grade_id: String,
        @Query("section") section: String
    ): Response<TeacherViewClassSubjectsResponse>

    @GET(teacherViewClassCourseUrl)
    suspend fun getCourses(
        @Header(AUTHORIZATION) token: String,
        @Query("grade") grade_id: String
    ): Response<TeacherViewClassOnlineCourseResponse>

    @GET
    suspend fun getTeacherOnlineClass(
        @Header(AUTHORIZATION) token: String,
        @Url url: String
    ): Response<TeacherViewClassResponse>

    @GET(teacherViewClassTeacherDetailsUrl)
    suspend fun getTeacherOnlineClassDetails(
        @Header(AUTHORIZATION) token: String,
        @Path("teacher_class_id") teacher_class_id: String,
    ): Response<TeacherViewClassDetailsResponse>

    @PUT(teacherViewClassTeacherDetailsCancelClassUrl)
    suspend fun putTeacherOnlineClassDetailsCancelClass(
        @Header(AUTHORIZATION) token: String,
        @Body body: JsonObject
    ): Response<JsonObject>

    @POST(ApiConstants.homeworkTeacherResourcesUploadUrl)
    @Multipart
    suspend fun postUploadResourceFile(
        @Header(AUTHORIZATION) token: String,
        @Part file: MultipartBody.Part,
        @Part ("online_class_id") online_class_id : String,
        @Part ("class_date") class_date : String,
        @Part ("description") description : String
    ): Response<JsonObject>

    @POST(ApiConstants.homeworkTeacherResourcesDeleteUrl)
    suspend fun postDeleteFile(
        @Header(AUTHORIZATION) token: String,
        @Body body: JsonObject
    ): Response<JsonObject>


    @GET(teacherViewClassResourcesUrl)
    suspend fun getTeacherOnlineResourcesFiles(
        @Header(AUTHORIZATION) token: String,
        @Query("online_class_id") online_class_id: String,
        @Query("class_date") class_date: String
    ): Response<OnlineClassTeacherResourceFilesResponse>

    @PUT(ApiConstants.teacherResourceUploadUrl)
    suspend fun putSubmitResourcesFile(
        @Header(AUTHORIZATION) token: String,
        @Body files : JsonObject
    ): Response<JsonObject>

    @GET(ApiConstants.teacherViewClassAttendanceUrl)
    suspend fun getAttendanceList(
        @Header(AUTHORIZATION) token: String,
        @Query("zoom_meeting_id") zoom_meeting_id: Int,
        @Query("class_date") class_date: String,
        @Query("type") type: String,
        @Query("page_number") page_number: Int,
        @Query("page_size") page_size: Int = 10
    ): Response<OnlineClassTeacherAttendanceListResponse>

    @PUT(ApiConstants.teacherViewClassMarkAttendanceUrl)
    suspend fun putMarkAttendanceTeacher(
        @Header(AUTHORIZATION) token: String,
        @Body files : JsonObject
    ): Response<JsonObject>

    @GET(createClassSubSectionUrl)
    suspend fun getSubSectionList(
        @Header(AUTHORIZATION) token: String,
        @Query("module_id") module_id: Int?,
        @Query("role") role: Int?,
        @Query("erp_id") erp_id: Int?,
        @Query("is_super") is_super: Int?,
        @Query("grade_id") grade_id: Int?,
        @Query("branch_id") branch_id: Int?,
        @Query("session_year") session_year: Int?
        ): Response<OnlineClassTeacherSubSectionResponse>

    @GET(createClassTutorEmailUrl)
    suspend fun getTutorEmailList(
        @Header(AUTHORIZATION) token: String,
        @Query("branch_id") branch_id: Int?,
        @Query("grade_id") grade_id: Int?,
        @Query("session_year") session_year: Int?
    ): Response<CreateClassTutorEmailListResponse>

    @GET(createClassCheckTutorTimeUrl)
    suspend fun getCheckTutorTime(
        @Header(AUTHORIZATION) token: String,
        @Query("tutor_email") tutor_email: String,
        @Query("start_time") start_time: String,
        @Query("duration") duration: Int?
    ): Response<JsonObject>

    @GET(createClassOnlineClassStudentFilterUrl)
    suspend fun getStudentFilter(
        @Header(AUTHORIZATION) token: String,
        @Query("section_mapping_ids") section_mapping_ids: Int?,
        @Query("subject_ids") subject_ids: Int?,
        @Query("branch_ids") branch_ids: Int?,
        @Query("grade_ids") grade_ids: Int?,
    ): Response<CreateClassStudentFilterResponse>

    @POST(createClassOnlineRecurringUrl)
    suspend fun postCreateClassOnlineRecurring(
        @Header(AUTHORIZATION) token: String,
        @Body createClassRequest: CreateClassRequest
    ): Response<JsonObject>

    @POST(createClassOnlineOnlineErp)
    suspend fun postCreateClassOnlineErpClass(
        @Header(AUTHORIZATION) token: String,
        @Body createClassRequest: CreateClassRequest
    ): Response<JsonObject>

}