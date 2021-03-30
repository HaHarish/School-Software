package com.newletseduvate.model.homeWork


import com.google.gson.annotations.SerializedName

data class HomeworkStudentDetailsEvaluatedModel(
    @SerializedName("student_homework_id")
    val studentHomeworkId: Int,
    @SerializedName("question_id")
    val questionId: Int,
    @SerializedName("question")
    val question: String?,
    @SerializedName("question_files")
    val questionFiles: ArrayList<String>?,
    @SerializedName("evaluated_files")
    val evaluatedFiles: ArrayList<String>?,
    @SerializedName("remark")
    val remark: String?
)