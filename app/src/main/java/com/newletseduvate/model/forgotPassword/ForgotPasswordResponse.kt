package com.newletseduvate.model.forgotPassword


import com.google.gson.annotations.SerializedName

data class ForgotPasswordResponse(
    @SerializedName("status_code")
    var statusCode: Int?,
    @SerializedName("message")
    var message: String?
)