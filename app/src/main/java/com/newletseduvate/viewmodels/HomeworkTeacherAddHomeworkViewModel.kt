package com.newletseduvate.viewmodels

import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.newletseduvate.model.general.UploadFileModel
import com.newletseduvate.model.homeWork.HomeworkTeacherCreateHomeworkQuestionModel
import com.newletseduvate.model.homeWork.HomeworkTeacherModel
import com.newletseduvate.repository.HomeWorkTeacherRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class HomeworkTeacherAddHomeworkViewModel @Inject constructor(private val repository: HomeWorkTeacherRepository) :
    ViewModel() {

    var moduleId = ObservableInt(-1)
    var subjectId = ObservableField("")

    var homeworkTeacherModel = MutableLiveData<HomeworkTeacherModel>()

    var questionList = MutableLiveData<ArrayList<HomeworkTeacherCreateHomeworkQuestionModel>>(ArrayList())

    var filesList = ArrayList<MutableLiveData<ArrayList<String>>>(ArrayList(ArrayList()))

    lateinit var currentUploadFilesList: MutableLiveData<ArrayList<String>>


    fun postUploadQuestionFile(file: UploadFileModel) {

        GlobalScope.launch {

            val response = repository.postUploadQuestionFile(file)

            withContext(Dispatchers.Main) {
                when {
                    response.isSuccessful -> {

                        val tempCurrUploadList = currentUploadFilesList.value
                        tempCurrUploadList?.add(
                            response.body()?.get("data")?.asString.toString()
                        )
                        tempCurrUploadList?.let {
                            currentUploadFilesList.value = it
                        }
                    }
                }
            }
        }
    }


    var homeworkAddQuestion = MutableLiveData(false)

    fun postUploadHomework(
        name: String,
        description: String,
        questionList: ArrayList<HomeworkTeacherCreateHomeworkQuestionModel>,
    ) {

        GlobalScope.launch {

            val response = repository.postUploadHomework(
                name,
                description,
                subjectId.get().toString(),
                homeworkTeacherModel.value?.classDate.toString(),
                questionList
            )

            withContext(Dispatchers.Main) {
                when {
                    response.isSuccessful -> {

                        homeworkAddQuestion.value = true
                    }
                    else -> homeworkAddQuestion.value = false

                }
            }
        }
    }

    fun postDeleteFile(fileName: String) {

        GlobalScope.launch {
            val response = repository.postDeleteFile(fileName)

            withContext(Dispatchers.Main) {
                when {
                    response.isSuccessful -> {

                    }
                }
            }
        }

    }

    fun clear(){
        moduleId.set(-1)
        subjectId.set("")

        homeworkTeacherModel = MutableLiveData<HomeworkTeacherModel>()

        questionList =
            MutableLiveData<ArrayList<HomeworkTeacherCreateHomeworkQuestionModel>>(ArrayList())

        filesList = ArrayList()
    }

}