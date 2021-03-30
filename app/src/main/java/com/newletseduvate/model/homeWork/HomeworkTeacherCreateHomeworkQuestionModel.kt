package com.newletseduvate.model.homeWork

import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import com.newletseduvate.viewmodels.HomeworkTeacherAddHomeworkViewModel

class HomeworkTeacherCreateHomeworkQuestionModel(
    val questionAttachments: MutableLiveData<ArrayList<String>>,
    val question: ObservableField<String> = ObservableField(""),
    val isFileUpload : ObservableBoolean = ObservableBoolean(false),
    val isPenToolEnabled : ObservableBoolean = ObservableBoolean(false),
    val viewModel: HomeworkTeacherAddHomeworkViewModel,
    val lifecycleOwner: LifecycleOwner,
    val maximumNumberOfFiles :ObservableInt =  ObservableInt(0)
)