package com.newletseduvate.model.homeWork

import com.google.gson.annotations.SerializedName

data class HomeWorkStudentRequest (
        @SerializedName("module_id") var moduleId: String? = null,
        @SerializedName("start_date") var startDate: String? = null,
        @SerializedName("end_date") var endDate: String? = null
)
