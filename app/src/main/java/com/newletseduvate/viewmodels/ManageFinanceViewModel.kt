package com.newletseduvate.viewmodels

import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.newletseduvate.model.online_class.ClassTypeModel
import com.newletseduvate.repository.FinanceRepository
import com.newletseduvate.utils.Resource
import com.newletseduvate.model.finance.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ManageFinanceViewModel @Inject constructor(private val repository: FinanceRepository) :
    ViewModel() {

    val getManageFinanceYearResponse = MutableLiveData<Resource<ManageFinanceYearResponse>>()
    val getAllTransactionsResponse = MutableLiveData<Resource<AllTransactionsResponse>>()

    val getFeesDetailsResponse = MutableLiveData<Resource<ArrayList<FeeDetailsResponse.FeeDetailsResponseItem>>>()
    val getMakePaymentResponse = MutableLiveData<Resource<ArrayList<MakePaymentResponse.MakePaymentResponseItem>>>()

    val isPageLoadingStudent = MutableLiveData(true)
    val isNoDataStudent = ObservableBoolean(false)

    var yearList = MutableLiveData<ArrayList<String>>()
    var yearSelected = ObservableField("2021-22")

    fun getManageFinanceYear() {

        GlobalScope.launch {
            val response = repository.getManageFinanceYear()

            withContext(Dispatchers.Main) {
                when {
                    response.isSuccessful -> {
                        if (response.body() != null) {
                            yearList.value = response.body()?.sessionYear!!
                            getManageFinanceYearResponse.value = Resource.success(response.body())
                        }
                    }
                }
            }
        }
    }

    fun getFeeDetails() {

        GlobalScope.launch {
            val response = repository.getFeeDetails(yearSelected.get()!!)

            withContext(Dispatchers.Main) {
                when {
                    response.isSuccessful -> {
                        if (response.body() != null) {
                            getFeesDetailsResponse.value = Resource.success(response.body())
                        }
                    }
                }
            }
        }
    }

    fun getMakePaymentDetails() {

        GlobalScope.launch {
            val response = repository.getMakePayment(yearSelected.get()!!)

            withContext(Dispatchers.Main) {
                when {
                    response.isSuccessful -> {
                        if (response.body() != null) {
                            getMakePaymentResponse.value = Resource.success(response.body())
                        }
                    }
                }
            }
        }
    }

    fun getAllTransactions() {

        GlobalScope.launch {
            val response = repository.getAllTransactions(yearSelected.get()!!)

            withContext(Dispatchers.Main) {
                when {
                    response.isSuccessful -> {
                        if (response.body() != null) {
                            getAllTransactionsResponse.value = Resource.success(response.body())
                        }
                    }
                }
            }
        }
    }
}