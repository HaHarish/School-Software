package com.newletseduvate.viewmodels

import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.newletseduvate.model.BottomSheetItem
import com.newletseduvate.model.homeWork.HomeWorkStudentModel
import com.newletseduvate.model.homeWork.HomeworkStudentResponse
import com.newletseduvate.repository.HomeWorkStudentRepository
import com.newletseduvate.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

class HomeworkStudentViewModel @Inject constructor(private val repository: HomeWorkStudentRepository) :
    ViewModel() {

    var moduleId = ObservableInt()

    val filterClicked = ObservableBoolean(false)

    var currentPageStudent = MutableLiveData(1)
    val isPageLoadingStudent = MutableLiveData(true)
    val isNoDataStudent = ObservableBoolean(false)

    val fromDate = ObservableField(0L)
    val toDate = ObservableField(0L)

    var subjectList = MutableLiveData<ArrayList<BottomSheetItem>>()
    var subjectId = ObservableInt(-1)
    var subjectName = ObservableField("")

    var studentHomeWorkListResponse =
        MutableLiveData<Resource<HomeworkStudentResponse>>()
    var studentHomeWorkList =
        MutableLiveData<Resource<ArrayList<HomeWorkStudentModel>>>()

    init {
        setFilterDate()
    }

    fun getStudentHomeWork() {

        GlobalScope.launch {
            val response = repository.getHomeWorkStudent(
                moduleId.get().toString(),
                fromDate.get()!!,
                toDate.get()!!
            )

            withContext(Dispatchers.Main) {
                isPageLoadingStudent.value = false
                when {
                    response.isSuccessful -> {

                        if (response.body() != null && response.body()!!.data != null) {

                            studentHomeWorkListResponse.value = Resource.success(response.body())

                            response.body()!!.data!!.header?.let {

                                val subjects = ArrayList<BottomSheetItem>()
                                it.mandatorySubjects?.forEach { subject ->
                                    subjects.add(BottomSheetItem(subject.id, subject.subjectSlag))
                                }

                                it.optionalSubjects?.forEach { subject ->
                                    subjects.add(BottomSheetItem(subject.id, subject.subjectSlag))
                                }

                                it.othersSubjects?.forEach { subject ->
                                    subjects.add(BottomSheetItem(subject.id, subject.subjectSlag))
                                }

                                subjectList.value = subjects
                            }
                        }

                    }
                    else -> {
                        studentHomeWorkListResponse.value = Resource.error(response.message())
                    }
                }
            }
        }

    }

    fun refreshPage() {

        filterClicked.set(false)

        isPageLoadingStudent.value = true

        isNoDataStudent.set(false)
        currentPageStudent.value = 1

        getStudentHomeWork()
    }

    fun clear() {

        filterClicked.set(false)

        isPageLoadingStudent.value = true

        isNoDataStudent.set(false)

        setFilterDate()

        studentHomeWorkListResponse =
            MutableLiveData<Resource<HomeworkStudentResponse>>()

        studentHomeWorkList =
            MutableLiveData<Resource<ArrayList<HomeWorkStudentModel>>>()
    }

    private fun setFilterDate() {
        val calendar = Calendar.getInstance()
        fromDate.set(calendar.timeInMillis)

        calendar.add(Calendar.DAY_OF_YEAR, 6)
        toDate.set(calendar.timeInMillis)
    }
}