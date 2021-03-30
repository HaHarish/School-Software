package com.newletseduvate.viewmodels

import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.newletseduvate.model.diary.DailyDiaryModel
import com.newletseduvate.model.diary.GeneralDiaryModel
import com.newletseduvate.model.online_class.BranchModel
import com.newletseduvate.model.online_class.GradeMappingModel
import com.newletseduvate.model.online_class.OnlineClassTeacherAcademicYearModel
import com.newletseduvate.model.online_class.SectionMappingModel
import com.newletseduvate.repository.DiaryRepository
import com.newletseduvate.utils.Resource
import com.newletseduvate.utils.extensions.combineWith
import com.newletseduvate.utils.extensions.getSelectedItemsInString
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

class DiaryTeacherViewModel @Inject constructor(private val repository: DiaryRepository) :
    ViewModel() {

    var moduleId = ObservableInt()

    val filterClicked = ObservableBoolean(false)

    val isPageLoadingDaily = MutableLiveData(false)
    val isPageLoadingGeneral = MutableLiveData(false)

    val mediatorIsPageLoading = isPageLoadingDaily.combineWith(isPageLoadingGeneral) { isPageLoadingDaily, isPageLoadingGeneral ->
        isPageLoadingDaily!! && isPageLoadingGeneral!!
    }

    val isNoDataDaily = ObservableBoolean(true)
    val isNoDataGeneral = ObservableBoolean(true)

    val currentPageGeneral = MutableLiveData(1)
    val currentPageDaily = MutableLiveData(1)

    val totalPageGeneral = ObservableInt(0)
    val totalPageDaily = ObservableInt(0)

    var isDataLoadingDaily = MutableLiveData(false)
    var isDataLoadingGeneral = MutableLiveData(false)

    val fromDate = ObservableField(0L)
    val toDate = ObservableField(0L)

    var academicYearId = ObservableInt(-1)

    var academicYearList =
        MutableLiveData<Resource<ArrayList<OnlineClassTeacherAcademicYearModel>>>()
    var branchList = MutableLiveData<Resource<ArrayList<BranchModel>>>()
    var gradeList = MutableLiveData<Resource<ArrayList<GradeMappingModel>>>()
    var sectionList = MutableLiveData<Resource<ArrayList<SectionMappingModel>>>()

    init {
        setFilterDate()
    }

    var generalDiaryListResponse = MutableLiveData<Resource<ArrayList<GeneralDiaryModel>>>()

    fun getGeneralDiaryMessages() {

        GlobalScope.launch {
            val response = repository.getTeacherGeneralDiaryMessages(
                moduleId.get().toString(),
                fromDate.get()!!,
                toDate.get()!!,
                currentPageGeneral.value!!,
                branchList.value?.data.getSelectedItemsInString(),
                gradeList.value?.data.getSelectedItemsInString(),
                sectionList.value?.data.getSelectedItemsInString()
            )

            withContext(Dispatchers.Main) {
                when {
                    response.isSuccessful -> {
                        if (response.body() != null) {
                            generalDiaryListResponse.value =
                                Resource.success(response.body()!!.result?.results)

                            response.body()!!.result?.totalPages?.let { totalPageGeneral.set(it) }
                        }
                    }
                    else -> generalDiaryListResponse.value = Resource.error(response.message())

                }
            }
        }

    }

    var dailyDiaryListResponse = MutableLiveData<Resource<ArrayList<DailyDiaryModel>>>()

    fun getDailyDiaryMessages() {

        GlobalScope.launch {
            val response = repository.getTeacherDailyDiaryMessages(
                moduleId.get().toString(),
                fromDate.get()!!,
                toDate.get()!!,
                currentPageDaily.value!!,
                branchList.value?.data.getSelectedItemsInString(),
                gradeList.value?.data.getSelectedItemsInString(),
                sectionList.value?.data.getSelectedItemsInString()
            )

            withContext(Dispatchers.Main) {
                when {
                    response.isSuccessful -> {
                        if (response.body() != null) {
                            dailyDiaryListResponse.value =
                                Resource.success(response.body()!!.result?.results)
                            response.body()!!.result?.totalPages?.let { it1 ->
                                totalPageDaily.set(
                                    it1
                                )
                            }
                        }
                    }
                    else -> dailyDiaryListResponse.value = Resource.error(response.message())
                }
            }


        }

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

    fun deleteTeacherDiary(
        diary_id: String?
    ) {
        GlobalScope.launch {
            val response = repository.deleteTeacherDiary(diary_id)
        }
    }


    fun setFilterDate() {
        val calendar = Calendar.getInstance()
        toDate.set(calendar.timeInMillis)

        calendar.add(Calendar.DAY_OF_YEAR, -6)
        fromDate.set(calendar.timeInMillis)
    }

    fun refreshPage() {

        filterClicked.set(false)

        isPageLoadingDaily.value = true
        isPageLoadingGeneral.value = true

        isNoDataDaily.set(false)
        isNoDataGeneral.set(false)

        currentPageGeneral.value = 1
        currentPageDaily.value = 1

        getDailyDiaryMessages()
        getGeneralDiaryMessages()
    }

    fun clear() {

        filterClicked.set(false)

        isPageLoadingDaily.value = false
        isPageLoadingGeneral.value = false

        isNoDataDaily.set(true)
        isNoDataGeneral.set(true)

        currentPageGeneral.value = 1
        currentPageDaily.value = 1

        setFilterDate()

        dailyDiaryListResponse = MutableLiveData<Resource<ArrayList<DailyDiaryModel>>>()
        generalDiaryListResponse = MutableLiveData<Resource<ArrayList<GeneralDiaryModel>>>()

        academicYearList =
            MutableLiveData<Resource<ArrayList<OnlineClassTeacherAcademicYearModel>>>()
        branchList = MutableLiveData<Resource<ArrayList<BranchModel>>>()
        gradeList = MutableLiveData<Resource<ArrayList<GradeMappingModel>>>()
        sectionList = MutableLiveData<Resource<ArrayList<SectionMappingModel>>>()
    }

}