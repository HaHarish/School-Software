package com.newletseduvate.model.profile


import com.google.gson.annotations.SerializedName

data class ChangeProfilePictureResponse(
    @SerializedName("status_code")
    var statusCode: Int?,
    @SerializedName("message")
    var message: String?,
    @SerializedName("data")
    var `data`: Data?
) {
    class Data(
    )
}