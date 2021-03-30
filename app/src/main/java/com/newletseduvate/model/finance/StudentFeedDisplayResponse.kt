package com.newletseduvate.model.finance


import com.google.gson.annotations.SerializedName

data class StudentFeedDisplayResponse(
    @SerializedName("fee_type")
    val feeType: Int?,
    @SerializedName("fee_type_name")
    val feeTypeName: String?,
    @SerializedName("fee_type_amount")
    val feeTypeAmount: Double?,
    @SerializedName("installments")
    val installments: ArrayList<Installment>
) {
    data class Installment(
        @SerializedName("installment_name")
        val installmentName: String?,
        @SerializedName("installment_amount")
        val installmentAmount: Double?,
        @SerializedName("due_date")
        val dueDate: String?,
        @SerializedName("amount_paid")
        val amountPaid: Double?,
        var feeTypeName: String?,
        var feeTypeAmount: Double?,
    )
}