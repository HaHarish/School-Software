package com.newletseduvate.ui.bindings

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.newletseduvate.BuildConfig.MEDIA_HOMEWORK_URL
import com.newletseduvate.R
import com.newletseduvate.adapter.dynamic_recycler_adapter.CallBackModel
import com.newletseduvate.adapter.dynamic_recycler_adapter.RecyclerDynamicAdapter
import com.newletseduvate.databinding.*
import com.newletseduvate.model.homeWork.*
import com.newletseduvate.utils.extensions.FileExtension
import com.newletseduvate.utils.extensions.openMediaFile
import com.newletseduvate.viewmodels.HomeworkTeacherSubmitViewModel


object BindingsHomeworkStudentDetails {

    @JvmStatic
    @BindingAdapter(value = ["bindRecyclerViewWithHomeworkStudentDetails"], requireAll = true)
    fun bindRecyclerViewWithHomeworkStudentDetails(
        recyclerView: RecyclerView,
        arrayList: ArrayList<String>?
    ) {

        arrayList?.let {
            val linearLayoutManager =
                LinearLayoutManager(recyclerView.context, RecyclerView.VERTICAL, false)
            recyclerView.layoutManager = linearLayoutManager

            val callbacks = ArrayList<CallBackModel<String>>()
            callbacks.add(CallBackModel(R.id.root) { model, position ->
                recyclerView.context.openMediaFile("$MEDIA_HOMEWORK_URL$model")
            })

            val adapter =
                RecyclerDynamicAdapter<AdapterHomeworkDetailsQuestionFilesBinding, String>(
                    R.layout.adapter_homework_details_question_files,
                    callbacks
                )
            recyclerView.adapter = adapter
            recyclerView.setHasFixedSize(true)

            adapter.submitList(arrayList)
        }

    }

    @JvmStatic
    @BindingAdapter(
        value = ["bindHomeworkQuestionOpenModel"],
        requireAll = true
    )
    fun bindHomeworkQuestionOpenModel(
        recyclerView: RecyclerView,
        questionModel: HomeworkStudentDetailsOpenedModifiedModel
    ) {

        val viewModel = questionModel.viewModel

        questionModel.questionFiles.let {

            val tempArray = ArrayList<HomeworkStudentDetailsOpenedCardModel>()

            questionModel.questionFiles.forEach {
                tempArray.add(
                    HomeworkStudentDetailsOpenedCardModel(
                        it,
                        questionModel.isPenEditorEnable
                    )
                )
            }

            val linearLayoutManager =
                LinearLayoutManager(recyclerView.context, RecyclerView.VERTICAL, false)
            recyclerView.layoutManager = linearLayoutManager

            val callbacks = ArrayList<CallBackModel<HomeworkStudentDetailsOpenedCardModel>>()
            callbacks.add(CallBackModel(R.id.root) { model, position ->
                recyclerView.context.openMediaFile("$MEDIA_HOMEWORK_URL${model.url}")
            })

            callbacks.add(CallBackModel(R.id.iv_pen_tool) { model, position ->

                Glide.with(recyclerView.context)
                    .asBitmap()
                    .load("$MEDIA_HOMEWORK_URL${model.url}")
                    .into(object : CustomTarget<Bitmap>() {
                        override fun onResourceReady(
                            resource: Bitmap,
                            transition: Transition<in Bitmap>?
                        ) {

                            val bitmapList = ArrayList<Bitmap>()
                            bitmapList.add(resource)

                            if (!viewModel.isUploadQuestionWise.get()) {
                                viewModel.currentUploadFilesList = viewModel.uploadFilesList
                            } else {
                                viewModel.currentUploadFilesList = questionModel.uploadHomeworkList
                            }

                            val bundle = Bundle()
                            bundle.putParcelableArrayList("bitmaps", bitmapList)
                            recyclerView.findNavController().navigate(R.id.nav_new_oes_tool, bundle)
                        }

                        override fun onLoadCleared(placeholder: Drawable?) {
                        }
                    })


            })

            val adapter =
                RecyclerDynamicAdapter<AdapterHomeworkDetailsQuestionOpenedCardBinding, HomeworkStudentDetailsOpenedCardModel>(
                    R.layout.adapter_homework_details_question_opened_card,
                    callbacks
                )

            recyclerView.adapter = adapter
            recyclerView.setHasFixedSize(true)

            adapter.submitList(tempArray)
        }

    }

