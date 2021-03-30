package com.newletseduvate.model.homeWork


import androidx.databinding.ObservableField
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import com.google.gson.annotations.SerializedName
import com.newletseduvate.viewmodels.HomeworkTeacherSubmitViewModel

data class HomeworkTeacherEvaluateTwoModel(
    @SerializedName("student_homework_id")
    val studentHomeworkId: Int?,
    @SerializedName("id")
    val id: Int?,
    @SerializedName("question_id")
    val questionId: Int?,
    @SerializedName("question")
    val question: String?,
    @SerializedName("submitted_files")
    val submittedFiles: ArrayList<String>?,
    @SerializedName("evaluated_files")
    val evaluatedFiles: ArrayList<String>?,
    @SerializedName("corrected_files")
    val correctedFiles: ArrayList<String>?,
    @SerializedName("remark")
    val remark: String?,
    @SerializedName("comment")
    val comment: String?,
    var viewModel: HomeworkTeacherSubmitViewModel,
    var attachmentListForQuestionWise: MutableLiveData<ArrayList<HomeworkTeacherEvaluateOneQuestionEvaluateAttachmentsModel>>,
    val position : Int,
    var viewLifecycleOwner: LifecycleOwner,
    var observableRemark : ObservableField<String>,
    var observableComment : ObservableField<String>
)