package com.newletseduvate.api

import com.newletseduvate.model.lesson_plan.*
import com.newletseduvate.model.lesson_plan.teacher.TeacherLessonPlanCompletedRequest
import com.newletseduvate.model.lesson_plan.teacher.TeacherLessonPlanCompletedResponse
import com.newletseduvate.model.lesson_plan.teacher.TeacherLessonPlanFilterResultsResponse
import com.newletseduvate.model.lesson_plan.teacher.TeacherLessonPlanViewMoreResponse
import com.newletseduvate.utils.constants.ApiConstants
import retrofit2.Response
import retrofit2.http.*

interface LessonPlanService {

    @Headers("x-api-key: vikash@12345#1231")
    @GET(ApiConstants.lessonPlanAcademicYearUrl)
    suspend fun getLessonPlanAcademicYear(): Response<LessonPlanAcademicYearResponse>

    @Headers("x-api-key: vikash@12345#1231")
    @GET(ApiConstants.lessonPlanVolume)
    suspend fun getLessonPlanVolume(): Response<LessonPlanVolumeResponse>

    @GET(ApiConstants.lessonPlanBranch)
    suspend fun getLessonPlanBranch(
        @Header(ApiConstants.AUTHORIZATION) token: String,
        @Query ("module_id") module_id: Int): Response<LessonPlanBranchResponse>

    @GET(ApiConstants.lessonPlanGrade)
    suspend fun getLessonPlanGrade(
        @Header(ApiConstants.AUTHORIZATION) token: String,
        @Query("branch_id") branchId: String,
        @Query("module_id") moduleId: String
    ): Response<LessonPlanGradeResponse>

    @GET(ApiConstants.lessonPlanSubject)
    suspend fun getLessonPlanSubject(
        @Header(ApiConstants.AUTHORIZATION) token: String,
        @Query("branch") branch: String,
        @Query("grade") grade: String
    ): Response<LessonPlanSubjectResponse>

    @GET(ApiConstants.lessonPlanChapter)
    suspend fun getLessonPlanChapter(
        @Header(ApiConstants.AUTHORIZATION) token: String,
        @Query("gs_mapping_id") gsMappingId: String,
        @Query("volume") volume: String,
        @Query("academic_year") academicYear: String,
        @Query("branch") branch: String
    ): Response<LessonPlanChapterResponse>

    /* Teacher */
    /* Teacher Lesson Plan Filter Results */
    @Headers("x-api-key: vikash@12345#1231")
    @GET(ApiConstants.teacherLessonPlanFilterResults)
    suspend fun getTeacherLessonPlanFilterResults(
        @Query("chapter") chapter: Int,
        @Query("page_number") page_number: Int,
        @Query("page_size") page_size: Int
    ): Response<TeacherLessonPlanFilterResultsResponse>

    /* Teacher Lesson Plan View More Results */
    @Headers("x-api-key: vikash@12345#1231")
    @GET(ApiConstants.teacherLessonPlanViewMoreResults)
    suspend fun getTeacherLessonPlanViewMoreResults(
        @Query("lesson_plan_id") chapter: Int
    ): Response<TeacherLessonPlanViewMoreResponse>

    /* Teacher Lesson Plan Completed Results */
    @POST(ApiConstants.teacherLessonPlanCompleted)
    suspend fun getTeacherLessonPlanCompleted(
        @Header(ApiConstants.AUTHORIZATION) token: String,
        @Body teacherLessonPlanCompletedRequest: TeacherLessonPlanCompletedRequest
    ): Response<TeacherLessonPlanCompletedResponse>

}