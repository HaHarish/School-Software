package com.newletseduvate.repository

import android.content.SharedPreferences
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.newletseduvate.model.online_class.*
import com.newletseduvate.api.OnlineClassServices
import com.newletseduvate.model.general.UploadFileModel
import com.newletseduvate.utils.constants.ApiConstants
import com.newletseduvate.utils.constants.Constants
import com.newletseduvate.utils.extensions.getErpUserId
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

/**
 * Created by SHASHANK BHAT on 18-Feb-21.
 *
 *
 */
@Singleton
class OnlineClassRepository @Inject constructor(
    private val apiService: OnlineClassServices,
    private val pref: SharedPreferences
) {

    suspend fun getStudentOnlineClass(
        classType: Int,
        pageNumber: Int?
    ): Response<StudentAttendOnlineClassResponse> {
        return apiService.getStudentOnlineClass(
            pref.getToken().toString(),
            pref.getErpUserId().toString(),
            classType.toString(),
            pageNumber.toString()
        )
    }

    suspend fun getStudentOnlineClassOcDetails(zoomId: String): Response<OnlineClassStudentOcDetailsResponse> {
        return apiService.getStudentOnlineClassOcDetails(
            pref.getToken().toString(),
            zoomId
        )
    }

    suspend fun getStudentOnlineClassResourceFiles(
        onlineClassId: String,
        classDate: String
    ): Response<JsonObject> {
        return apiService.getStudentOnlineClassResourceFiles(
            pref.getToken().toString(),
            onlineClassId,
            classDate
        )
    }

    suspend fun putMarkAttendance(
        class_date: String,
        acceptedOrRejected: String,
        zoom_meeting_id: String
    ): Response<StudentAttendOnlineClassMarkAttendanceResponse> {
        val body = JsonObject()
        body.addProperty("class_date", class_date)
        body.addProperty(acceptedOrRejected, true)
        body.addProperty("zoom_meeting_id", zoom_meeting_id)
        return apiService.putMarkAttendance(
            pref.getToken().toString(),
            body
        )
    }

    suspend fun getViewCoursePlan(courseId: Int): Response<ViewCoursePlanResponse> {
        return apiService.getViewCoursePlan(
            pref.getToken()!!,
            courseId
        )
    }

    suspend fun getAcademicYear(): Response<OnlineClassTeacherAcademicYearResponse> {
        return apiService.getAcademicYear(
            pref.getToken()!!
        )
    }

    suspend fun getBranches(module_id: Int, session_year: Int): Response<TeacherViewClassBranchResponse> {
        return apiService.getBranches(
            pref.getToken()!!,
            module_id.toString(),
            session_year.toString()
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

    suspend fun getSubjectMapping(
        module_id: String,
        session_year: String,
        branch_id: String,
        grade_id: String,
        section: String
    ): Response<TeacherViewClassSubjectsResponse> {
        return apiService.getSubjectMapping(
            pref.getToken()!!,
            module_id,
            session_year,
            branch_id,
            grade_id,
            section
        )
    }

    suspend fun getCourses(
        grade_id: String
    ): Response<TeacherViewClassOnlineCourseResponse> {
        return apiService.getCourses(
            pref.getToken()!!,
            grade_id
        )
    }


    suspend fun getTeacherOnlineClass(
        session_year: String,
        section_mapping_ids: String,
        subject_id: String,
        class_type: String,
        start_date: Long,
        end_date: Long,
        module_id: String,
        page_number: Int,
        courses_id: String
    ): Response<TeacherViewClassResponse> {

        val requestDateFormat = SimpleDateFormat(Constants.DateFormat.YYYYMMDD)

        val startDate = requestDateFormat.format(Date(start_date))
        val endDate = requestDateFormat.format(Date(end_date))

        var stringUrl =
            ApiConstants.teacherViewClassTeacherOnlineClassUrl + "?session_year=$session_year" +
                    "&section_mapping_ids=$section_mapping_ids" +
                    "&class_type=$class_type" +
                    "&start_date=$startDate" +
                    "&end_date=$endDate" +
                    "&module_id=$module_id" +
                    "&page_number=$page_number" +
                    "&page_size=15" +
                    "&is_aol=0"

        if (class_type != "0") {
            stringUrl += "&course_id=$courses_id"
        } else
            stringUrl += "&subject_id=$subject_id"

        return apiService.getTeacherOnlineClass(
            pref.getToken()!!,
            stringUrl
        )
    }


    suspend fun getTeacherOnlineClassDetails(
        teacherClassId: String
    ): Response<TeacherViewClassDetailsResponse> {
        return apiService.getTeacherOnlineClassDetails(
            pref.getToken().toString(),
            teacherClassId
        )
    }

    suspend fun putTeacherOnlineClassDetailsCancelClass(
        class_date: String,
        zoom_meeting_id: Int
    ): Response<JsonObject> {

        val body = JsonObject()
        body.addProperty("class_date", class_date)
        body.addProperty("zoom_meeting_id", zoom_meeting_id)

        return apiService.putTeacherOnlineClassDetailsCancelClass(
            pref.getToken().toString(), body
        )
    }

    suspend fun postUploadResourceFile(
        file: UploadFileModel,
        online_class_id: String,
        class_date: String
    ): Response<JsonObject> {

        val requestFile = file.file
            .asRequestBody("multipart/form-data".toMediaTypeOrNull())

        return apiService.postUploadResourceFile(
            pref.getToken().toString(),
            MultipartBody.Part.createFormData(
                "file",
                "photo-" + System.currentTimeMillis().toString() + file.file.name,
                requestFile
            ),
            online_class_id,
            class_date,
            "description123"
        )
    }

    suspend fun postDeleteFile(fileName: String): Response<JsonObject> {

        val body = JsonObject()
        body.addProperty("file_name", fileName)
        return apiService.postDeleteFile(
            pref.getToken().toString(),
            body
        )
    }

    suspend fun getTeacherOnlineResourcesFiles(
        online_class_id: String,
        class_date: String,
    ): Response<OnlineClassTeacherResourceFilesResponse>? {

        try {
            return apiService.getTeacherOnlineResourcesFiles(
                pref.getToken().toString(),
                online_class_id,
                class_date
            )
        } catch (ex: Exception) {
        }

        return null
    }

    suspend fun putSubmitResourcesFile(
        files: ArrayList<String>,
        online_class_id: String,
        class_date: String,
    ): Response<JsonObject> {

        val body = JsonObject()
        body.addProperty("online_class_id", online_class_id)
        body.addProperty("class_date", class_date)
        body.addProperty("description", "description123")

        val fileArray = JsonArray()
        files.forEach {
            fileArray.add(it)
        }

        body.add("files", fileArray)
        return apiService.putSubmitResourcesFile(
            pref.getToken().toString(),
            body
        )
    }


    suspend fun getAttendanceList(
        zoom_meeting_id: Int,
        class_date: Long,
        page_number: Int
    ): Response<OnlineClassTeacherAttendanceListResponse> {

        val requestDateFormat = SimpleDateFormat(Constants.DateFormat.YYYYMMDD)

        val date = requestDateFormat.format(class_date?.let { Date(it) })

        return apiService.getAttendanceList(
            pref.getToken().toString(),
            zoom_meeting_id,
            date,
            "json",
            page_number
        )
    }

    suspend fun putMarkAttendanceTeacher(
        class_date: Long?,
        is_attended: Boolean,
        zoom_meeting_id: Int
    ): Response<JsonObject> {
        val requestDateFormat = SimpleDateFormat(Constants.DateFormat.YYYYMMDD)

        val date = requestDateFormat.format(class_date?.let { Date(it) })

        val body = JsonObject()
        body.addProperty("class_date", date)
        body.addProperty("is_attended", is_attended)
        body.addProperty("zoom_meeting_id", zoom_meeting_id)
        return apiService.putMarkAttendanceTeacher(
            pref.getToken().toString(),
            body
        )
    }

    suspend fun getSubSectionList(
        module_id: Int?,
        role: Int?,
        is_super: Int?,
        grade_id: Int?,
        branch_id: Int?,
        session_year: Int?
    ): Response<OnlineClassTeacherSubSectionResponse> {
        return apiService.getSubSectionList(
            pref.getToken().toString(),
            module_id,
            role,
            pref.getErpUserId(),
            is_super,
            grade_id,
            branch_id,
            session_year
        )
    }

    suspend fun getTutorEmailList(
        branch_id: Int?,
        grade_id: Int?,
        session_year: Int?
    ): Response<CreateClassTutorEmailListResponse> {
        return apiService.getTutorEmailList(
            pref.getToken().toString(),
            branch_id,
            grade_id,
            session_year
        )
    }

    suspend fun getCheckTutorTime(
        tutor_email: String,
        start_time: String,
        duration: Int?
    ): Response<JsonObject>{
        return apiService.getCheckTutorTime(
            pref.getToken().toString(),
            tutor_email,
            start_time,
            duration
        )
    }

    suspend fun getStudentFilter(
       section_mapping_ids: Int?,
       subject_ids: Int?,
       branch_ids: Int?,
       grade_ids: Int?,
    ): Response<CreateClassStudentFilterResponse>{
        return apiService.getStudentFilter(
            pref.getToken().toString(),
            section_mapping_ids,
            subject_ids,
            branch_ids,
            grade_ids
        )
    }


    suspend fun postCreateClassOnlineRecurring(
        createClassRequest: CreateClassRequest
    ): Response<JsonObject>{
        return apiService.postCreateClassOnlineRecurring(
            pref.getToken().toString(),
            createClassRequest
        )
    }

    suspend fun postCreateClassOnlineErpClass(
        createClassRequest: CreateClassRequest
    ): Response<JsonObject>{
        return apiService.postCreateClassOnlineErpClass(
            pref.getToken().toString(),
            createClassRequest
        )
    }

}