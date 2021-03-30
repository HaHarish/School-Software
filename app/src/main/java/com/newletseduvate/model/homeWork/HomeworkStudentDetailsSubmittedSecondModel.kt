package com.newletseduvate.model.homeWork


import com.google.gson.annotations.SerializedName

data class HomeworkStudentDetailsSubmittedSecondModel(
    @SerializedName("student_homework_id")
    val studentHomeworkId: Int,
    @SerializedName("question_id")
    val questionId: Int,
    @SerializedName("question")
    val question: String,
    @SerializedName("question_files")
    val questionFiles: ArrayList<String>,
    @SerializedName("submitted_files")
    val submittedFiles: ArrayList<String>
)