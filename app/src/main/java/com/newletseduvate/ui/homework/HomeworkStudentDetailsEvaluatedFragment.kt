package com.newletseduvate.ui.homework

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.newletseduvate.BuildConfig.MEDIA_HOMEWORK_URL
import com.newletseduvate.R
import com.newletseduvate.adapter.dynamic_recycler_adapter.CallBackModel
import com.newletseduvate.adapter.dynamic_recycler_adapter.RecyclerDynamicAdapter
import com.newletseduvate.base.BaseFragment
import com.newletseduvate.databinding.*
import com.newletseduvate.model.homeWork.HomeWorkStudentModel
import com.newletseduvate.model.homeWork.HomeworkStudentDetailsEvaluatedModel
import com.newletseduvate.model.homeWork.HomeworkStudentDetailsEvaluatedSecondModel
import com.newletseduvate.utils.NetworkCheck
import com.newletseduvate.utils.Status
import com.newletseduvate.utils.constants.Constants
import com.newletseduvate.utils.extensions.openMediaFile
import com.newletseduvate.utils.extensions.snackBar
import com.newletseduvate.viewmodels.HomeworkStudentDetailsEvaluatedViewModel

class HomeworkStudentDetailsEvaluatedFragment : BaseFragment(R.layout.fragment_homework_student_details_evaluated) {

    lateinit var binding: FragmentHomeworkStudentDetailsEvaluatedBinding
    private val viewModel by lazy { getViewModel<HomeworkStudentDetailsEvaluatedViewModel>(this) }
    lateinit var adapter : RecyclerDynamicAdapter<AdapterHomeworkDetailsQuestionEvaluatedFirstBinding, HomeworkStudentDetailsEvaluatedModel>
    lateinit var adapter2 : RecyclerDynamicAdapter<AdapterHomeworkDetailsQuestionEvaluatedSecondBinding, HomeworkStudentDetailsEvaluatedSecondModel.Question>
    lateinit var adapterAllEvaluated : RecyclerDynamicAdapter<AdapterHomeworkDetailsQuestionFilesBinding, String>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeworkStudentDetailsEvaluatedBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewModel = viewModel

        binding.tvHeading.text = requireArguments().getString("heading", "")
        viewModel.moduleId.set(requireArguments().getInt(Constants.MODULE_ID))
        viewModel.homeworkStudentModel.value = requireArguments().getSerializable(HomeworkStudentFragment.HomeworkStudentModelConstant) as HomeWorkStudentModel

        initDataObserver()

        getHomeworkStudentDetails()
    }

    private fun initDataObserver() {

        viewModel.homeworkStudentDetails.observe(viewLifecycleOwner, {

            val gson = Gson()

            when (it.status) {
                Status.Success -> {

                    try {

                        initRecyclerView2()
                        initRecyclerViewAllEvaluatedFiles()

                        val tempQuestionList = ArrayList<HomeworkStudentDetailsEvaluatedSecondModel.Question>()

                        for (questionModel in it.data?.data?.hwQuestions?.asJsonObject?.get("questions")!!.asJsonArray) {
                            val model = gson.fromJson(questionModel.toString(), HomeworkStudentDetailsEvaluatedSecondModel.Question::class.java)
                            tempQuestionList.add(model)
                        }

                        adapter2.replaceList(tempQuestionList)

                        if (it?.data?.data?.hwQuestions?.asJsonObject?.get("evaluated_files")!!.asJsonArray?.size() != 0) {


                            val allSubmittedFiles = ArrayList<String>()
                            for (submittedModel in it?.data?.data?.hwQuestions?.asJsonObject?.get("evaluated_files")!!.asJsonArray) {
                                Log.i("homework 2 sub", "$submittedModel")
                                val url = gson.fromJson(submittedModel.toString(), String::class.java)
                                allSubmittedFiles.add(url)
                            }

                            adapterAllEvaluated.replaceList(allSubmittedFiles)

                            binding.allEvaluatedFiles.visibility = View.VISIBLE
                            binding.allEvaluatedFiles.visibility = View.VISIBLE
                        }

                        binding.tvComments.text = it.data.data.hwQuestions.asJsonObject?.get("comment")?.asString
                        binding.tvRemark.text = it.data.data.hwQuestions.asJsonObject?.get("remark")?.asString

                        binding.clComments.visibility = View.VISIBLE
                        binding.clRemark.visibility = View.VISIBLE

                    } catch (ex: Exception) {

                        viewModel.isHWQuestionObject.set(false)

                        initRecyclerView()

                        val tempQuestionList = ArrayList<HomeworkStudentDetailsEvaluatedModel>()

                        for(questionModel in it.data?.data?.hwQuestions?.asJsonArray?.asJsonArray!!){
                            val model = gson.fromJson(questionModel.toString(), HomeworkStudentDetailsEvaluatedModel::class.java)
                            tempQuestionList.add(model)
                        }

                        adapter.replaceList(tempQuestionList)
                    }

                    binding.tvOverallRemark.text = viewModel.homeworkStudentDetails.value?.data?.data?.overallRemark.toString()
                    binding.tvOverallScore.text = viewModel.homeworkStudentDetails.value?.data?.data?.score.toString()

                }

                Status.Error -> {
                }
            }

        })

    }

    private fun initRecyclerView2() {

    val callbacks = ArrayList<CallBackModel<HomeworkStudentDetailsEvaluatedSecondModel.Question>>()

        val linearLayoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        binding.rvList.layoutManager = linearLayoutManager

        adapter2 =
            RecyclerDynamicAdapter(R.layout.adapter_homework_details_question_evaluated_second, callbacks)
        binding.rvList.adapter = adapter2
        binding.rvList.setHasFixedSize(true)
    }

    private fun initRecyclerView() {

        val callbacks = ArrayList<CallBackModel<HomeworkStudentDetailsEvaluatedModel>>()
//        callbacks.add(CallBackModel(R.id.root) { model, position ->
//        })

        val linearLayoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        binding.rvList.layoutManager = linearLayoutManager

        adapter =
                RecyclerDynamicAdapter(R.layout.adapter_homework_details_question_evaluated_first, callbacks)
        binding.rvList.adapter = adapter
        binding.rvList.setHasFixedSize(true)
    }

    private fun initRecyclerViewAllEvaluatedFiles() {

        val linearLayoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        binding.rvLisAllEvaluated.layoutManager = linearLayoutManager

        val callbacks = ArrayList<CallBackModel<String>>()
        callbacks.add(CallBackModel(R.id.root) { model, position ->
            requireContext().openMediaFile("$MEDIA_HOMEWORK_URL$model")
        })

        adapterAllEvaluated =
            RecyclerDynamicAdapter(R.layout.adapter_homework_details_question_files, callbacks)
        binding.rvLisAllEvaluated.adapter = adapterAllEvaluated
        binding.rvLisAllEvaluated.setHasFixedSize(true)
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
            HomeworkStudentDetailsEvaluatedFragment()
    }
}