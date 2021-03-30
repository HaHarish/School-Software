package com.newletseduvate.viewmodels

import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.newletseduvate.model.diary.DailyDiaryModel
import com.newletseduvate.model.diary.GeneralDiaryModel
import com.newletseduvate.repository.DiaryRepository
import com.newletseduvate.utils.Resource
import com.newletseduvate.utils.extensions.combineWith
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

class DiaryStudentViewModel @Inject constructor(private val repository: DiaryRepository) :
    ViewModel() {

    var moduleId = ObservableInt()

    val filterClicked = ObservableBoolean(false)

    val isPageLoadingDaily = MutableLiveData(true)
    val isPageLoadingGeneral = MutableLiveData(true)

    val mediatorIsPageLoading = isPageLoadingDaily.combineWith(isPageLoadingGeneral) { isPageLoadingDaily, isPageLoadingGeneral ->
        isPageLoadingDaily!! && isPageLoadingGeneral!!
    }

    val isNoDataDaily = ObservableBoolean(false)
    val isNoDataGeneral = ObservableBoolean(false)

    val currentPageGeneral = MutableLiveData(1)
    val currentPageDaily = MutableLiveData(1)

    val totalPageGeneral = ObservableInt(0)
    val totalPageDaily = ObservableInt(0)

    var isDataLoadingDaily = MutableLiveData(false)
    var isDataLoadingGeneral = MutableLiveData(false)

    val fromDate = ObservableField(0L)
    val toDate = ObservableField(0L)

    init {
        setFilterDate()
    }

    var generalDiaryListResponse = MutableLiveData<Resource<ArrayList<GeneralDiaryModel>>>()

    fun getGeneralDiaryMessages() {

        GlobalScope.launch {
            val response = repository.getGeneralDiaryMessages(
                moduleId.get().toString(),
                fromDate.get()!!,
                toDate.get()!!,
                currentPageGeneral.value!!
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
            val response = repository.getDailyDiaryMessages(
                moduleId.get().toString(),
                fromDate.get()!!,
                toDate.get()!!,
                currentPageDaily.value!!
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

    private fun setFilterDate() {
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

        isPageLoadingDaily.value = true
        isPageLoadingGeneral.value = true

        isNoDataDaily.set(false)
        isNoDataGeneral.set(false)

        currentPageGeneral.value = 1
        currentPageDaily.value = 1

        setFilterDate()

        dailyDiaryListResponse = MutableLiveData<Resource<ArrayList<DailyDiaryModel>>>()
        generalDiaryListResponse = MutableLiveData<Resource<ArrayList<GeneralDiaryModel>>>()
    }

}