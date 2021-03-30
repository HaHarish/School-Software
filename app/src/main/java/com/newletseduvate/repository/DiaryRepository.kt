package com.newletseduvate.repository

import android.content.SharedPreferences
import com.google.gson.JsonElement
import com.newletseduvate.model.diary.DailyDiaryResponse
import com.newletseduvate.model.diary.GeneralDiaryResponse
import com.newletseduvate.api.DiaryServices
import com.newletseduvate.model.online_class.OnlineClassTeacherAcademicYearResponse
import com.newletseduvate.model.online_class.TeacherViewClassBranchResponse
import com.newletseduvate.model.online_class.TeacherViewClassGradeMappingResponse
import com.newletseduvate.model.online_class.TeacherViewClassSectionMappingResponse
import com.newletseduvate.model.diary.teacher.*
import com.newletseduvate.model.general.UploadFileModel
import com.newletseduvate.utils.constants.Constants
import com.newletseduvate.utils.extensions.getToken
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by SHASHANK BHAT on 17-Feb-21.
 *
 *
 */

@Singleton
class DiaryRepository @Inject constructor(
    private val apiService: DiaryServices,
    private val pref: SharedPreferences
) {

    suspend fun getGeneralDiaryMessages(
        moduleId: String,
        startDate: Long,
        toDate: Long,
        page: Int
    ): Response<GeneralDiaryResponse> {

        val requestDateFormat = SimpleDateFormat(Constants.DateFormat.YYYYMMDD)

        return apiService.getGeneralDiaryMessages(
            pref.getToken().toString(),
            moduleId,
            requestDateFormat.format(Date(startDate)),
            requestDateFormat.format(Date(toDate)),
            page.toString(),
            "1"
        )
    }

    suspend fun getDailyDiaryMessages(
        moduleId: String,
        startDate: Long,
        toDate: Long,
        page: Int
    ): Response<DailyDiaryResponse> {

        val requestDateFormat = SimpleDateFormat(Constants.DateFormat.YYYYMMDD)

        return apiService.getDailyDiaryMessages(
            pref.getToken().toString(),
            moduleId,
            requestDateFormat.format(Date(startDate)),
            requestDateFormat.format(Date(toDate)),
            page.toString(),
            "2"
        )
    }

    suspend fun getTeacherDiaryActiveStudentsList(
        academic_year: Int,
        active: Int,
        page: Int,
        page_size: Int,
        bgs_mapping: Int,
        module_id: Int
    ): Response<TeacherDiaryActiveStudentsResponse> {

        return apiService.getTeacherDiaryActiveStudents(
            pref.getToken().toString(),
            academic_year,
            active,
            page,
            page_size,
            bgs_mapping,
            module_id
        )
    }

    /* Circular Teacher Upload File */
    suspend fun teacherDiaryFileUpload(token: String, branch: String?, grade: String?,
                                       section: String?,
                                       file: UploadFileModel
    ): Response<TeacherDiaryFileUploadResponse> {

        val requestFile = file.file
            .asRequestBody("multipart/form-data".toMediaTypeOrNull())

        return apiService.teacherDiaryFileUpload(token,
            RequestBody.create("text/plain".toMediaTypeOrNull(), branch!!),
            RequestBody.create("text/plain".toMediaTypeOrNull(), grade!!),
            RequestBody.create("text/plain".toMediaTypeOrNull(), section!!),
            MultipartBody.Part.createFormData(
                "file",
                "photo-"+System.currentTimeMillis().toString()+ file.file.name,
                requestFile
            ))
    }

    /* Diary Chapter Response */
    suspend fun teacherDiaryChapter(academicYearId: Int, subjectId: Int,
                                    moduelId: Int): Response<TeacherDiaryChapterResponse> {
        return apiService.teacherDiaryChapters(pref.getToken()!!,
            academicYearId, subjectId, moduelId)
    }

    /* Diary Subjects Response */
    suspend fun teacherDiarySubject(academicYearId: Int, branchId: Int, gradeId: Int,
                                    sectionId: Int,
                                    moduelId: Int): Response<TeacherDiarySubjectResponse> {
        return apiService.teacherDiarySubjects(pref.getToken()!!,
            academicYearId, branchId, gradeId, sectionId, moduelId)
    }

    /* Diary Branch Response */
    suspend fun diaryBranch(academicYearId: Int, moduelId: Int): Response<TeacherDiaryBranchResponse> {
        return apiService.getDiaryBranch(pref.getToken()!!,
            academicYearId,
            moduelId)
    }

    /* Teacher Diary Create */
    suspend fun teacherDiaryCreate(token: String, teacherDiaryCreateRequest: TeacherDiaryCreateRequest
    ): Response<TeacherDiaryCreateResponse> {
        return apiService.teacherDiaryCreate(token, teacherDiaryCreateRequest)
    }

    /* Teacher Diary Daily Create */
    suspend fun teacherDiaryCreateDaily(token: String, teacherDiaryCreateDailyRequest: TeacherDiaryCreateDailyRequest
    ): Response<TeacherDiaryCreateDailyResponse> {
        return apiService.teacherDiaryCreateDaily(token, teacherDiaryCreateDailyRequest)
    }

    /* Teacher Diary Create */
    suspend fun teacherDiary(token: String, teacherDiaryCreateRequest: TeacherDiaryCreateRequest
    ): Response<TeacherDiaryCreateResponse> {
        return apiService.teacherDiaryCreate(token, teacherDiaryCreateRequest)
    }


    suspend fun getAcademicYear(): Response<OnlineClassTeacherAcademicYearResponse> {
        return apiService.getAcademicYear(
            pref.getToken()!!
        )
    }

    suspend fun getBranches(moduleId: Int, sessionYear: Int): Response<TeacherViewClassBranchResponse> {
        return apiService.getBranches(
            pref.getToken()!!,
            moduleId.toString(),
            sessionYear.toString()
        )
    }

    suspend fun getGradeMapping(
        module_id: String,
        session_year: String,
        branch_id: String
    ): Response<TeacherViewClassGradeMappingResponse> {
        return apiService.getGradeMapping(
            pref.getToken()!!,
            module_id,
            session_year,
            branch_id
        )
    }

    suspend fun getSectionMapping(
        module_id: String,
        session_year: String,
        branch_id: String,
        grade_id: String
    ): Response<TeacherViewClassSectionMappingResponse> {
        return apiService.getSectionMapping(
            pref.getToken()!!,
            module_id,
            session_year,
            branch_id,
            grade_id
        )
    }


    suspend fun getTeacherGeneralDiaryMessages(
        moduleId: String,
        startDate: Long,
        toDate: Long,
        page: Int,
        branch: String?,
        grades: String?,
        sections: String?,
    ): Response<GeneralDiaryResponse> {

        val requestDateFormat = SimpleDateFormat(Constants.DateFormat.YYYYMMDD)

        return apiService.getTeacherGeneralDiaryMessages(
            pref.getToken().toString(),
            moduleId,
            requestDateFormat.format(Date(startDate)),
            requestDateFormat.format(Date(toDate)),
            page.toString(),
            "1",
            branch,
            grades,
            sections
        )
    }

    suspend fun getTeacherDailyDiaryMessages(
        moduleId: String,
        startDate: Long,
        toDate: Long,
        page: Int,
        branch: String?,
        grades: String?,
        sections: String?,
    ): Response<DailyDiaryResponse> {

        val requestDateFormat = SimpleDateFormat(Constants.DateFormat.YYYYMMDD)

        return apiService.getTeacherDailyDiaryMessages(
            pref.getToken().toString(),
            moduleId,
            requestDateFormat.format(Date(startDate)),
            requestDateFormat.format(Date(toDate)),
            page.toString(),
            "2",
            branch,
            grades,
            sections
        )
    }

    suspend fun deleteTeacherDiary(
        diary_id: String?
    ): Response<JsonElement>{
        return apiService.deleteTeacherDiary(
            pref.getToken().toString(),
            diary_id
        )
    }
}