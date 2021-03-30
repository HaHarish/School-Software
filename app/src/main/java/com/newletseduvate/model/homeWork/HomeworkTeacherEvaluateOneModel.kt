package com.newletseduvate.model.homeWork

import com.google.gson.annotations.SerializedName


data class HomeworkTeacherEvaluateOneModel(
    @SerializedName("questions")
    val questions: ArrayList<Question>?,
    @SerializedName("id")
    val id: Int?,
    @SerializedName("submitted_files")
    val submittedFiles: ArrayList<String>?,
    @SerializedName("evaluated_files")
    val evaluatedFiles: ArrayList<String>?,
    @SerializedName("corrected_files")
    val correctedFiles: ArrayList<String>?,
    @SerializedName("remark")
    val remark: String?,
    @SerializedName("comment")
    val comment: String?
) {
    data class Question(
        @SerializedName("homework_id")
        val homeworkId: Int?,
        @SerializedName("question")
        val question: String?,
        @SerializedName("question_files")
        val questionFiles: ArrayList<String?>?,
        @SerializedName("max_attachment")
        val maxAttachment: Int?,
        @SerializedName("is_pen_editor_enable")
        val isPenEditorEnable: Boolean?
    )
}