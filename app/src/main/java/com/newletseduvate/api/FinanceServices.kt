package com.newletseduvate.api

import com.google.gson.JsonArray
import com.newletseduvate.model.finance.*
import com.newletseduvate.utils.constants.ApiConstants
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface FinanceServices {

    @GET(ApiConstants.financeStudentFeeDisplayUrl)
    suspend fun getStudentFeeDisplay(
        @Header(ApiConstants.AUTHORIZATION) token: String
    ): Response<JsonArray>

    @GET(ApiConstants.financeGetYearUrl)
    suspend fun getManageFinanceYear(
        @Header(ApiConstants.AUTHORIZATION) token: String
    ): Response<ManageFinanceYearResponse>

    @GET(ApiConstants.financeFeeDetails)
    suspend fun getFeeDetails(
        @Header(ApiConstants.AUTHORIZATION) token: String,
        @Query ("academic_year") academic_year: String
    ): Response<FeeDetailsResponse>

    @GET(ApiConstants.financeMakePayment)
    suspend fun getMakePaymentDetails(
        @Header(ApiConstants.AUTHORIZATION) token: String,
        @Query ("academic_year") academic_year: String
    ): Response<MakePaymentResponse>

    @GET(ApiConstants.financeAllTransactions)
    suspend fun getAllTransactionsDetails(
        @Header(ApiConstants.AUTHORIZATION) token: String,
        @Query ("session_year") academic_year: String
    ): Response<AllTransactionsResponse>

}