package com.newletseduvate.ui.homework

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
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
import com.newletseduvate.utils.extensions.prepareFilePart
import com.newletseduvate.utils.extensions.snackBar
import com.newletseduvate.utils.oes_tool.NewOesToolViewModel
import com.newletseduvate.viewmodels.HomeworkTeacherSubmitViewModel
import kotlin.collections.ArrayList

class HomeworkTeacherSubmitFragment : BaseFragment(R.layout.fragment_homework_teacher_submit) {

    lateinit var binding: FragmentHomeworkTeacherSubmitBinding
    private val viewModel by lazy { getViewModel<HomeworkTeacherSubmitViewModel>(requireActivity()) }
    private lateinit var oneQuestionAdapter: RecyclerDynamicAdapter<AdapterHomeworkEvaluateOneQuestionBinding, HomeworkTeacherEvaluateOneModel.Question>
    private lateinit var oneAttachmentAdapter: RecyclerDynamicAdapter<AdapterHomeworkEvaluateOneQuestionAttachmentBinding, HomeworkTeacherEvaluateOneAttachmentModel>
    private lateinit var oneEvaluatedAttachmentsAdapter: RecyclerDynamicAdapter<AdapterTeacherHomeworkEvaluateOneEvaluateAttachmentBinding, HomeworkTeacherEvaluateOneQuestionEvaluateAttachmentsModel>

    private lateinit var twoQuestionAdapter: RecyclerDynamicAdapter<AdapterHomeworkEvaluateTwoQuestionBinding, HomeworkTeacherEvaluateTwoModel>

    private val oesViewModel by lazy { getViewModel<NewOesToolViewModel>(requireActivity()) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.clear()

        viewModel.homeworkTeacherEvaluatedModel.value =
            requireArguments().getSerializable("teacherEvaluatedModel") as HomeworkTeacherDetailsEvaluatedResponse.Submitted

        getHomeworkTeacherDetailsEvaluated()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeworkTeacherSubmitBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel

        binding.tvHeading.text = requireArguments().getString("heading", "")

        initDataObserver()

        binding.btnEvaluationDone.setOnClickListener {
            binding.root.snackBar("Saved")
            viewModel.postEvaluationDone(
                binding.etOverallRemark.text.toString(),
                binding.etOverallScore.text.toString()
            )
        }

        binding.btnCancel.setOnClickListener {
            findNavController().popBackStack()
        }

        oesViewModel.isSaved.observe(viewLifecycleOwner, {

            if (it == true) {
                Handler(Looper.getMainLooper()).postDelayed({
                    val path = oesViewModel.imageFiles[0].absolutePath
                    requireContext().prepareFilePart(path, path).let { uploadFileModel ->
                        viewModel.postUploadQuestionFile(uploadFileModel)
                    }
                    oesViewModel.isSaved.value = false
                }, 200)
            }
        })

    }