    @JvmStatic
    @BindingAdapter(
        value = ["bindRecyclerWithHomeworkStudentDetailsOpenedModifiedModel"],
        requireAll = true
    )
    fun bindRecyclerWithHomeworkStudentDetailsOpenedModifiedModel(
        recyclerView: RecyclerView,
        homeWorkModifiedModel: HomeworkStudentDetailsOpenedModifiedModel
    ) {

        var adapterFiles: RecyclerDynamicAdapter<AdapterStudentHomeworkUploadFileBinding, HomeworkUploadQuestionFileModel>? =
            null
        val callbacks = ArrayList<CallBackModel<HomeworkUploadQuestionFileModel>>()
        callbacks.add(CallBackModel(R.id.iv_delete_file) { model, position ->
            adapterFiles?.removeItemAt(position)
            model.url?.let { homeWorkModifiedModel.viewModel.postDeleteFile(it) }
        })

        callbacks.add(CallBackModel(R.id.root) { model, position ->
            model.url?.let { recyclerView.context.openMediaFile(MEDIA_HOMEWORK_URL + it) }
        })

        adapterFiles =
            RecyclerDynamicAdapter(
                R.layout.adapter_student_homework_upload_file,
                callbacks
            )
        val layoutManager = LinearLayoutManager(recyclerView.context, RecyclerView.VERTICAL, false)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapterFiles

        homeWorkModifiedModel.uploadHomeworkList.observe(homeWorkModifiedModel.lifeCycleOwner, {
            adapterFiles.replaceList(it)
        })

    }


    @JvmStatic
    @BindingAdapter(value = ["bindHomeworkImageViewByUrl"], requireAll = true)
    fun bindImageViewByUrl(imageView: ImageView, url: String?) {
        Glide.with(imageView.context)
            .load("$MEDIA_HOMEWORK_URL$url")
            .centerCrop()
            .placeholder(R.drawable.image_shape_empty)
            .into(imageView)

    }

    @JvmStatic
    @BindingAdapter(value = ["bindHomeworkPenToolIconEnable"], requireAll = true)
    fun bindHomeworkPenToolIconEnable(
        imageView: ImageView,
        model: HomeworkStudentDetailsOpenedCardModel
    ) {

        if (FileExtension.isImageFile(model.url)) {

            if (model.isPenToolEnabled)
                imageView.visibility = View.VISIBLE
            else
                imageView.visibility = View.GONE

        } else
            imageView.visibility = View.GONE

    }


    @JvmStatic
    @BindingAdapter(value = ["bindHomeworkTeacherEvaluatePenToolIconEnable"], requireAll = true)
    fun bindHomeworkTeacherEvaluatePenToolIconEnable(
        imageView: ImageView,
        model: HomeworkTeacherEvaluateOneAttachmentModel
    ) {

        if (FileExtension.isImageFile(model.url)) {

            if (model.isPenToolEnabled)
                imageView.visibility = View.VISIBLE
            else
                imageView.visibility = View.GONE

        } else
            imageView.visibility = View.GONE

    }

    @JvmStatic
    @BindingAdapter(value = ["bindHomeworkTeacherEvaluateTwoPenToolIconEnable"], requireAll = true)
    fun bindHomeworkTeacherEvaluateTwoPenToolIconEnable(
        imageView: ImageView,
        model: HomeworkTeacherEvaluateTwoAttachmentFilesModel
    ) {

        if (FileExtension.isImageFile(model.url.toString())) {

            if (model.isPenToolEnabled)
                imageView.visibility = View.VISIBLE
            else
                imageView.visibility = View.GONE

        } else
            imageView.visibility = View.GONE

    }

    @JvmStatic
    @BindingAdapter(value = ["bindVisibilityBasedOnMediaType"], requireAll = true)
    fun bindVisibilityBasedOnMediaType(textView: TextView, url: String) {

        if (FileExtension.isImageFile(url)) {
            textView.visibility = View.GONE
        } else
            textView.visibility = View.VISIBLE

    }


    @JvmStatic
    @BindingAdapter(value = ["bindIconForFileType"], requireAll = true)
    fun bindIconForFileType(imageView: ImageView, url: String?) {
        url?.let {
            when {
                url.endsWith(".pdf") -> {
                    imageView.setImageDrawable(
                        ContextCompat.getDrawable(
                            imageView.context,
                            R.drawable.ic_pdf_file
                        )
                    )
                }

                FileExtension.isImageFile(url) -> {

                    imageView.layoutParams.height = 220
                    imageView.layoutParams.width = 220


                    Glide.with(imageView.context)
                        .load("$MEDIA_HOMEWORK_URL$url")
                        .centerCrop()
                        .placeholder(R.drawable.image_shape_empty)
                        .into(imageView)

                }

                FileExtension.isVideoFile(url) -> {
                    imageView.setImageDrawable(
                        ContextCompat.getDrawable(
                            imageView.context,
                            R.drawable.ic_video_file
                        )
                    )
                }

                FileExtension.isAudioFile(url) -> {
                    imageView.setImageDrawable(
                        ContextCompat.getDrawable(
                            imageView.context,
                            R.drawable.ic_audio_file
                        )
                    )
                }

                else -> {
                    imageView.setImageDrawable(
                        ContextCompat.getDrawable(
                            imageView.context,
                            R.drawable.ic_document_file
                        )
                    )
                }
            }
        }
    }


