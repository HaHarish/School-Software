package com.newletseduvate.viewmodels

import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableInt
import androidx.databinding.ObservableLong
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.newletseduvate.model.online_class.GradeModel
import com.newletseduvate.model.online_class.StudentOnlineClassModel
import com.newletseduvate.repository.OnlineClassRepository
import com.newletseduvate.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Created by SHASHANK BHAT on 18-Feb-21.
 *
 *
 */
class OnlineClassViewClassViewModel @Inject constructor(private val repository: OnlineClassRepository) : ViewModel() {


    var moduleId = ObservableInt()

    val filterClicked = ObservableBoolean(false)

    var currentPageStudent = MutableLiveData(1)
    val isPageLoadingStudent = MutableLiveData(true)
    val isNoDataStudent = ObservableBoolean(false)
    val totalPageStudent = ObservableInt(0)
    var isDataLoadingStudent = MutableLiveData(false)

    var branchList = MutableLiveData<Resource<ArrayList<GradeModel>>>()
    var gradeList = MutableLiveData<Resource<ArrayList<GradeModel>>>()
    var courseList = MutableLiveData<Resource<ArrayList<GradeModel>>>()
    var batchLimitList = MutableLiveData<Resource<ArrayList<GradeModel>>>()

    var branchId = ObservableInt(-1)
    var gradeId = ObservableInt(-1)
    var courseId = ObservableInt(-1)
    var batchLimitId = ObservableInt(-1)

    var startDate = ObservableLong(0L)
    var endDate = ObservableLong(0L)

    var onlineClassListResponse = MutableLiveData<Resource<ArrayList<StudentOnlineClassModel>>>()

    fun getStudentOnlineClass(){

        GlobalScope.launch {

            withContext(Dispatchers.Main){
                val listTemp = ArrayList<StudentOnlineClassModel>()
                onlineClassListResponse.value = Resource.success(listTemp)
            }
//            val response = repository.getStudentOnlineClass("", currentPageStudent.value, 1)
//            Log.i("response", response.toString())
//            withContext(Dispatchers.Main) {
//                when {
//                    response.isSuccessful -> {
//
//                        val listTemp = ArrayList<StudentOnlineClassModel>()
//                        if (response.body() != null && response.body()!!.has("data")) {
//                            val data = response.body()!!.get("data").asJsonArray
//                            data.forEach {
//                                val jsonObj =
//                                    Gson().fromJson(it, StudentOnlineClassModel::class.java)
//                                listTemp.add(jsonObj)
//                            }
//                        }
//
//                        onlineClassListResponse.value = Resource.success(listTemp)
//                    }
//                    else -> {
//                        onlineClassListResponse.value = Resource.error(response.message())
//                    }
//                }
//            }
        }

    }

    fun refreshPage() {

        filterClicked.set(false)

//        isPageLoadingDaily.set(true)
//        isPageLoadingGeneral.set(true)
//
//        isNoDataDaily.set(false)
//        isNoDataGeneral.set(false)
//
//        currentPageGeneral.value = 1
//        currentPageDaily.value = 1
//
//        getDailyDiaryMessages()
//        getGeneralDiaryMessages()

        getStudentOnlineClass()
    }

    fun clear() {

        filterClicked.set(false)

        onlineClassListResponse = MutableLiveData<Resource<ArrayList<StudentOnlineClassModel>>>()

//        isPageLoadingDaily.set(true)
//        isPageLoadingGeneral.set(true)
//
//        isNoDataDaily.set(false)
//        isNoDataGeneral.set(false)
//
//        currentPageGeneral.value = 1
//        currentPageDaily.value = 1
//
//        setFilterDate()
//
//        dailyDiaryListResponse = MutableLiveData<Resource<ArrayList<DailyDiaryModel>>>()
//        generalDiaryListResponse = MutableLiveData<Resource<ArrayList<GeneralDiaryModel>>>()
    }
}