package com.newletseduvate.ui.homework

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.newletseduvate.BuildConfig.MEDIA_HOMEWORK_URL
import com.newletseduvate.R
import com.newletseduvate.adapter.dynamic_recycler_adapter.CallBackModel
import com.newletseduvate.adapter.dynamic_recycler_adapter.RecyclerDynamicAdapter
import com.newletseduvate.base.BaseFragment
import com.newletseduvate.databinding.*
import com.newletseduvate.model.homeWork.HomeWorkStudentModel
import com.newletseduvate.model.homeWork.HomeworkStudentDetailsSubmittedSecondModel
import com.newletseduvate.model.homeWork.HomeworkStudentDetailsSubmittedModel
import com.newletseduvate.utils.NetworkCheck
import com.newletseduvate.utils.Status
import com.newletseduvate.utils.constants.Constants
import com.newletseduvate.utils.extensions.openMediaFile
import com.newletseduvate.utils.extensions.snackBar
import com.newletseduvate.viewmodels.HomeworkStudentDetailsSubmittedViewModel

class HomeworkStudentDetailsSubmittedFragment : BaseFragment(R.layout.fragment_homework_student_details_submitted) {

    lateinit var binding: FragmentHomeworkStudentDetailsSubmittedBinding
    private val viewModel by lazy { getViewModel<HomeworkStudentDetailsSubmittedViewModel>(this) }
    lateinit var adapter : RecyclerDynamicAdapter<AdapterHomeworkDetailsQuestionSubmittedBinding, HomeworkStudentDetailsSubmittedModel.Questions>
    lateinit var adapter2 : RecyclerDynamicAdapter<AdapterHomeworkDetailsQuestionSubmittedSecondBinding, HomeworkStudentDetailsSubmittedSecondModel>
    lateinit var adapterAllEvaluated : RecyclerDynamicAdapter<AdapterHomeworkDetailsQuestionFilesBinding, String>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeworkStudentDetailsSubmittedBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.moduleId.set(requireArguments().getInt(Constants.MODULE_ID))
        viewModel.homeworkStudentModel.value = requireArguments().getSerializable(HomeworkStudentFragment.HomeworkStudentModelConstant) as HomeWorkStudentModel
        binding.tvHeading.text = requireArguments().getString("heading", "")

        initDataObserver()

        getHomeworkStudentDetails()
    }

    private fun initDataObserver() {

        viewModel.homeworkStudentDetails.observe(viewLifecycleOwner, {

            val gson = Gson()

            when(it.status){
                Status.Success -> {

                    try {

                        Log.i("homework 2", " ${it.data.toString()}")

                        initRecyclerView2()
                        initRecyclerViewAllEvaluatedFiles()

                        val tempQuestionList = ArrayList<HomeworkStudentDetailsSubmittedSecondModel>()

                        for(questionModel in it.data?.data?.hwQuestions?.asJsonObject?.get("questions")!!.asJsonArray){
                            val model = gson.fromJson(questionModel.toString(), HomeworkStudentDetailsSubmittedSecondModel::class.java)
                            tempQuestionList.add(model)
                        }

                        adapter2.replaceList(tempQuestionList)

                        if(it?.data?.data?.hwQuestions?.asJsonObject?.get("submitted_files")!!.asJsonArray?.size() != 0){


                            val allSubmittedFiles = ArrayList<String>()
                            for(submittedModel in it?.data?.data?.hwQuestions?.asJsonObject?.get("submitted_files")!!.asJsonArray){
                                Log.i("homework 2 sub", "$submittedModel")
                                val url = gson.fromJson(submittedModel.toString(), String::class.java)
                                allSubmittedFiles.add(url)
                            }

                            adapterAllEvaluated.replaceList(allSubmittedFiles)

                            binding.tvSubmittedFiles.visibility = View.VISIBLE
                            binding.rvSubmittedFiles.visibility = View.VISIBLE
                        }


                    }catch (ex : Exception){

                        initRecyclerView()

                        val tempQuestionList = ArrayList<HomeworkStudentDetailsSubmittedModel.Questions>()

                        for(questionModel in it.data?.data?.hwQuestions?.asJsonArray?.asJsonArray!!){
                            val model = gson.fromJson(questionModel.toString(), HomeworkStudentDetailsSubmittedModel.Questions::class.java)
                            tempQuestionList.add(model)
                        }

                        adapter.replaceList(tempQuestionList)

                        Log.i("homework", " ${it.data.toString()}")

//                        viewModel.homeworkStudentDetailsListResponse.observe(viewLifecycleOwner, {
//                            adapter.replaceList(it.data!!)
//                        })


                    }



                }

                Status.Error -> {}
            }

        })


    }

    private fun initRecyclerView() {

        val callbacks = ArrayList<CallBackModel<HomeworkStudentDetailsSubmittedModel.Questions>>()
//        callbacks.add(CallBackModel(R.id.root) { model, position ->
//        })

        val linearLayoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        binding.rvList.layoutManager = linearLayoutManager

        adapter =
            RecyclerDynamicAdapter(R.layout.adapter_homework_details_question_submitted, callbacks)
        binding.rvList.adapter = adapter
        binding.rvList.setHasFixedSize(true)
    }

    private fun initRecyclerView2() {

        val callbacks = ArrayList<CallBackModel<HomeworkStudentDetailsSubmittedSecondModel>>()

        val linearLayoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        binding.rvList.layoutManager = linearLayoutManager

        adapter2 =
            RecyclerDynamicAdapter(R.layout.adapter_homework_details_question_submitted_second, callbacks)
        binding.rvList.adapter = adapter2
        binding.rvList.setHasFixedSize(true)
    }



    private fun initRecyclerViewAllEvaluatedFiles() {

        val linearLayoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        binding.rvSubmittedFiles.layoutManager = linearLayoutManager

        val callbacks = ArrayList<CallBackModel<String>>()
        callbacks.add(CallBackModel(R.id.root) { model, position ->
            Log.i("homesub","$model")
            requireContext().openMediaFile("$MEDIA_HOMEWORK_URL$model")
        })

        adapterAllEvaluated =
            RecyclerDynamicAdapter(R.layout.adapter_homework_details_question_files, callbacks)
        binding.rvSubmittedFiles.adapter = adapterAllEvaluated
        binding.rvSubmittedFiles.setHasFixedSize(true)
    }



    private fun getHomeworkStudentDetails() {
        if (NetworkCheck.isInternetAvailable(requireContext())) {
            viewModel.getHomeworkStudentDetails()
        } else {
            if(::binding.isInitialized){
                binding.root.snackBar(getString(R.string.check_internet), getString(R.string.retry), true) {
                    getHomeworkStudentDetails()
                }
            }
        }
    }


    companion object {

        @JvmStatic
        fun newInstance() =
            HomeworkStudentDetailsSubmittedFragment()
    }
}