    @JvmStatic
    @BindingAdapter(value = ["bindingMediaTypeText"], requireAll = true)
    fun bindingMediaTypeText(textView: TextView, url: String?) {
        url?.let {
            when {
                url.endsWith(".pdf") -> {
                    textView.text = "Pdf"
                }

                FileExtension.isImageFile(url) -> {
                    textView.text = "Image"
                }

                FileExtension.isVideoFile(url) -> {
                    textView.text = "Video"
                }

                FileExtension.isAudioFile(url) -> {
                    textView.text = "Audio"
                }

                else -> {
                    textView.text = "Document"
                }
            }
        }
    }


    @JvmStatic
    @BindingAdapter(
        value = ["bindRecyclerViewWithHomeworkTeacherFilesWithPenTool", "bindHomeworkTeacherEvaluatedViewModel", "bindHomeworkTeacherEvaluatedListForQuestionWisePosition"],
        requireAll = true
    )
    fun bindRecyclerViewWithHomeworkTeacherFilesWithPenTool(
        recyclerView: RecyclerView,
        arrayList: ArrayList<String>?,
        viewModel: HomeworkTeacherSubmitViewModel,
        position : Int
    ) {

        arrayList?.let {
            val linearLayoutManager =
                LinearLayoutManager(recyclerView.context, RecyclerView.VERTICAL, false)
            recyclerView.layoutManager = linearLayoutManager

            val callbacks =
                ArrayList<CallBackModel<HomeworkTeacherEvaluateTwoAttachmentFilesModel>>()
            callbacks.add(CallBackModel(R.id.root) { model, position ->
                recyclerView.context.openMediaFile("$MEDIA_HOMEWORK_URL$model")
            })

            callbacks.add(CallBackModel(R.id.iv_pen_tool) { model, position ->

                Glide.with(recyclerView.context)
                    .asBitmap()
                    .load("$MEDIA_HOMEWORK_URL${model.url}")
                    .into(object : CustomTarget<Bitmap>() {
                        override fun onResourceReady(
                            resource: Bitmap,
                            transition: Transition<in Bitmap>?
                        ) {

                            val bitmapList = ArrayList<Bitmap>()
                            bitmapList.add(resource)

                            viewModel.currentUploadFilesList = viewModel.attachmentListForQuestionWise[position]

                            val bundle = Bundle()
                            bundle.putParcelableArrayList("bitmaps", bitmapList)
                            recyclerView.findNavController().navigate(R.id.nav_new_oes_tool, bundle)
                        }

                        override fun onLoadCleared(placeholder: Drawable?) {
                        }
                    })


            })

            val adapter =
                RecyclerDynamicAdapter<AdapterHomeworkEvaluateTwoQuestionAttachmentsFilesBinding, HomeworkTeacherEvaluateTwoAttachmentFilesModel>(
                    R.layout.adapter_homework_evaluate_two_question_attachments_files,
                    callbacks
                )
            recyclerView.adapter = adapter
            recyclerView.setHasFixedSize(true)

            val tempArray = ArrayList<HomeworkTeacherEvaluateTwoAttachmentFilesModel>()

            // TODO isPenToolEnabled modification
            it.forEach { model ->
                tempArray.add(HomeworkTeacherEvaluateTwoAttachmentFilesModel(model, true))
            }
            adapter.submitList(tempArray)

        }

    }

