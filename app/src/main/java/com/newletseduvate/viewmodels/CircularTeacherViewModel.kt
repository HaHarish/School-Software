package com.newletseduvate.viewmodels

import android.content.SharedPreferences
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.newletseduvate.model.BottomSheetItem
import com.newletseduvate.model.circular.teacher.*
import com.newletseduvate.model.general.UploadFileModel
import com.newletseduvate.repository.CircularRepository
import com.newletseduvate.utils.Resource
import com.newletseduvate.utils.constants.ModulesConstant
import com.newletseduvate.utils.constants.PrefKeys
import com.newletseduvate.utils.extensions.getBranchName
import com.newletseduvate.utils.extensions.getToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CircularTeacherViewModel @Inject constructor(private val sharedPreferences: SharedPreferences,
                                                   private val circularRepository: CircularRepository
                                                    ): ViewModel(){

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

    var roleList = MutableLiveData<ArrayList<BottomSheetItem>>()
    var roleListId = ObservableInt(-1)

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

    var moduleId : Int = 0
    var academicYearId: Int = 0
    var branchId: Int = 0
    var gradeId: Int = 0
    var sectionId: Int = 0
    var isCreateClassClicked = ObservableBoolean(false)
    var circularTitle: String = ""
    var circularTitleDesc: String = ""
    var circularDescription: String = ""

    var listBranchCreate: MutableList<Int> = mutableListOf()
    var listGradeCreate: MutableList<Int> = mutableListOf()
    var listSectionCreate: MutableList<Int> = mutableListOf()

    var circularTeacherDeleteResponse = MutableLiveData<Resource<CircularTeacherDeleteResponse>>()
    var circularId = ObservableInt(0)

    var circularTeacherUploadFileResponse = MutableLiveData<Resource<CircularTeacherUploadFileResponse>>()

    var circularTeacherDeleteFileResponse = MutableLiveData<Resource<CircularTeacherDeleteFileResponse>>()

    var listImageUrl = MutableLiveData<ArrayList<String>>(ArrayList())

    var isAttachClicked = ObservableBoolean(false)

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
            val response = circularRepository.circularBranch(academicYearId, moduleId)
            withContext(Dispatchers.Main){
                when(response.body()?.statusCode){
                    200 -> {
                        val branches = ArrayList<BottomSheetItem>()
                        if(response.body()?.data?.results?.size != 0){
                            branches.add(BottomSheetItem(response.body()?.data?.results?.get(0)?.branch?.id,
                                response.body()?.data?.results?.get(0)?.branch?.branchName))
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
                            sections.add(BottomSheetItem(it?.id, it?.sectionSectionName))
                        }
                        sectionList.value = sections
                    }
                    // 401 -> circularFilter.value = response.body()?.message?.let { Resource.error(it) }
                    else -> circularSectionResponse.value = Resource.error("Something went wrong")
                }
            }
        }
    }

    /* Roles */
    fun circularRole(){
        val roles = ArrayList<BottomSheetItem>()
        roles.add(BottomSheetItem(0, "Student Circular"))
        roleList.value = roles
    }

    /* Circular Section */
    fun circularTeacherFilterResults(){
        GlobalScope.launch {
            val response = circularRepository.circularTeacherFilterResults(sharedPreferences.getBoolean(
                PrefKeys.IS_SUPER_USER, false),
                branchId,
                gradeId,
                sectionId,
                academicYearId,
                fromDate.get()!!,
                toDate.get()!!,
                currentPageStudent.value.toString(),
                totalPageStudent.get().toString())
            withContext(Dispatchers.Main){
                when(response.body()?.statusCode){
                    200 -> {
                        if(response.body()?.result?.size != 0){
                            circularTeacherFilterResponse.value = Resource.success(response.body()?.result)
                        }else{
                            isNoDataStudent.set(true)
                        }
                    }
                    // 401 -> circularFilter.value = response.body()?.message?.let { Resource.error(it) }
                    else -> circularTeacherFilterResponse.value = Resource.error("Something went wrong")
                }
            }
        }
    }

    /* Circular Create */
    fun circularTeacherCreate(){

        listBranchCreate.add(branchId)
        listGradeCreate.add(gradeId)
        listSectionCreate.add(sectionId)

        GlobalScope.launch {
            val response = circularRepository.circularTeacherCreate(
                CircularCreateRequest(
                circularTitleDesc,
                circularDescription,
                    ModulesConstant.Circular.teacherCircular,
                listImageUrl.value,
                listBranchCreate,
                listGradeCreate,
                listSectionCreate,
                academicYearId
            )
            )
            withContext(Dispatchers.Main){
                when(response.body()?.statusCode){
                    200 -> {
                        circularCreateResponse.value = Resource.success(response.body())
                    }
                    // 401 -> circularFilter.value = response.body()?.message?.let { Resource.error(it) }
                    else -> circularCreateResponse.value = Resource.error("Something went wrong")
                }
            }
        }
    }

    fun circularTeacherDelete(){
        GlobalScope.launch {
            val response = circularRepository.circularTeacherDelete(sharedPreferences.getToken()!!,
                CircularTeacherDeleteRequest(circularId.get(),true)
            )
            withContext(Dispatchers.Main){
                when(response.body()?.statusCode){
                    200 -> {
                        circularTeacherDeleteResponse.value = Resource.success(response.body())
                    }
                    // 401 -> circularFilter.value = response.body()?.message?.let { Resource.error(it) }
                    else -> circularTeacherDeleteResponse.value = Resource.error("Something went wrong")
                }
            }
        }
    }

    fun circularTeacherUploadFile(uploadFileModel: UploadFileModel){
        GlobalScope.launch {
            val response = circularRepository.circularTeacherUploadFile(sharedPreferences.getToken()!!,
                sharedPreferences.getBranchName()!!,
                uploadFileModel)
            withContext(Dispatchers.Main){
                when(response.body()?.statusCode){
                    200 -> {
                        listImageUrl.value?.add(response.body()?.result!!)
                        listImageUrl.value = listImageUrl.value

                        circularTeacherUploadFileResponse.value = Resource.success(response.body())
                    }
                    // 401 -> circularFilter.value = response.body()?.message?.let { Resource.error(it) }
                    else -> circularTeacherUploadFileResponse.value = Resource.error("Something went wrong")
                }
            }
        }
    }

    fun circularTeacherDeleteFile(imageFileUrl: String?){
        GlobalScope.launch {
            val response = circularRepository.circularTeacherDeleteUploadedFile(sharedPreferences.getToken()!!,
                imageFileUrl)
            withContext(Dispatchers.Main){
                when(response.body()?.statusCode){
                    204 -> {
                        circularTeacherDeleteFileResponse.value = Resource.success(response.body())
                    }
                    // 401 -> circularFilter.value = response.body()?.message?.let { Resource.error(it) }
                    else -> circularTeacherDeleteFileResponse.value = Resource.error("Something went wrong")
                }
            }
        }
    }

    fun clear(){
        circularAcademicYearResponse = MutableLiveData<Resource<CircularAcademicYearResponse>>()
        circularBranchResponse = MutableLiveData<Resource<CircularBranchResponse>>()
        circularGradeResponse = MutableLiveData<Resource<CircularGradeResponse>>()
        circularSectionResponse = MutableLiveData<Resource<CircularSectionResponse>>()
        circularTeacherFilterResponse = MutableLiveData<Resource<ArrayList<CircularTeacherFilterResponse.Result>>>()
        circularCreateResponse = MutableLiveData<Resource<CircularCreateResponse>>()

        circularTeacherDeleteResponse = MutableLiveData<Resource<CircularTeacherDeleteResponse>>()

        circularTeacherUploadFileResponse = MutableLiveData<Resource<CircularTeacherUploadFileResponse>>()

        circularTeacherDeleteFileResponse = MutableLiveData<Resource<CircularTeacherDeleteFileResponse>>()

        listImageUrl = MutableLiveData<ArrayList<String>>(ArrayList())
    }
}