package com.newletseduvate.repository

import android.content.SharedPreferences
import com.google.gson.JsonArray
import com.newletseduvate.api.FinanceServices
import com.newletseduvate.model.finance.AllTransactionsResponse
import com.newletseduvate.model.finance.FeeDetailsResponse
import com.newletseduvate.model.finance.MakePaymentResponse
import com.newletseduvate.model.finance.ManageFinanceYearResponse
import com.newletseduvate.utils.extensions.getToken
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FinanceRepository @Inject constructor(
    private val apiService: FinanceServices,
    private val pref: SharedPreferences
) {

    suspend fun getStudentFeeDisplay(): Response<JsonArray> {
        return apiService.getStudentFeeDisplay(pref.getToken().toString())
    }

    suspend fun getManageFinanceYear(): Response<ManageFinanceYearResponse> {
        return apiService.getManageFinanceYear(pref.getToken().toString())
    }

    suspend fun getFeeDetails(academicYear: String): Response<FeeDetailsResponse> {
        return apiService.getFeeDetails(pref.getToken().toString(), academicYear)
    }

    suspend fun getMakePayment(academicYear: String): Response<MakePaymentResponse> {
        return apiService.getMakePaymentDetails(pref.getToken().toString(), academicYear) }

    suspend fun getAllTransactions(academicYear: String): Response<AllTransactionsResponse> {
        return apiService.getAllTransactionsDetails(pref.getToken().toString(), academicYear) }

}