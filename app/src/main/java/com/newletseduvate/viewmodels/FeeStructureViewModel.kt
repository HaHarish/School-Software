package com.newletseduvate.viewmodels

import androidx.databinding.ObservableBoolean
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.newletseduvate.model.finance.StudentFeedDisplayResponse
import com.newletseduvate.repository.FinanceRepository
import com.newletseduvate.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FeeStructureViewModel @Inject constructor(private val repository: FinanceRepository) :
    ViewModel() {


    var financeFeeListResponse = MutableLiveData<Resource<ArrayList<StudentFeedDisplayResponse>>>()

    val isPageLoadingStudent = MutableLiveData(true)
    val isNoDataStudent = ObservableBoolean(false)

    val gson = Gson()


    fun getStudentFeeDisplay() {

        GlobalScope.launch {
            val response = repository.getStudentFeeDisplay()

            withContext(Dispatchers.Main) {
                when {
                    response.isSuccessful -> {
                        if (response.body() != null) {
                            val tempList = ArrayList<StudentFeedDisplayResponse>()

                            response.body()?.forEach { fee ->
                                val feeModel =  gson.fromJson(fee.toString(), StudentFeedDisplayResponse::class.java)
                                tempList.add(feeModel)
                            }

                            financeFeeListResponse.value =
                                Resource.success(tempList)
                        }
                    }
                    else -> financeFeeListResponse.value = Resource.error(response.message())

                }
            }
        }

    }

}
