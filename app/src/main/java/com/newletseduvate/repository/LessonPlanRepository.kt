package com.newletseduvate.repository

import android.content.SharedPreferences
import android.util.Log
import com.newletseduvate.api.HomeWorkService
import com.newletseduvate.model.lesson_plan.*
import com.newletseduvate.api.LessonPlanService
import com.newletseduvate.api.LoginServices
import com.newletseduvate.model.lesson_plan.teacher.TeacherLessonPlanCompletedRequest
import com.newletseduvate.model.lesson_plan.teacher.TeacherLessonPlanCompletedResponse
import com.newletseduvate.model.lesson_plan.teacher.TeacherLessonPlanFilterResultsResponse
import com.newletseduvate.model.lesson_plan.teacher.TeacherLessonPlanViewMoreResponse
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LessonPlanRepository @Inject constructor(private val apiService: LessonPlanService) {

    /* Student - Academic Year */
    suspend fun getLessonPlanAcademicYear(): Response<LessonPlanAcademicYearResponse> {
        return apiService.getLessonPlanAcademicYear()
    }

    /* Volume */
    suspend fun getLessonPlanVolume(): Response<LessonPlanVolumeResponse> {
        return apiService.getLessonPlanVolume()
    }

    /* Branch */
    suspend fun getLessonPlanBranch(token: String, moduleId: Int): Response<LessonPlanBranchResponse> {
        return apiService.getLessonPlanBranch(token, moduleId)
    }

    /* Grade */
    suspend fun getLessonPlanGrade(token: String,
                                    branchId: String,
                                    moduleId: String): Response<LessonPlanGradeResponse> {
        return apiService.getLessonPlanGrade(token, branchId, moduleId)
    }

    /* Subject */
    suspend fun getLessonPlanSubject(token: String,
                                   branch: String,
                                   grade: String): Response<LessonPlanSubjectResponse> {
        return apiService.getLessonPlanSubject(token, branch, grade)
    }

    /* Chapter */
    suspend fun getLessonPlanChapter(token: String,
                                     gsMappingId: String,
                                     volume: String,
                                     academicYear: String,
                                     branch: String): Response<LessonPlanChapterResponse> {
        return apiService.getLessonPlanChapter(token, gsMappingId, volume, academicYear, branch)
    }

    /* Teacher Lesson Plan Results */
    suspend fun getTeacherLessonPlanFilterResults(
                                                  chapter: Int,
                                                  page_number: Int,
                                                  page_size: Int): Response<TeacherLessonPlanFilterResultsResponse> {
        return apiService.getTeacherLessonPlanFilterResults( chapter, page_number, page_size)
    }

    /* Teacher Lesson Plan View More */
    suspend fun getTeacherLessonPlanViewMoreResults(lesson_plan_id: Int): Response<TeacherLessonPlanViewMoreResponse> {
        return apiService.getTeacherLessonPlanViewMoreResults(lesson_plan_id)
    }

    /* Teacher Lesson Plan View More */
    suspend fun getTeacherLessonPlanCompletedResult(token: String,
                                                    teacherLessonPlanCompletedRequest: TeacherLessonPlanCompletedRequest):
                                                    Response<TeacherLessonPlanCompletedResponse> {
        return apiService.getTeacherLessonPlanCompleted(token, teacherLessonPlanCompletedRequest)
    }

}