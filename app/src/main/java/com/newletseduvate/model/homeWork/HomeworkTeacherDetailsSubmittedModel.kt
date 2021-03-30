package com.newletseduvate.model.homeWork

import com.google.gson.annotations.SerializedName

data class HomeworkTeacherDetailsSubmittedModel(
    @SerializedName("homework_id")
    val homeworkId: Int?,
    @SerializedName("id")
    val id: Int?,
    @SerializedName("question")
    val question: String?,
    @SerializedName("question_files")
    val questionFiles: ArrayList<String>?,
    @SerializedName("max_attachment")
    val maxAttachment: Int?,
    @SerializedName("is_pen_editor_enable")
    val isPenEditorEnable: Boolean?
)