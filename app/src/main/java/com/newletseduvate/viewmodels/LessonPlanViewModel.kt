package com.newletseduvate.viewmodels

import android.content.SharedPreferences
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableInt
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.newletseduvate.model.BottomSheetItem
import com.newletseduvate.utils.Resource
import com.newletseduvate.model.lesson_plan.LessonPlanAcademicYearResponse
import com.newletseduvate.model.lesson_plan.teacher.TeacherLessonPlanCompletedRequest
import com.newletseduvate.model.lesson_plan.teacher.TeacherLessonPlanCompletedResponse
import com.newletseduvate.model.lesson_plan.teacher.TeacherLessonPlanFilterResultsResponse
import com.newletseduvate.model.lesson_plan.teacher.TeacherLessonPlanViewMoreResponse
import com.newletseduvate.repository.LessonPlanRepository
import com.newletseduvate.utils.extensions.getToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class LessonPlanViewModel @Inject constructor(private val lessonPlanRepository: LessonPlanRepository,
                            private val sharedPreferences: SharedPreferences): ViewModel() {

    var academicYearList = MutableLiveData<ArrayList<BottomSheetItem>>()
    var academicYearListId = ObservableInt(-1)

    var volumeList = MutableLiveData<ArrayList<BottomSheetItem>>()
    var volumeListId = ObservableInt(-1)

    var branchList = MutableLiveData<ArrayList<BottomSheetItem>>()
    var branchListId = ObservableInt(-1)

    var gradeList = MutableLiveData<ArrayList<BottomSheetItem>>()
    var gradeListId = ObservableInt(-1)

    var subjectList = MutableLiveData<ArrayList<BottomSheetItem>>()
    var subjectListId = ObservableInt(-1)

    var chapterList = MutableLiveData<ArrayList<BottomSheetItem>>()
    var chapterListId = ObservableInt(-1)

    val academicYearData = MutableLiveData<Resource<LessonPlanAcademicYearResponse>>()
    val teacherLessonPlanFilterResultsResponse = MutableLiveData<Resource<ArrayList<TeacherLessonPlanFilterResultsResponse.Result>>>()
    val teacherLessonPlanViewMoreResponse = MutableLiveData<Resource<TeacherLessonPlanViewMoreResponse>>()
    val teacherLessonPlanCompletedResponse = MutableLiveData<Resource<TeacherLessonPlanCompletedResponse>>()

    var currentPageStudent = MutableLiveData(1)
    val isPageLoadingStudent = MutableLiveData(true)
    var isNoDataStudent = ObservableBoolean(false)
    val totalPageStudent = ObservableInt(0)
    var isDataLoadingStudent = MutableLiveData(false)

    var moduelId: Int = 0

    var academicName: String? = null
    var academicYearId: Int = 0

    var volumeName: String? = null
    var volumeId = 0

    var branchName: String? = null
    var branchId = 0

    var gradeName: String? = null
    var gradeId = 0

    var subjectName: String? = null
    var subjectId = 0

    var chapterId= 0
    var chapterTitle: String? = null
    var chapterName: String? = null
    var lessonClickValue = 0

    var centralGSMappingId: Int = 0

    /* Academic Year*/
    fun getLessonPlanAcademicYear(){
        GlobalScope.launch {
            val response = lessonPlanRepository.getLessonPlanAcademicYear()
            withContext(Dispatchers.Main){
                when(response.body()?.statusCode){
                    200 -> {
                        if(response.body()?.statusCode == 200){
                            val academicYears = ArrayList<BottomSheetItem>()
                            response.body()?.result?.results?.forEach {
                                academicYears.add(BottomSheetItem(it?.id, it?.sessionYear))
                            }
                            academicYearList.value = academicYears
                        }
                    }
                    401 -> academicYearData.value = response.body()?.message?.let { Resource.error(it) }
                    else -> academicYearData.value = Resource.error("Something went wrong")
                }
            }
        }
    }

    fun getLessonPlanVolume(){
        GlobalScope.launch {
            val response = lessonPlanRepository.getLessonPlanVolume()
            withContext(Dispatchers.Main){
                when(response.body()?.statusCode){
                    200 -> {
                        if(response.body()?.statusCode == 200){
                            val volumes = ArrayList<BottomSheetItem>()
                            response.body()?.result?.results?.forEach {
                                volumes.add(BottomSheetItem(it?.id, it?.volumeName))
                            }
                            volumeList.value = volumes
                        }
                    }
                    401 -> academicYearData.value = response.body()?.message?.let { Resource.error(it) }
                    else -> academicYearData.value = Resource.error("Something went wrong")
                }
            }
        }
    }

    fun getLessonPlanBranch(){
        GlobalScope.launch {
            val response = lessonPlanRepository
                .getLessonPlanBranch(sharedPreferences.getToken()!!, moduelId)
            withContext(Dispatchers.Main){
                when(response.body()?.statusCode){
                    200 -> {
                        if(response.body()?.statusCode == 200){
                            val branches = ArrayList<BottomSheetItem>()
                            if(response.body()?.data?.results?.size != 0){
                                branches.add(BottomSheetItem(response.body()?.data?.results?.get(0)?.branch?.id,
                                    response.body()?.data?.results?.get(0)?.branch?.branchName))
                            }
                            branchList.value = branches
                        }
                    }
                    401 -> academicYearData.value = response.body()?.message?.let { Resource.error(it) }
                    else -> academicYearData.value = Resource.error("Something went wrong")
                }
            }
        }
    }

    fun getLessonPlanGrade(){
        GlobalScope.launch {
            val response = lessonPlanRepository
                .getLessonPlanGrade(sharedPreferences.getToken()!!, branchId.toString(), moduelId.toString())
            withContext(Dispatchers.Main){
                when(response.body()?.statusCode){
                    200 -> {
                        if(response.body()?.statusCode == 200){
                            val grades = ArrayList<BottomSheetItem>()
                            response.body()?.data?.forEach {
                                grades.add(BottomSheetItem(it?.gradeId, it?.gradeGradeName))
                            }
                            gradeList.value = grades
                        }
                    }
                    401 -> academicYearData.value = response.body()?.message?.let { Resource.error(it) }
                    else -> academicYearData.value = Resource.error("Something went wrong")
                }
            }
        }
    }

    fun getLessonPlanSubject(){
        GlobalScope.launch {
            val response = lessonPlanRepository
                .getLessonPlanSubject(sharedPreferences.getToken()!!, branchId.toString(), gradeId.toString())
            withContext(Dispatchers.Main){
                when(response.body()?.statusCode){
                    200 -> {
                        if(response.body()?.statusCode == 200){
                            val subjects = ArrayList<BottomSheetItem>()
                            response.body()?.result?.forEach {
                                subjects.add(BottomSheetItem(it?.id, it?.subjectName))
                            }
                            subjectList.value = subjects
                        }
                    }
                    401 -> academicYearData.value = response.body()?.message?.let { Resource.error(it) }
                    else -> academicYearData.value = Resource.error("Something went wrong")
                }
            }
        }
    }

    fun getLessonPlanChapter(){
        GlobalScope.launch {
            val response = lessonPlanRepository
                .getLessonPlanChapter(sharedPreferences.getToken()!!,
                    subjectId.toString(),
                    volumeId.toString(),
                    academicYearId.toString(),
                    branchId.toString())
            withContext(Dispatchers.Main){
                when(response.body()?.statusCode){
                    200 -> {
                        if(response.body()?.statusCode == 200){
                            val chapters = ArrayList<BottomSheetItem>()
                            response.body()?.result?.chapterList?.forEach {
                                chapters.add(BottomSheetItem(it?.id, it?.chapterName))
                            }
                            chapterList.value = chapters

                            centralGSMappingId = response.body()?.result?.centralGsMappingId!!
                        }
                    }
                    401 -> academicYearData.value = response.body()?.message?.let { Resource.error(it) }
                    else -> academicYearData.value = Resource.error("Something went wrong")
                }
            }
        }
    }

    /* Teacher Lesson Plan Filter Results */
    fun getTeacherLessonPlanFilterResults(){
        GlobalScope.launch {
            val response = lessonPlanRepository.getTeacherLessonPlanFilterResults(
                chapterId,
                currentPageStudent.value!!,
                totalPageStudent.get())
            withContext(Dispatchers.Main){
                when(response.body()?.statusCode){
                    200 -> {
                        response.body()?.result?.forEach {
                            it.chapterName = chapterName
                        }
                        teacherLessonPlanFilterResultsResponse.value = Resource.success(response.body()?.result!!)
                    }
                    401 -> teacherLessonPlanFilterResultsResponse.value = response.body()?.message?.let { Resource.error(it) }
                    else -> teacherLessonPlanFilterResultsResponse.value = Resource.error("Something went wrong")
                }
            }
        }
    }

    fun getTeacherLessonPlanViewMoreResults(){
        GlobalScope.launch {
            val response = lessonPlanRepository.getTeacherLessonPlanViewMoreResults(lessonClickValue)
            withContext(Dispatchers.Main){
                when(response.body()?.statusCode){
                    200 -> {
                        teacherLessonPlanViewMoreResponse.value = Resource.success(response.body())
                    }
                    401 -> teacherLessonPlanViewMoreResponse.value = response.body()?.message?.let { Resource.error(it) }
                    else -> teacherLessonPlanViewMoreResponse.value = Resource.error("Something went wrong")
                }
            }
        }
    }

    fun getTeacherLessonPlanCompletedResult(){
        GlobalScope.launch {
            val response = lessonPlanRepository.getTeacherLessonPlanCompletedResult(
                sharedPreferences.getToken()!!,
                TeacherLessonPlanCompletedRequest(
                    academicName,
                    academicYearId,
                    volumeId,
                    volumeName,
                    subjectId,
                    chapterId,
                    chapterName,
                    centralGSMappingId,
                    lessonClickValue
                )
            )
            withContext(Dispatchers.Main){
                when(response.body()?.statusCode){
                    200 -> {
                        teacherLessonPlanCompletedResponse.value = Resource.success(response.body())
                    }
                    401 -> teacherLessonPlanCompletedResponse.value = response.body()?.message?.let { Resource.error(it) }
                    else -> teacherLessonPlanCompletedResponse.value = Resource.error("Something went wrong")
                }
            }
        }
    }

}