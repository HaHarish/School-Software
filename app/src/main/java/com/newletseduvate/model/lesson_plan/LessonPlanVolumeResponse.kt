package com.newletseduvate.model.lesson_plan


import com.google.gson.annotations.SerializedName

data class LessonPlanVolumeResponse(
    @SerializedName("status_code")
    var statusCode: Int?,
    @SerializedName("message")
    var message: String?,
    @SerializedName("result")
    var result: MainResult?
) {
    data class MainResult(
        @SerializedName("results")
        var results: List<SubResult?>?
    ) {
        data class SubResult(
            @SerializedName("id")
            var id: Int?,
            @SerializedName("volume_name")
            var volumeName: String?,
            @SerializedName("is_delete")
            var isDelete: Boolean?,
            @SerializedName("created_by")
            var createdBy: Any?
        )
    }
}