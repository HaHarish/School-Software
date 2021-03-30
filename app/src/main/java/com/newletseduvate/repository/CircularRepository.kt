package com.newletseduvate.repository

import android.content.SharedPreferences
import com.newletseduvate.api.CircularService
import com.newletseduvate.model.circular.teacher.*
import com.newletseduvate.model.general.UploadFileModel
import com.newletseduvate.utils.constants.Constants
import com.newletseduvate.utils.extensions.getErpUserId
import com.newletseduvate.utils.extensions.getToken
import com.newletseduvate.utils.extensions.getUserIdNew
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CircularRepository @Inject constructor(private val apiService: CircularService,
                                            private val sharedPreferences: SharedPreferences){

    /* Circular Filter Response */
    suspend fun circularFilterResponse(fromDate: Long, toDate: Long, currentPage: String,
                                        totalPage: String, roleId: Int, moduleId: Int,
                                        moduleName: String): Response<CircularFilterResponse> {

        val requestDateFormat = SimpleDateFormat(Constants.DateFormat.YYYYMMDD)

        return apiService.getCircularFilterResponse(sharedPreferences.getToken()!!,
                                                    sharedPreferences.getUserIdNew().toString(),
                                                    requestDateFormat.format(Date(fromDate)),
                                                    requestDateFormat.format(Date(toDate)),
                                                    currentPage,
                                                    totalPage,
                                                    roleId,
                                                    moduleId,
                                                    moduleName)
    }

    /* Circular Academic Year Response */
    suspend fun circularAcademicYear(): Response<CircularAcademicYearResponse> {
        return apiService.getCircularAcademicYear(sharedPreferences.getToken()!!)
    }

    /* Circular Branch Response */
    suspend fun circularBranch(academicYearId: Int, moduelId: Int): Response<CircularBranchResponse> {
        return apiService.getCircularBranch(sharedPreferences.getToken()!!,
                                academicYearId,
                                moduelId)
    }

    /* Circular Grade Response */
    suspend fun circularGrade(academicYearId: Int, moduelId: Int,
    branchId: Int): Response<CircularGradeResponse> {
        return apiService.getCircularGrade(sharedPreferences.getToken()!!,
            academicYearId, moduelId, branchId)
    }

    /* Circular Section Response */
    suspend fun circularSection(academicYearId: Int, moduelId: Int,
                              branchId: Int, gradeId: Int): Response<CircularSectionResponse> {
        return apiService.getCircularSection(sharedPreferences.getToken()!!,
            academicYearId, moduelId, branchId, gradeId)
    }

    /* Circular Teacher Filter Results Response */
    suspend fun circularTeacherFilterResults(is_superuser: Boolean,
                                             branch: Int,
                                             grade: Int,
                                             section: Int,
                                             academic_year: Int,
                                             start_date: Long,
                                             end_date: Long,
                                             page: String,
                                             page_size: String): Response<CircularTeacherFilterResponse> {

        val requestDateFormat = SimpleDateFormat(Constants.DateFormat.YYYYMMDD)

        return apiService.getCircularTeacherFilterResults(sharedPreferences.getToken()!!,
                                                            is_superuser,
                                                            branch,
                                                            grade,
                                                            section,
                                                            academic_year,
                                requestDateFormat.format(Date(start_date)),
                                    requestDateFormat.format(Date(end_date)),
                                                            page,
                                                            page_size)
    }

    /* Circular Teacher Create */
    suspend fun circularTeacherCreate(circularCreateRequest: CircularCreateRequest): Response<CircularCreateResponse> {
        return apiService.createTeacherCircular(sharedPreferences.getToken()!!,circularCreateRequest)
    }

    /* Circular Teacher Delete */
    suspend fun circularTeacherDelete(token: String,
                                      circularTeacherDeleteRequest: CircularTeacherDeleteRequest):
            Response<CircularTeacherDeleteResponse> {
        return apiService.deleteTeacherCircular(token, circularTeacherDeleteRequest)
    }

    /* Circular Teacher Upload File */
    suspend fun circularTeacherUploadFile(token: String,
                                          branch: String?,
                                          file: UploadFileModel
    ): Response<CircularTeacherUploadFileResponse> {

        val requestFile = file.file
            .asRequestBody("multipart/form-data".toMediaTypeOrNull())

        return apiService.teacherCircularUploadFile(token,
            RequestBody.create("text/plain".toMediaTypeOrNull(), branch!!),
            MultipartBody.Part.createFormData(
                "file",
                "photo-"+System.currentTimeMillis().toString()+ file.file.name,
                requestFile
            ))
    }

    /* Circular Teacher Delete Uploaded file */
    suspend fun circularTeacherDeleteUploadedFile(token: String,
                                      fileName: String?): Response<CircularTeacherDeleteFileResponse> {
        return apiService.teacherCircularDeleteFile(token, CircularTeacherDeleteFileRequest(fileName!!))
    }

}