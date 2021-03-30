package com.newletseduvate.model.finance


import com.google.gson.annotations.SerializedName

data class ManageFinanceYearResponse(
    @SerializedName("session_year")
    var sessionYear: ArrayList<String>?
)