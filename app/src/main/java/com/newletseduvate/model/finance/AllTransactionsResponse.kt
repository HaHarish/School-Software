package com.newletseduvate.model.finance


import com.google.gson.annotations.SerializedName

data class AllTransactionsResponse(
    @SerializedName("results")
    var results: ArrayList<Result>?,
    @SerializedName("total")
    var total: Int?
) {
    data class Result(
        @SerializedName("misc_fee")
        var miscFee: Boolean?,
        @SerializedName("transaction_id")
        var transactionId: String?,
        @SerializedName("RecepitNo")
        var recepitNo: String?,
        @SerializedName("date_of_payment")
        var dateOfPayment: String?,
        @SerializedName("payment_mode")
        var paymentMode: String?,
        @SerializedName("cheque_number")
        var chequeNumber: Any?,
        @SerializedName("is_cancelled")
        var isCancelled: Boolean?,
        @SerializedName("is_raised_for_cancellation")
        var isRaisedForCancellation: Boolean?,
        @SerializedName("Fee_type")
        var feeType: List<FeeType?>?,
        @SerializedName("kit_payment")
        var kitPayment: Boolean?,
        @SerializedName("is_app_reg_fee")
        var isAppRegFee: Boolean?
    ) {
        data class FeeType(
            @SerializedName("installment-id")
            var installmentId: Int?,
            @SerializedName("fee-type-installment")
            var feeTypeInstallment: String?,
            @SerializedName("paid_amount")
            var paidAmount: Int?
        )
    }
}