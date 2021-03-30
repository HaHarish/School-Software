package com.newletseduvate.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.newletseduvate.model.online_class.OnlineClassAttendResourceModel
import com.newletseduvate.repository.OnlineClassRepository
import com.newletseduvate.ui.online_class.OnlineClassAttendResourceFragment.Companion.textNotAvailableConstant
import com.newletseduvate.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


class OnlineClassAttendResourceViewModel @Inject constructor(private val repository: OnlineClassRepository) :
    ViewModel() {

    var onlineClassListResponse =
        MutableLiveData<Resource<ArrayList<OnlineClassAttendResourceModel>>>()

    fun getStudentOnlineClassResourceFiles(onlineClassId: String, classDate: String) {

        GlobalScope.launch {
            val response = repository.getStudentOnlineClassResourceFiles(onlineClassId, classDate)

            withContext(Dispatchers.Main) {
                when {
                    response.isSuccessful -> {

                        if (response.body() != null) {
                            if (response.body()!!
                                    .get("message").asString == "Resource is not available"
                            ) {
                                val tempList = ArrayList<OnlineClassAttendResourceModel>()

                                val noData = ArrayList<String?>()
                                noData.add(textNotAvailableConstant)
                                tempList.add(
                                    OnlineClassAttendResourceModel(
                                        noData,
                                        null,
                                        classDate,
                                        null,
                                        null,
                                        null
                                    )
                                )
                                onlineClassListResponse.value = Resource.success(tempList)
                            } else {
                                val tempList = ArrayList<OnlineClassAttendResourceModel>()

                                response.body()!!
                                    .get("result").asJsonArray.forEach {
                                        val gson = Gson()
                                        val objectData =
                                            gson.fromJson(it.toString(), OnlineClassAttendResourceModel::class.java)

                                        if(objectData.files != null)
                                            tempList.add(objectData)
                                    }

                                onlineClassListResponse.value = Resource.success(tempList)
                            }

                        }

                    }
                    else -> {
                        onlineClassListResponse.value = Resource.error(response.message())
                    }
                }
            }

        }

    }

}