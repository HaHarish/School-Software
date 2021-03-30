package com.newletseduvate.model.homeWork

import androidx.databinding.ObservableBoolean
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import com.newletseduvate.viewmodels.HomeworkStudentDetailOpenedViewModel

class HomeworkStudentDetailsOpenedModifiedModel(
    val homeworkId: Int,
    val id: Int,
    val question: String?,
    val questionFiles: ArrayList<String>,
    val maxAttachment: Int,
    val isPenEditorEnable: Boolean,
    val isUploadQuestionWise: ObservableBoolean,
    val lifeCycleOwner: LifecycleOwner,
    val viewModel: HomeworkStudentDetailOpenedViewModel,
    var uploadHomeworkList:MutableLiveData<ArrayList<HomeworkUploadQuestionFileModel>>
)