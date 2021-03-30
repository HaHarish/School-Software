package com.newletseduvate.viewmodels

import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.newletseduvate.model.online_class.OnlineClassTeacherAttendanceListModel
import com.newletseduvate.model.online_class.TeacherViewClassModel
import com.newletseduvate.repository.OnlineClassRepository
import com.newletseduvate.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

class OnlineClassTeacherAttendanceViewModel @Inject constructor(private val repository: OnlineClassRepository) :
    ViewModel() {

    var moduleId = ObservableInt()


    val isPageLoadingDaily = MutableLiveData(true)

    val isNoDataDaily = ObservableBoolean(false)

    val currentPageDaily = MutableLiveData(1)

    val totalPageDaily = ObservableInt(0)

    var isDataLoadingDaily = MutableLiveData(false)

    val date = ObservableField(0L)

    val attendedCount = ObservableInt(0)
    val absentCount = ObservableInt(0)

    init {
        setFilterDate()
    }

    lateinit var onlineClassModel: TeacherViewClassModel

    var generalDiaryListResponse = MutableLiveData<Resource<ArrayList<OnlineClassTeacherAttendanceListModel>>>()

    fun getAttendanceList() {

        GlobalScope.launch {
            val response = repository.getAttendanceList(
                onlineClassModel.id!!,
                date.get()!!,
                currentPageDaily.value?.toInt() ?: 1
            )

            withContext(Dispatchers.Main) {
                when {
                    response.isSuccessful -> {
                        if (response.body() != null) {

                            val tempArray = ArrayList<OnlineClassTeacherAttendanceListModel>()

                            response.body()!!.data?.forEach {
                                it.viewModel = this@OnlineClassTeacherAttendanceViewModel
                                it.attendedCount = attendedCount
                                it.absentCount = absentCount
                                it.isAttended = ObservableBoolean(it.attendanceDetails?.isAttended ?: false)
                                tempArray.add(it)
                            }

                            generalDiaryListResponse.value =
                                Resource.success(tempArray)

                            response.body()?.pageSize.let { totalPageDaily.set(it?.toInt() ?: 0) }

                            attendedCount.set(response.body()?.attendedCount ?:0)
                            absentCount.set((response.body()?.count ?: 0) - (response.body()?.attendedCount ?:0))
                        }
                    }
                    else -> generalDiaryListResponse.value = Resource.error(response.message())

                }
            }
        }

    }

    fun putMarkAttendanceTeacher(
        isAttended: Boolean,
        zoomId: Int
    ) {

        GlobalScope.launch {
            repository.putMarkAttendanceTeacher(date.get(), isAttended, zoomId)
        }

    }

    private fun setFilterDate() {
        val calendar = Calendar.getInstance()
        date.set(calendar.timeInMillis)
    }

    fun refreshPage() {

        isPageLoadingDaily.value = true

        isNoDataDaily.set(false)

        currentPageDaily.value = 1

        getAttendanceList()
    }

    fun clear() {

        isPageLoadingDaily.value = true
        isNoDataDaily.set(false)
        currentPageDaily.value = 1
        setFilterDate()
        generalDiaryListResponse = MutableLiveData<Resource<ArrayList<OnlineClassTeacherAttendanceListModel>>>()
    }

}