    private fun initDataObserver() {

        viewModel.homeworkEvaluationDone.observe(viewLifecycleOwner, {
            if (it) {
                viewModel.homeworkEvaluationDone.value = false
                findNavController().popBackStack()
            }
        })

        viewModel.homeworkTeacherEvaluationForBulkType.observe(viewLifecycleOwner, {
            if (it) {
                viewModel.homeworkTeacherEvaluationForBulkType.value = false
                binding.root.snackBar("Saved")
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

                viewModel.attachmentListForBulk.observe(viewLifecycleOwner, {
                    oneEvaluatedAttachmentsAdapter.replaceList(viewModel.attachmentListForBulk.value!!)
                })

                if (viewModel.attachmentListForBulk.value?.size == 0) {
                    homeworkTeacherEvaluateOneModel.correctedFiles?.let { files ->
                        val tempArray =
                            ArrayList<HomeworkTeacherEvaluateOneQuestionEvaluateAttachmentsModel>()
                        files.forEach { file ->
                            tempArray.add(
                                HomeworkTeacherEvaluateOneQuestionEvaluateAttachmentsModel(file)
                            )
                        }
                        viewModel.attachmentListForBulk.value = tempArray
                    }
                }


                binding.etCommentsQuestion.setText(homeworkTeacherEvaluateOneModel.comment.toString())
                binding.etRemarkQuestion.setText(homeworkTeacherEvaluateOneModel.remark.toString())

                binding.btnSave.setOnClickListener {
                    if (viewModel.attachmentListForBulk.value?.size!! >= homeworkTeacherEvaluateOneModel.submittedFiles?.size!!) {
                        viewModel.postTeacherEvaluation(
                            binding.etRemarkQuestion.text.toString(),
                            binding.etCommentsQuestion.text.toString(),
                            homeworkTeacherEvaluateOneModel.id,
                            oneAttachmentAdapter.getItemList()
                        )
                    } else
                        binding.root.snackBar("Please evaluate all the attachments")
                }

            } catch (ex: Exception) {

                viewModel.isBulkUpload.set(false)
                val objectArray = it.data?.data?.hwQuestions as JsonArray

                initTwoAttachmentsAdapter()


                if (viewModel.attachmentListForQuestionWise.size == 0) {
                    objectArray.forEach { objectModel ->
                        val questionWiseEvaluate =
                            MutableLiveData<ArrayList<HomeworkTeacherEvaluateOneQuestionEvaluateAttachmentsModel>>(
                                ArrayList()
                            )
                        viewModel.attachmentListForQuestionWise.add(questionWiseEvaluate)
                    }
                }


                val questionsArray = ArrayList<HomeworkTeacherEvaluateTwoModel>()

                for (i in 0 until objectArray.size()) {
                    val objectModel = objectArray.get(i).asJsonObject

                    val teacher2Model =
                        gson.fromJson(objectModel, HomeworkTeacherEvaluateTwoModel::class.java)

                    teacher2Model.viewModel = viewModel
                    teacher2Model.attachmentListForQuestionWise =
                        viewModel.attachmentListForQuestionWise[i]
                    teacher2Model.viewLifecycleOwner = viewLifecycleOwner
                    teacher2Model.observableComment = ObservableField(teacher2Model.comment)
                    teacher2Model.observableRemark = ObservableField(teacher2Model.remark)
                    questionsArray.add(teacher2Model)
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

            Glide.with(requireContext())
                .asBitmap()
                .load("$MEDIA_HOMEWORK_URL${model.url}")
                .into(object : CustomTarget<Bitmap>() {
                    override fun onResourceReady(
                        resource: Bitmap,
                        transition: Transition<in Bitmap>?
                    ) {

                        val bitmapList = ArrayList<Bitmap>()
                        bitmapList.add(resource)

                        viewModel.currentUploadFilesList = viewModel.attachmentListForBulk

                        val bundle = Bundle()
                        bundle.putParcelableArrayList("bitmaps", bitmapList)
                        findNavController().navigate(R.id.nav_new_oes_tool, bundle)
                    }

                    override fun onLoadCleared(placeholder: Drawable?) {
                    }
                })


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
            requireContext().openMediaFile(MEDIA_HOMEWORK_URL + model.url.toString())
        })

        callbacks.add(CallBackModel(R.id.iv_delete_file) { model, position ->
            viewModel.attachmentListForBulk.value?.removeAt(position)
            viewModel.attachmentListForBulk.value = viewModel.attachmentListForBulk.value

            viewModel.postDeleteFile(model.url.toString())
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
        callbacks.add(CallBackModel(R.id.btn_save) { model, position ->
            if (model.attachmentListForQuestionWise.value?.size!! >= model.submittedFiles?.size!!) {
                viewModel.postTeacherEvaluationQuestionWise(
                    model.observableRemark.get().toString(),
                    model.observableComment.get().toString(),
                    model.questionId,
                    model.attachmentListForQuestionWise.value!!,
                    model.submittedFiles
                )
            } else
                binding.root.snackBar("Please evaluate all the attachments")
        })

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
        fun newInstance() = HomeworkTeacherSubmitFragment()
    }
}