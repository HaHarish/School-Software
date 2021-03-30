package com.newletseduvate.viewmodels

import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableInt
import androidx.databinding.ObservableLong
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.newletseduvate.model.online_class.*
import com.newletseduvate.repository.OnlineClassRepository
import com.newletseduvate.utils.Resource
import com.newletseduvate.utils.extensions.getSelectedItemsInString
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

/**
 * Created by SHASHANK BHAT on 18-Feb-21.
 *
 *
 */
class OnlineClassTeacherResourcesViewModel @Inject constructor(private val repository: OnlineClassRepository) :
    ViewModel() {


    var moduleId = ObservableInt()

    val filterClicked = ObservableBoolean(false)

    var currentPageStudent = MutableLiveData(1)
    val isPageLoadingStudent = MutableLiveData(false)
    val isNoDataStudent = ObservableBoolean(true)
    val totalPageStudent = ObservableInt(0)
    var isDataLoadingStudent = MutableLiveData(false)

    var classTypeList = MutableLiveData<Resource<ArrayList<ClassTypeModel>>>()
    var academicYearList =
        MutableLiveData<Resource<ArrayList<OnlineClassTeacherAcademicYearModel>>>()
    var branchList = MutableLiveData<Resource<ArrayList<BranchModel>>>()
    var gradeList = MutableLiveData<Resource<ArrayList<GradeMappingModel>>>()
    var sectionList = MutableLiveData<Resource<ArrayList<SectionMappingModel>>>()
//    var batchLimitList = MutableLiveData<Resource<ArrayList<GradeModel>>>()
    var subjectList = MutableLiveData<Resource<ArrayList<SubjectModel>>>()

    var coursesList = MutableLiveData<Resource<ArrayList<CourseModel>>>()

    var classTypeId = ObservableInt(-1)
    var academicYearId = ObservableInt(-1)

    var fromDate = ObservableLong(0L)
    var toDate = ObservableLong(0L)

    init {
        setFilterDate()
    }


    fun getAcademicYear() {
        GlobalScope.launch {
            val response = repository.getAcademicYear()
            withContext(Dispatchers.Main) {
                when {
                    response.isSuccessful -> {
                        academicYearList.value = Resource.success(response.body()?.data)
                    }
                }
            }
        }
    }

    fun getBranches() {
        GlobalScope.launch {
            val response = repository.getBranches(moduleId.get(), academicYearId.get())
            withContext(Dispatchers.Main) {
                when {
                    response.isSuccessful -> {
                        val tempBranch = ArrayList<BranchModel>()
                        response.body()?.data?.results?.forEach { result ->
                            result?.branch?.let { tempBranch.add(it) }
                        }
                        branchList.value = Resource.success(tempBranch)
                    }
                }
            }
        }
    }

    fun getGrades() {
        GlobalScope.launch {
            val branch_id = branchList.value?.data.getSelectedItemsInString()

            val response = repository.getGradeMapping(
                moduleId.get().toString(),
                academicYearId.get().toString(),
                branch_id
            )
            withContext(Dispatchers.Main) {
                when {
                    response.isSuccessful -> {
                        gradeList.value = Resource.success(response.body()?.data)
                    }
                }
            }
        }
    }


    fun getSection() {
        GlobalScope.launch {

//            val session_id = sectionList.value?.data.getSelectedItemsInString()
            val branch_id = branchList.value?.data.getSelectedItemsInString()
            val grade_id = gradeList.value?.data.getSelectedItemsInString()

            val response = repository.getSectionMapping(
                moduleId.get().toString(),
                academicYearId.get().toString(),
                branch_id,
                grade_id
            )
            withContext(Dispatchers.Main) {
                when {
                    response.isSuccessful -> {
                        sectionList.value = Resource.success(response.body()?.data)
                    }
                }
            }
        }
    }

    fun getSubjectMapping() {
        GlobalScope.launch {

            val branch_id = branchList.value?.data.getSelectedItemsInString()
            val grade_id = gradeList.value?.data.getSelectedItemsInString()
            val section_id = sectionList.value?.data.getSelectedItemsInString()


            val response = repository.getSubjectMapping(
                moduleId.get().toString(),
                academicYearId.get().toString(),
                branch_id,
                grade_id,
                section_id
            )
            withContext(Dispatchers.Main) {
                when {
                    response.isSuccessful -> {
                        subjectList.value = Resource.success(response.body()?.data)
                    }
                }
            }
        }
    }

    fun getCourses() {
        GlobalScope.launch {

            val grade_id = gradeList.value?.data.getSelectedItemsInString()
            val response = repository.getCourses(
                grade_id
            )
            withContext(Dispatchers.Main) {
                when {
                    response.isSuccessful -> {
                        coursesList.value = Resource.success(response.body()?.result)
                    }
                }
            }
        }
    }

    var onlineClassResponse = MutableLiveData<Resource<TeacherViewClassResponse>>()
    var onlineClassListResponse = MutableLiveData<Resource<ArrayList<TeacherViewClassModel>>>()

    fun getTeacherOnlineClass() {

        val subject_id = subjectList.value?.data.getSelectedItemsInString()
        val section_id = sectionList.value?.data.getSelectedItemsInString()
        val courses_id = coursesList.value?.data.getSelectedItemsInString()

        var section_mapping_id = ""
        for(section in sectionList.value?.data!!){
            if(section.selected){
                section_mapping_id = section.id.toString()
            }
        }

        GlobalScope.launch {
            val response = repository.getTeacherOnlineClass(
                academicYearId.get().toString(),
                section_mapping_id,
                subject_id,
                classTypeId.get().toString(),
                fromDate.get(),
                toDate.get(),
                moduleId.get().toString(),
                currentPageStudent.value!!,
                courses_id
            )

            withContext(Dispatchers.Main) {
                when {
                    response.isSuccessful -> {

                        if (response.body() != null && response.body()!!.data != null) {
                            onlineClassResponse.value = Resource.success(response.body()!!)
                            onlineClassListResponse.value = Resource.success(response.body()!!.data!!)
                        }

                        response.body()!!.totalPages?.let { totalPageStudent.set(it) }

                    }
                    else -> {
                        onlineClassListResponse.value = Resource.error(response.message())
                    }
                }
            }
        }
    }


    fun setFilterDate() {
        val calendar = Calendar.getInstance()

        fromDate.set(calendar.timeInMillis)

        calendar.add(Calendar.DAY_OF_YEAR, 6)
        toDate.set(calendar.timeInMillis)
    }

    fun refreshPage() {

        filterClicked.set(false)
        getTeacherOnlineClass()
    }

    fun clear() {

        filterClicked.set(false)

        onlineClassResponse = MutableLiveData<Resource<TeacherViewClassResponse>>()
        onlineClassListResponse = MutableLiveData<Resource<ArrayList<TeacherViewClassModel>>>()

    }
}