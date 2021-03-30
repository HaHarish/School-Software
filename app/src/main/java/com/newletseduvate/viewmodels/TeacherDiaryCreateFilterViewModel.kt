package com.newletseduvate.viewmodels

import android.content.SharedPreferences
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.newletseduvate.model.BottomSheetItem
import com.newletseduvate.model.circular.teacher.*
import com.newletseduvate.model.diary.teacher.*
import com.newletseduvate.model.general.UploadFileModel
import com.newletseduvate.repository.CircularRepository
import com.newletseduvate.repository.DiaryRepository
import com.newletseduvate.utils.Resource
import com.newletseduvate.utils.extensions.getBranchName
import com.newletseduvate.utils.extensions.getToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class TeacherDiaryCreateFilterViewModel @Inject constructor(private val sharedPreferences: SharedPreferences,
                                                            private val circularRepository: CircularRepository,
                                                            private val diaryRepository: DiaryRepository
                                            ): ViewModel() {

    val fromDate = ObservableField(0L)
    val toDate = ObservableField(0L)

    var academicYearList = MutableLiveData<ArrayList<BottomSheetItem>>()
    var academicYearListId = ObservableInt(-1)

    var branchList = MutableLiveData<ArrayList<BottomSheetItem>>()
    var branchListId = ObservableInt(-1)

    var gradeList = MutableLiveData<ArrayList<BottomSheetItem>>()
    var gradeListId = ObservableInt(-1)

    var sectionList = MutableLiveData<ArrayList<BottomSheetItem>>()
    var sectionListId = ObservableInt(-1)

    var subjectList = MutableLiveData<ArrayList<BottomSheetItem>>()
    var subjectListId = ObservableInt(-1)

    var chapterList = MutableLiveData<ArrayList<BottomSheetItem>>()
    var chapterListId = ObservableInt(-1)

    var currentPageStudent = MutableLiveData(1)
    val isPageLoadingStudent = MutableLiveData(true)
    var isNoDataStudent = ObservableBoolean(false)
    val totalPageStudent = ObservableInt(0)
    var isDataLoadingStudent = MutableLiveData(false)

    var circularAcademicYearResponse = MutableLiveData<Resource<CircularAcademicYearResponse>>()
    var circularBranchResponse = MutableLiveData<Resource<CircularBranchResponse>>()
    var circularGradeResponse = MutableLiveData<Resource<CircularGradeResponse>>()
    var circularSectionResponse = MutableLiveData<Resource<CircularSectionResponse>>()
    var circularTeacherFilterResponse = MutableLiveData<Resource<ArrayList<CircularTeacherFilterResponse.Result>>>()
    var circularCreateResponse = MutableLiveData<Resource<CircularCreateResponse>>()

    var teacherDiaryActiveStudentsResponse = MutableLiveData<Resource<TeacherDiaryActiveStudentsResponse>>()

    var moduleId : Int = 0
    var academicYearId: Int = 0
    var branchId: Int = 0
    var gradeId: Int = 0
    var sectionId: Int = 0
    var subjectId : Int = 0
    var chapterId : Int = 0

    var isSelectAllChecked = ObservableBoolean(false)

    var circularTitle: String = ""
    var circularTitleDesc: String = ""
    var circularDescription: String = ""

    var listUserCreate: MutableList<Int> = mutableListOf()
    var listGradeCreate: MutableList<Int> = mutableListOf()
    var listSectionCreate: MutableList<Int> = mutableListOf()

    var listImageUrl = MutableLiveData<ArrayList<String>>(ArrayList())

    var circularTeacherUploadFileResponse = MutableLiveData<Resource<CircularTeacherUploadFileResponse>>()
    var teacherDiaryFileUploadResponse = MutableLiveData<Resource<TeacherDiaryFileUploadResponse>>()

    var circularTeacherDeleteFileResponse = MutableLiveData<Resource<CircularTeacherDeleteFileResponse>>()

    var teacherDiaryCreateResponse = MutableLiveData<Resource<TeacherDiaryCreateResponse>>()
    var teacherDiaryCreateDailyResponse = MutableLiveData<Resource<TeacherDiaryCreateDailyResponse>>()

    var teacherDiarySubjectResponse = MutableLiveData<Resource<TeacherDiarySubjectResponse>>()
    var teacherDiaryChapterResponse = MutableLiveData<Resource<TeacherDiaryChapterResponse>>()

    var isGeneralClicked = ObservableBoolean(false)
    var isDailyClicked = ObservableBoolean(false)
    var isFilterClicked = ObservableBoolean(false)

    var title: String? = null
    var description: String? = null

    var recapOfPreviousClass: String? = null
    var detailsOfClasswork: String? = null
    var summary: String? = null
    var toolsUsed: String? = null
    var homework: String? = null

    /* Circular Academic Year */
    fun circularAcademicYear(){
        GlobalScope.launch {
            val response = circularRepository.circularAcademicYear()
            withContext(Dispatchers.Main){
                when(response.body()?.statusCode){
                    200 -> {
                        val academicYears = ArrayList<BottomSheetItem>()
                        response.body()?.data?.forEach {
                            academicYears.add(BottomSheetItem(it?.id, it?.sessionYear))
                        }
                        academicYearList.value = academicYears
                    }
                    // 401 -> circularFilter.value = response.body()?.message?.let { Resource.error(it) }
                    else -> circularAcademicYearResponse.value = Resource.error("Something went wrong")
                }
            }
        }
    }

    /* Circular Branch */
    fun circularBranch(){
        GlobalScope.launch {
            val response = diaryRepository.diaryBranch(academicYearId, moduleId)
            withContext(Dispatchers.Main){
                when(response.body()?.statusCode){
                    200 -> {
                        val branches = ArrayList<BottomSheetItem>()
                        response.body()?.data?.forEach {
                            branches.add(BottomSheetItem(it?.id, it?.branchName))
                        }
                        branchList.value = branches
                    }
                    // 401 -> circularFilter.value = response.body()?.message?.let { Resource.error(it) }
                    else -> circularBranchResponse.value = Resource.error("Something went wrong")
                }
            }
        }
    }

    /* Circular Grade */
    fun circularGrade(){
        GlobalScope.launch {
            val response = circularRepository.circularGrade(academicYearId, moduleId, branchId)
            withContext(Dispatchers.Main){
                when(response.body()?.statusCode){
                    200 -> {
                        val grades = ArrayList<BottomSheetItem>()
                        response.body()?.data?.forEach {
                            grades.add(BottomSheetItem(it?.gradeId, it?.gradeGradeName))
                        }
                        gradeList.value = grades
                    }
                    // 401 -> circularFilter.value = response.body()?.message?.let { Resource.error(it) }
                    else -> circularGradeResponse.value = Resource.error("Something went wrong")
                }
            }
        }
    }

    /* Circular Section */
    fun circularSection(){
        GlobalScope.launch {
            val response = circularRepository.circularSection(academicYearId, moduleId, branchId, gradeId)
            withContext(Dispatchers.Main){
                when(response.body()?.statusCode){
                    200 -> {
                        val sections = ArrayList<BottomSheetItem>()
                        response.body()?.data?.forEach {
                            sections.add(BottomSheetItem(it?.sectionId, it?.sectionSectionName))
                        }
                        sectionList.value = sections
                    }
                    // 401 -> circularFilter.value = response.body()?.message?.let { Resource.error(it) }
                    else -> circularSectionResponse.value = Resource.error("Something went wrong")
                }
            }
        }
    }

    /* Diary Subjects */
    fun diarySubjects(){
        GlobalScope.launch {
            val response = diaryRepository.teacherDiarySubject(academicYearId,
                branchId, gradeId, sectionId, moduleId)
            withContext(Dispatchers.Main){
                when(response.body()?.statusCode){
                    200 -> {
                        val subjects = ArrayList<BottomSheetItem>()
                        response.body()?.data?.forEach {
                            subjects.add(BottomSheetItem(it?.subjectId, it?.subjectSubjectName))
                        }
                        subjectList.value = subjects
                    }
                    // 401 -> circularFilter.value = response.body()?.message?.let { Resource.error(it) }
                    else -> teacherDiarySubjectResponse.value = Resource.error("Something went wrong")
                }
            }
        }
    }

    /* Diary Subjects */
    fun diaryChapters(){
        GlobalScope.launch {
            val response = diaryRepository.teacherDiaryChapter(academicYearId,
                subjectId,  moduleId)
            withContext(Dispatchers.Main){
                when(response.body()?.statusCode){
                    200 -> {
                        val chapters = ArrayList<BottomSheetItem>()
                        response.body()?.result?.forEach {
                            chapters.add(BottomSheetItem(it?.id, it?.chapterName))
                        }
                        chapterList.value = chapters
                    }
                    // 401 -> circularFilter.value = response.body()?.message?.let { Resource.error(it) }
                    else -> teacherDiaryChapterResponse.value = Resource.error("Something went wrong")
                }
            }
        }
    }



    /* Teacher Diary Active Students list */
    fun teacherDiaryActiveStudentsList(){
        GlobalScope.launch {
            val response = diaryRepository.getTeacherDiaryActiveStudentsList(
                academicYearId,
                0,
                1,
                15,
                gradeId,
                moduleId)
            withContext(Dispatchers.Main){
                when(response.body()?.statusCode){
                    200 -> {
                        teacherDiaryActiveStudentsResponse.value = Resource.success(response.body())
                    }
                    // 401 -> circularFilter.value = response.body()?.message?.let { Resource.error(it) }
                    else -> teacherDiaryActiveStudentsResponse.value = Resource.error("Something went wrong")
                }
            }
        }
    }

    fun teacherDiaryFileUpload(uploadFileModel: UploadFileModel){
        GlobalScope.launch {
            val response = diaryRepository.teacherDiaryFileUpload(sharedPreferences.getToken()!!,
                sharedPreferences.getBranchName()!!,
                gradeId.toString(),
                sectionId.toString(),
                uploadFileModel)
            withContext(Dispatchers.Main){
                when(response.body()?.statusCode){
                    200 -> {
                        listImageUrl.value?.add(response.body()?.result!!)
                        listImageUrl.value = listImageUrl.value

                        teacherDiaryFileUploadResponse.value = Resource.success(response.body())
                    }
                    // 401 -> circularFilter.value = response.body()?.message?.let { Resource.error(it) }
                    else -> teacherDiaryFileUploadResponse.value = Resource.error("Something went wrong")
                }
            }
        }
    }

    fun teacherDiaryCreate(){

        listGradeCreate.add(gradeId)
        listSectionCreate.add(sectionId)
        listUserCreate.add(0)

        GlobalScope.launch {
            val response = diaryRepository.teacherDiaryCreate(sharedPreferences.getToken()!!,
                TeacherDiaryCreateRequest(
                    title,
                    description,
                    listImageUrl.value,
                    branchId,
                    listGradeCreate,
                    listSectionCreate,
                    listUserCreate,
                    1
                )
            )
            withContext(Dispatchers.Main){
                when(response.body()?.statusCode){
                    200 -> {
                        teacherDiaryCreateResponse.value = Resource.success(response.body())
                    }
                    // 401 -> circularFilter.value = response.body()?.message?.let { Resource.error(it) }
                    else -> teacherDiaryCreateResponse.value = Resource.error("Something went wrong")
                }
            }
        }
    }

    fun teacherDiaryCreateDaily(){

        listGradeCreate.add(gradeId)
        listSectionCreate.add(sectionId)
        listUserCreate.add(0)

        GlobalScope.launch {
            val response = diaryRepository.teacherDiaryCreateDaily(sharedPreferences.getToken()!!,
                TeacherDiaryCreateDailyRequest(
                    academicYearId,
                    moduleId,
                    branchId,
                    listGradeCreate,
                    listSectionCreate,
                    subjectId,
                    chapterId,
                    listImageUrl.value,
                    TeacherDiaryCreateDailyRequest.TeacherReport(
                        recapOfPreviousClass,
                        summary,
                        detailsOfClasswork,
                        toolsUsed,
                        homework
                    ),
                    2
                )
            )
            withContext(Dispatchers.Main){
                when(response.body()?.statusCode){
                    200 -> {
                        teacherDiaryCreateDailyResponse.value = Resource.success(response.body())
                    }
                    // 401 -> circularFilter.value = response.body()?.message?.let { Resource.error(it) }
                    else -> teacherDiaryCreateDailyResponse.value = Resource.error("Something went wrong")
                }
            }
        }
    }

}