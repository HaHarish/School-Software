package com.newletseduvate.api

import com.newletseduvate.model.forgotPassword.ForgotPasswordResponse
import com.newletseduvate.utils.constants.ApiConstants
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ForgotPasswordService {

    @GET(ApiConstants.forgotPasswordUrl)
    suspend fun getForgotPassword(
        @Query("erp_id") erpId: String
    ): Response<ForgotPasswordResponse>

}