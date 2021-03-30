package com.newletseduvate.ui.homework

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.newletseduvate.BuildConfig.MEDIA_HOMEWORK_URL
import com.newletseduvate.R
import com.newletseduvate.adapter.dynamic_recycler_adapter.CallBackModel
import com.newletseduvate.adapter.dynamic_recycler_adapter.RecyclerDynamicAdapter
import com.newletseduvate.base.BaseFragment
import com.newletseduvate.databinding.*
import com.newletseduvate.model.homeWork.*
import com.newletseduvate.utils.NetworkCheck
import com.newletseduvate.utils.extensions.openMediaFile
import com.newletseduvate.utils.extensions.snackBar
import com.newletseduvate.viewmodels.HomeworkTeacherEvaluateViewModel

class HomeworkTeacherEvaluateFragment : BaseFragment(R.layout.fragment_homework_teacher_evaluate) {

    lateinit var binding: FragmentHomeworkTeacherEvaluateBinding
    private val viewModel by lazy { getViewModel<HomeworkTeacherEvaluateViewModel>(requireActivity()) }
    private lateinit var oneQuestionAdapter: RecyclerDynamicAdapter<AdapterHomeworkEvaluateOneQuestionBinding, HomeworkTeacherEvaluateOneModel.Question>
    private lateinit var oneAttachmentAdapter: RecyclerDynamicAdapter<AdapterHomeworkEvaluateOneQuestionAttachmentBinding, HomeworkTeacherEvaluateOneAttachmentModel>
    private lateinit var oneEvaluatedAttachmentsAdapter: RecyclerDynamicAdapter<AdapterTeacherHomeworkEvaluateOneEvaluateAttachmentBinding, HomeworkTeacherEvaluateOneQuestionEvaluateAttachmentsModel>

    private lateinit var twoQuestionAdapter: RecyclerDynamicAdapter<AdapterHomeworkEvaluateTwoQuestionBinding, HomeworkTeacherEvaluateTwoModel>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeworkTeacherEvaluateBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel

        viewModel.homeworkTeacherEvaluatedModel.value =
            requireArguments().getSerializable("teacherEvaluatedModel") as HomeworkTeacherDetailsEvaluatedResponse.Evaluated

        initDataObserver()

        getHomeworkTeacherDetailsEvaluated()


