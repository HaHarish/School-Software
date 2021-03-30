package com.newletseduvate.model.profile


import com.google.gson.annotations.SerializedName

data class ChangePasswordRequest(
    @SerializedName("old_password")
    var oldPassword: String?,
    @SerializedName("new_password")
    var newPassword: String?
)