package com.newletseduvate.model.finance


import com.google.gson.annotations.SerializedName

class MakePaymentResponse : ArrayList<MakePaymentResponse.MakePaymentResponseItem>(){
    data class MakePaymentResponseItem(
        @SerializedName("fee_account_id")
        var feeAccountId: Int?,
        @SerializedName("fee_account_name")
        var feeAccountName: String?,
        @SerializedName("Installments_data")
        var installmentsData: ArrayList<InstallmentsData>?
    ) {
        data class InstallmentsData(
            @SerializedName("id")
            var id: Int?,
            @SerializedName("fee_type_id")
            var feeTypeId: Int?,
            @SerializedName("fee_type")
            var feeType: String?,
            @SerializedName("fee_type_amount")
            var feeTypeAmount: Int?,
            @SerializedName("installments_id")
            var installmentsId: Int?,
            @SerializedName("installments_name")
            var installmentsName: String?,
            @SerializedName("installment_amount")
            var installmentAmount: Int?,
            @SerializedName("fine_amount")
            var fineAmount: Int?,
            @SerializedName("due_date")
            var dueDate: String?,
            @SerializedName("amount_paid")
            var amountPaid: Int?,
            @SerializedName("discount")
            var discount: Int?,
            @SerializedName("balance")
            var balance: Int?,
            @SerializedName("total")
            var total: Int?,
            @SerializedName("is_other_fee")
            var isOtherFee: Boolean?,
            @SerializedName("upload_status")
            var uploadStatus: Int?
        )
    }
}