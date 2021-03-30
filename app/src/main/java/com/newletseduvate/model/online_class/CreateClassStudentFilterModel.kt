package com.newletseduvate.model.online_class

import androidx.databinding.ObservableBoolean
import com.google.gson.annotations.SerializedName

data class CreateClassStudentFilterModel(
    @SerializedName("id")
    val id: Int?,
    @SerializedName("user")
    val user: User?,
    @SerializedName("erp_id")
    val erpId: String?,
    var isSelected: ObservableBoolean
) {
    data class User(
        @SerializedName("id")
        val id: Int?,
        @SerializedName("username")
        val username: String?,
        @SerializedName("email")
        val email: String?,
        @SerializedName("first_name")
        val firstName: String?
    )
}