    @JvmStatic
    @BindingAdapter(
        value = ["bindHomeworkTeacherEvaluateTwoEvaluateAttachmentsFile"],
        requireAll = true
    )
    fun bindHomeworkTeacherEvaluateTwoEvaluateAttachmentsFile(
        recyclerView: RecyclerView,
        homeworkTeacherEvaluateTwoModel: HomeworkTeacherEvaluateTwoModel
    ) {

        val callbacks =
            ArrayList<CallBackModel<HomeworkTeacherEvaluateOneQuestionEvaluateAttachmentsModel>>()

        callbacks.add(CallBackModel(R.id.iv_delete_file) { model, position ->

            homeworkTeacherEvaluateTwoModel.viewModel.attachmentListForQuestionWise[homeworkTeacherEvaluateTwoModel.position].value?.removeAt(position)
            homeworkTeacherEvaluateTwoModel.viewModel.attachmentListForQuestionWise[homeworkTeacherEvaluateTwoModel.position].value =
                homeworkTeacherEvaluateTwoModel.viewModel.attachmentListForQuestionWise[homeworkTeacherEvaluateTwoModel.position].value

            homeworkTeacherEvaluateTwoModel.viewModel.postDeleteFile(model.url.toString())
        })

        val linearLayoutManager =
            LinearLayoutManager(recyclerView.context, RecyclerView.VERTICAL, false)
        recyclerView.layoutManager = linearLayoutManager

        val twoQuestionAttachmentsAdapter =
            RecyclerDynamicAdapter<AdapterTeacherHomeworkEvaluateTwoEvaluateAttachmentBinding, HomeworkTeacherEvaluateOneQuestionEvaluateAttachmentsModel>(
                R.layout.adapter_teacher_homework_evaluate_two_evaluate_attachment,
                callbacks
            )
        recyclerView.adapter = twoQuestionAttachmentsAdapter
        recyclerView.setHasFixedSize(true)

        homeworkTeacherEvaluateTwoModel.viewModel.attachmentListForQuestionWise[homeworkTeacherEvaluateTwoModel.position].observe(homeworkTeacherEvaluateTwoModel.viewLifecycleOwner, {
            twoQuestionAttachmentsAdapter.replaceList(it)
        })

        if(homeworkTeacherEvaluateTwoModel.viewModel.attachmentListForQuestionWise[homeworkTeacherEvaluateTwoModel.position].value?.size==0){
            val tempArray = ArrayList<HomeworkTeacherEvaluateOneQuestionEvaluateAttachmentsModel>()
            homeworkTeacherEvaluateTwoModel.evaluatedFiles?.forEach {
                tempArray.add(HomeworkTeacherEvaluateOneQuestionEvaluateAttachmentsModel(it))
            }
            homeworkTeacherEvaluateTwoModel.viewModel.attachmentListForQuestionWise[homeworkTeacherEvaluateTwoModel.position].value = tempArray
        }

    }


    @JvmStatic
    @BindingAdapter(
        value = ["bindRecyclerWithHomeworkTeacherCreateHomeworkQuestionModel"],
        requireAll = true
    )
    fun bindRecyclerWithHomeworkTeacherCreateHomeworkQuestionModel(
        recyclerView: RecyclerView,
        homeworkModel: HomeworkTeacherCreateHomeworkQuestionModel
    ) {

        var adapterFiles: RecyclerDynamicAdapter<AdapterTeacherHomeworkCreateQuestionFileBinding, String>? =
            null
        val callbacks = ArrayList<CallBackModel<String>>()
        callbacks.add(CallBackModel(R.id.iv_delete_file) { url, position ->
            adapterFiles?.removeItemAt(position)
            url.let { homeworkModel.viewModel.postDeleteFile(url) }
        })

        adapterFiles =
            RecyclerDynamicAdapter(
                R.layout.adapter_teacher_homework_create_question_file,
                callbacks
            )
        val layoutManager = LinearLayoutManager(recyclerView.context, RecyclerView.VERTICAL, false)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapterFiles

        homeworkModel.questionAttachments.observe(homeworkModel.lifecycleOwner, {
            adapterFiles.replaceList(it)
        })

    }


    @JvmStatic
    @BindingAdapter(
        value = ["bindTextViewByString"],
        requireAll = true
    )
    fun bindTextViewByString(
        textView: TextView,
        text: String
    ) {
        textView.text = text
    }


    @JvmStatic
    @BindingAdapter(
        value = ["bindAddHomeworkIncreaseButton"],
        requireAll = true
    )
    fun bindAddHomeworkIncreaseButton(
        imageView: Button,
        model: HomeworkTeacherCreateHomeworkQuestionModel
    ) {
        imageView.setOnClickListener {
            if(model.maximumNumberOfFiles.get() < 10)
                model.maximumNumberOfFiles.set(model.maximumNumberOfFiles.get() + 1)
        }
    }

    @JvmStatic
    @BindingAdapter(
        value = ["bindAddHomeworkDecreaseButton"],
        requireAll = true
    )
    fun bindAddHomeworkDecreaseButton(
        imageView: Button,
        model: HomeworkTeacherCreateHomeworkQuestionModel
    ) {
        imageView.setOnClickListener {
            if(model.maximumNumberOfFiles.get() > 0)
                model.maximumNumberOfFiles.set(model.maximumNumberOfFiles.get() - 1)
        }
    }


}