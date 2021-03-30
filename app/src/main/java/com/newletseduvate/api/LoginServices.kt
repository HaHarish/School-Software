package com.newletseduvate.api

import com.newletseduvate.model.login.LoginRequest
import com.newletseduvate.model.login.LoginSuccessResponse
import com.newletseduvate.utils.constants.ApiConstants
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface LoginServices {

    @Headers("Content-Type: application/json")
    @POST(ApiConstants.loginUrl)
    suspend fun loginAsync(@Body loginRequest: LoginRequest): Response<LoginSuccessResponse>

}