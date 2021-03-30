package com.newletseduvate.model.online_class


import com.google.gson.annotations.SerializedName

data class ViewCoursePlanResponse(
    @SerializedName("status_code")
    var statusCode: Int?,
    @SerializedName("message")
    var message: String?,
    @SerializedName("result")
    var result: List<Result?>?
) {
    data class Result(
        @SerializedName("id")
        var id: Int?,
        @SerializedName("course_id")
        var courseId: CourseId?
    ) {
        data class CourseId(
            @SerializedName("id")
            var id: Int?,
            @SerializedName("grade")
            var grade: List<Int?>?,
            @SerializedName("course_name")
            var courseName: String?,
            @SerializedName("level")
            var level: String?,
            @SerializedName("time_slot")
            var timeSlot: List<Any?>?,
            @SerializedName("pre_requirement")
            var preRequirement: String?,
            @SerializedName("overview")
            var overview: String?,
            @SerializedName("learn")
            var learn: String?,
            @SerializedName("files")
            var files: List<String?>?,
            @SerializedName("course_period")
            var coursePeriod: List<CoursePeriod>?,
            @SerializedName("no_of_periods")
            var noOfPeriods: Int?,
            @SerializedName("prices")
            var prices: Prices?,
            @SerializedName("tags")
            var tags: Tags?,
            @SerializedName("batch")
            var batch: Batch?,
            @SerializedName("thumbnail")
            var thumbnail: List<String?>?,
            @SerializedName("is_active")
            var isActive: Boolean?
        ) {
            data class Prices(
                @SerializedName("min_price")
                var minPrice: Any?
            )

            class Tags(
            )

            data class Batch(
                @SerializedName("min_batch_size")
                var minBatchSize: Any?
            )
        }
    }
}