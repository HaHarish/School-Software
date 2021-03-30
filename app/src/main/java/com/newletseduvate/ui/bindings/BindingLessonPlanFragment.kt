package com.newletseduvate.ui.bindings

import android.net.Uri
import android.util.Log
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.newletseduvate.R
import com.newletseduvate.adapter.dynamic_recycler_adapter.CallBackModel
import com.newletseduvate.adapter.dynamic_recycler_adapter.RecyclerDynamicAdapter
import com.newletseduvate.databinding.AdapterTeacherLessonPlanViewMoreMediaBinding
import com.newletseduvate.model.lesson_plan.teacher.TeacherLessonPlanViewMoreModel
import com.newletseduvate.model.lesson_plan.teacher.TeacherLessonPlanViewMoreResponse
import com.newletseduvate.utils.extensions.FileExtension
import com.newletseduvate.utils.extensions.openMediaFile
import okhttp3.HttpUrl
import java.net.URLEncoder
import java.nio.charset.StandardCharsets


object BindingLessonPlanFragment {

    @JvmStatic
    @BindingAdapter(
        value = ["bindRecyclerViewWithTeacherLessonPlan", "bindTeacherLessonPlanBranchName"],
        requireAll = true
    )
    fun bindRecyclerViewWithTeacherLessonPlan(
        recyclerView: RecyclerView,
        resultModels: ArrayList<TeacherLessonPlanViewMoreResponse.Result>?,
        branchName: String?
        /*academicYear: String?,
        volumeName: String?,
        gradeName: String?,
        subjectName: String?,
        chapterName: String?,
        periodName: String?,
        documentType: String?*/
    ) {

        resultModels?.let {
            val linearLayoutManager =
                LinearLayoutManager(recyclerView.context, RecyclerView.VERTICAL, false)
            recyclerView.layoutManager = linearLayoutManager

            val callbacks = ArrayList<CallBackModel<TeacherLessonPlanViewMoreModel>>()
            callbacks.add(CallBackModel(R.id.root) { url, position ->

                recyclerView.context.openMediaFile(
                    url.mediaUrl.toString()
                )
            })

            val tempyList = ArrayList<TeacherLessonPlanViewMoreModel>()

            it.forEach {
                val getUrl = "https://erp-revamp.s3.ap-south-1.amazonaws.com"
                // "https://erp-revamp.s3.ap-south-1.amazonaws.com"

                val getPath = "/dev/lesson_plan_file/" + branchName + "/" +
                        it.documentType + "/" +
                        it.mediaFile?.get(0).toString()

                /*var finalUrl: HttpUrl = HttpUrl.Builder()
                    .scheme("https")
                    .host(getUrl)
                    .addPathSegment(getPath)
                    .build()*/

                var finalUrl = Uri.encode("${getUrl}${getPath}")

            //    val finalUrl = URLEncoder.encode(getUrl, StandardCharsets.UTF_8.toString())

                Log.d("TAGY", "LastURL: " + finalUrl.toString())

                tempyList.add(
                    TeacherLessonPlanViewMoreModel(
                        it.documentType,
                        finalUrl.toString()
                    )
                )
            }

            val adapter =
                RecyclerDynamicAdapter<AdapterTeacherLessonPlanViewMoreMediaBinding, TeacherLessonPlanViewMoreModel>(
                    R.layout.adapter_teacher_lesson_plan_view_more_media,
                    callbacks
                )
            recyclerView.adapter = adapter
            recyclerView.setHasFixedSize(true)

            adapter.submitList(tempyList)
        }

    }


    @JvmStatic
    @BindingAdapter(
        value = ["bindIconForFileTypeCircular"],
        requireAll = true
    )
    fun bindIconForFileTypeCircular(
        imageView: ImageView, url: String?
    ) {
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
                        .load(url)
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

}