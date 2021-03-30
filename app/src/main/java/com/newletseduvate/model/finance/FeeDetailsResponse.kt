package com.newletseduvate.model.finance


import com.google.gson.annotations.SerializedName

class FeeDetailsResponse : ArrayList<FeeDetailsResponse.FeeDetailsResponseItem>(){
    data class FeeDetailsResponseItem(
        @SerializedName("id")
        var id: Int?,
        @SerializedName("fee_type")
        var feeType: FeeType?,
        @SerializedName("fee_type_amount")
        var feeTypeAmount: Double?,
        @SerializedName("installments")
        var installments: Installments?,
        @SerializedName("amount_paid")
        var amountPaid: Double?,
        @SerializedName("discount")
        var discount: Double?,
        @SerializedName("balance")
        var balance: Double?,
        @SerializedName("total")
        var total: Double?,
        @SerializedName("updated_by")
        var updatedBy: Any?
    ) {
        data class FeeType(
            @SerializedName("id")
            var id: Int?,
            @SerializedName("fee_type_name")
            var feeTypeName: String?,
            @SerializedName("updated_by")
            var updatedBy: Any?
        )
    
        data class Installments(
            @SerializedName("id")
            var id: Int?,
            @SerializedName("installment_name")
            var installmentName: String?,
            @SerializedName("installment_amount")
            var installmentAmount: Double?,
            @SerializedName("due_date")
            var dueDate: String?,
            @SerializedName("updated_by")
            var updatedBy: Any?
        )
    }
}