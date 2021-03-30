package com.newletseduvate.viewmodels

import android.content.SharedPreferences
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt
import androidx.databinding.ObservableLong
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.newletseduvate.model.BottomSheetItem
import com.newletseduvate.model.online_class.*
import com.newletseduvate.repository.OnlineClassRepository
import com.newletseduvate.utils.Resource
import com.newletseduvate.utils.constants.Constants
import com.newletseduvate.utils.extensions.getSelectedItemsInString
import com.newletseduvate.utils.extensions.getUserId
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

/**
 * Created by SHASHANK BHAT on 18-Feb-21.
 *
 *
 */
class OnlineClassCreateClassViewModel @Inject constructor(
    private val repository: OnlineClassRepository,
    private val pref: SharedPreferences
) :
    ViewModel() {


    var moduleId = ObservableInt()

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
    var tutorsList = MutableLiveData<Resource<ArrayList<CreateClassTutorsModel>>>()

    var coHostList = MutableLiveData<Resource<ArrayList<CreateClassTutorsModel>>>()

    var classTypeId = ObservableInt(-1)
    var academicYearId = ObservableInt(-1)

    var startDate = ObservableLong(0L)
//    var toDate = ObservableLong(0L)

    var startTime = ObservableField("")
    var duration = ObservableField("")

    var isRecurring = ObservableBoolean(false)

    var days = MutableLiveData<ArrayList<BottomSheetItem>>()


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


    fun getSubSectionList() {
        GlobalScope.launch {

            val branch_id = branchList.value?.data.getSelectedItemsInString()
            val grade_id = gradeList.value?.data.getSelectedItemsInString()

            var role_id: Int? = -1
            for (tutor in tutorsList.value?.data!!) {
                if (tutor.selected) {
                    role_id = tutor.roles
                }
            }

            val response = repository.getSubSectionList(
                moduleId.get(),
                role_id,
                0,
                grade_id.toInt(),
                branch_id.toInt(),
                academicYearId.get()
            )
            withContext(Dispatchers.Main) {
                when {
                    response.isSuccessful -> {
                        sectionList.value = Resource.success(response.body()?.data?.section)
                        subjectList.value = Resource.success(response.body()?.data?.subject)
                    }
                }
            }
        }
    }

    fun getTutorEmailList() {
        GlobalScope.launch {

            val branch_id = branchList.value?.data.getSelectedItemsInString()
            val grade_id = gradeList.value?.data.getSelectedItemsInString()

            val response = repository.getTutorEmailList(
                branch_id.toInt(),
                grade_id.toInt(),
                academicYearId.get()
            )
            withContext(Dispatchers.Main) {
                when {
                    response.isSuccessful -> {
                        tutorsList.value = Resource.success(response.body()?.data)
                    }
                }
            }
        }
    }

    val isTutorAvailable = ObservableBoolean(false)

    fun getCheckTutorTime() {
        GlobalScope.launch {

            val tutorEmail = tutorsList.value?.data.getSelectedItemsInString()

            val requestDateFormat =
                SimpleDateFormat(Constants.DateFormat.YYYYMMDD, Locale.getDefault())
            val startDate = requestDateFormat.format(Date(startDate.get()))

            if (duration.get()?.isNotEmpty()!!) {
                val response = repository.getCheckTutorTime(
                    tutorEmail,
                    startDate + " " + startTime.get(),
                    duration.get()?.toInt()
                )
                withContext(Dispatchers.Main) {
                    when {
                        response.isSuccessful -> {
                            try {
                                if (response.body()
                                        ?.get("message")?.asString == "Tutor is available"
                                )
                                    isTutorAvailable.set(true)
                                else
                                    isTutorAvailable.set(false)
                            } catch (ex: Exception) {
                                isTutorAvailable.set(false)
                            }
                        }
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

    fun setFilterDate() {
        val calendar = Calendar.getInstance()

        startDate.set(calendar.timeInMillis)
    }


    var studentList = MutableLiveData<Resource<ArrayList<CreateClassStudentFilterModel>>>()

    fun getStudentFilter(
        section_mapping_ids: Int?,
        subject_ids: Int?,
        branch_ids: Int?,
        grade_ids: Int?,
    ) {
        GlobalScope.launch {

            val response = repository.getStudentFilter(
                section_mapping_ids,
                subject_ids,
                branch_ids,
                grade_ids
            )
            withContext(Dispatchers.Main) {
                when {
                    response.isSuccessful -> {
                        val tempArray = ArrayList<CreateClassStudentFilterModel>()

                        response.body()?.data?.forEach {
                            it.isSelected = ObservableBoolean(false)
                            tempArray.add(it)
                        }

                        studentList.value = Resource.success(tempArray)
                    }
                }
            }
        }
    }

    val createClassSuccessful = MutableLiveData("")

    fun postCreateClassOnlineRecurring(
        createClassRequest: CreateClassRequest
    ) {
        createClassRequest.userId = pref.getUserId()

        GlobalScope.launch {

            val response = repository.postCreateClassOnlineRecurring(
                createClassRequest
            )
            withContext(Dispatchers.Main) {
                when {
                    response.isSuccessful -> {
                        createClassSuccessful.value = "SUCCESS"
                    }
                    else -> createClassSuccessful.value = response.message()
                }
            }


        }
    }

    fun postCreateClassOnlineErpClass(
        createClassRequest: CreateClassRequest
    ) {
        createClassRequest.userId = pref.getUserId()

        GlobalScope.launch {

            createClassRequest.subjectId = null
            createClassRequest.final_price = 0
            createClassRequest.price = 0

            val response = repository.postCreateClassOnlineErpClass(
                createClassRequest
            )
            withContext(Dispatchers.Main) {
                when {
                    response.isSuccessful -> {
                        createClassSuccessful.value = "SUCCESS"
                    }
                    else -> createClassSuccessful.value = response.message()
                }
            }


        }
    }

    fun refreshPage() {
    }

    fun clear() {

    }
}