        binding.btnEvaluationDone.setOnClickListener {
            viewModel.postEvaluationDone(
                binding.etOverallRemark.text.toString(),
                binding.etOverallScore.text.toString()
            )
        }
    }

    private fun initDataObserver() {

        viewModel.homeworkEvaluationDone.observe(viewLifecycleOwner, {
            if(it){
                viewModel.homeworkEvaluationDone.value = false
                findNavController().popBackStack()
            }
        })

        viewModel.teacherHomeWorkEvaluateResponse.observe(viewLifecycleOwner, {

            binding.etOverallRemark.setText(it.data?.data?.overallRemark.toString())
            binding.etOverallScore.setText(it.data?.data?.score.toString())

            val gson = Gson()

            try {

                val objectModel = it.data?.data?.hwQuestions as JsonObject
                viewModel.isBulkUpload.set(true)

                val homeworkTeacherEvaluateOneModel =
                    gson.fromJson(objectModel, HomeworkTeacherEvaluateOneModel::class.java)

                initOneQuestionAdapter()
                initOneAttachmentsAdapter()
                initOneEvaluatedAttachmentsAdapter()

                homeworkTeacherEvaluateOneModel.questions?.let { questionList ->
                    oneQuestionAdapter.replaceList(questionList)
                }

                homeworkTeacherEvaluateOneModel.submittedFiles?.let { files ->
                    val attachmentList = ArrayList<HomeworkTeacherEvaluateOneAttachmentModel>()
                    files.forEach { file ->
                        attachmentList.add(HomeworkTeacherEvaluateOneAttachmentModel(file, true))
                    }
                    oneAttachmentAdapter.replaceList(attachmentList)
                }

                homeworkTeacherEvaluateOneModel.correctedFiles?.let { files ->
                    val attachmentList =
                        ArrayList<HomeworkTeacherEvaluateOneQuestionEvaluateAttachmentsModel>()
                    files.forEach { file ->
                        attachmentList.add(
                            HomeworkTeacherEvaluateOneQuestionEvaluateAttachmentsModel(file)
                        )
                    }
                    oneEvaluatedAttachmentsAdapter.replaceList(attachmentList)
                }

            } catch (ex: Exception) {

                viewModel.isBulkUpload.set(false)
                val objectArray = it.data?.data?.hwQuestions as JsonArray

                initTwoAttachmentsAdapter()

                val questionsArray = ArrayList<HomeworkTeacherEvaluateTwoModel>()

                objectArray.forEach { objectModel ->
                    val homeworkTeacherEvaluateTwoModel =
                        gson.fromJson(objectModel, HomeworkTeacherEvaluateTwoModel::class.java)
                    questionsArray.add(homeworkTeacherEvaluateTwoModel)
                }

                twoQuestionAdapter.replaceList(questionsArray)

            }


        })

    }


    private fun initOneQuestionAdapter() {
        val callbacks = ArrayList<CallBackModel<HomeworkTeacherEvaluateOneModel.Question>>()

        val linearLayoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        binding.rvQuestionList.layoutManager = linearLayoutManager

        oneQuestionAdapter =
            RecyclerDynamicAdapter(R.layout.adapter_homework_evaluate_one_question, callbacks)
        binding.rvQuestionList.adapter = oneQuestionAdapter
        binding.rvQuestionList.setHasFixedSize(true)
    }

    private fun initOneAttachmentsAdapter() {
        val callbacks = ArrayList<CallBackModel<HomeworkTeacherEvaluateOneAttachmentModel>>()

        callbacks.add(CallBackModel(R.id.root) { model, position ->
            requireContext().openMediaFile(MEDIA_HOMEWORK_URL + model.url)
        })

        callbacks.add(CallBackModel(R.id.iv_pen_tool) { model, position ->

        })

        val linearLayoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        binding.rvAttachments.layoutManager = linearLayoutManager

        oneAttachmentAdapter =
            RecyclerDynamicAdapter(
                R.layout.adapter_homework_evaluate_one_question_attachment,
                callbacks
            )
        binding.rvAttachments.adapter = oneAttachmentAdapter
        binding.rvAttachments.setHasFixedSize(true)
    }

    private fun initOneEvaluatedAttachmentsAdapter() {
        val callbacks =
            ArrayList<CallBackModel<HomeworkTeacherEvaluateOneQuestionEvaluateAttachmentsModel>>()

        callbacks.add(CallBackModel(R.id.layout_file_item) { model, position ->
            requireContext().openMediaFile(model.url.toString())
        })

        callbacks.add(CallBackModel(R.id.iv_delete_file) { model, position ->
            oneEvaluatedAttachmentsAdapter.removeItemAt(position)
        })

        val linearLayoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        binding.rvEvaluatedAttachments.layoutManager = linearLayoutManager

        oneEvaluatedAttachmentsAdapter =
            RecyclerDynamicAdapter(
                R.layout.adapter_teacher_homework_evaluate_one_evaluate_attachment,
                callbacks
            )
        binding.rvEvaluatedAttachments.adapter = oneEvaluatedAttachmentsAdapter
        binding.rvEvaluatedAttachments.setHasFixedSize(true)
    }

    private fun initTwoAttachmentsAdapter() {
        val callbacks = ArrayList<CallBackModel<HomeworkTeacherEvaluateTwoModel>>()

        val linearLayoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        binding.rvQuestionListTwo.layoutManager = linearLayoutManager

        twoQuestionAdapter =
            RecyclerDynamicAdapter(R.layout.adapter_homework_evaluate_two_question, callbacks)
        binding.rvQuestionListTwo.adapter = twoQuestionAdapter
        binding.rvQuestionListTwo.setHasFixedSize(true)
    }


    private fun getHomeworkTeacherDetailsEvaluated() {
        if (NetworkCheck.isInternetAvailable(requireContext())) {
            viewModel.getHomeworkTeacherEvaluate()
        } else {
            if (::binding.isInitialized) {
                binding.root.snackBar(
                    getString(R.string.check_internet),
                    getString(R.string.retry),
                    true
                ) {
                    getHomeworkTeacherDetailsEvaluated()
                }
            }
        }
    }


    companion object {
        @JvmStatic
        fun newInstance() = HomeworkTeacherEvaluateFragment()
    }
}