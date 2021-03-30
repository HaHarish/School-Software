package com.newletseduvate.ui.bindings

import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.newletseduvate.R
import com.newletseduvate.adapter.dynamic_recycler_adapter.CallBackModel
import com.newletseduvate.adapter.dynamic_recycler_adapter.RecyclerDynamicAdapter
import com.newletseduvate.databinding.AdapterHomeworkDetailsQuestionFilesBinding
import com.newletseduvate.utils.extensions.FileExtension
import com.newletseduvate.utils.extensions.openMediaFile

object BindingCircularFragment {

    @JvmStatic
    @BindingAdapter(
        value = ["bindRecyclerViewWithCircularMedia", "bindCircularBranchName", "bindTeacherCircular"],
        requireAll = true
    )
    fun bindRecyclerViewWithCircularMedia(
        recyclerView: RecyclerView,
        arrayList: ArrayList<String>?,
        branchName: String,
        teacherCircular: Boolean
    ) {

        arrayList?.let {
            val linearLayoutManager =
                LinearLayoutManager(recyclerView.context, RecyclerView.VERTICAL, false)
            recyclerView.layoutManager = linearLayoutManager

            val callbacks = ArrayList<CallBackModel<String>>()
            callbacks.add(CallBackModel(R.id.root) { url, position ->

                recyclerView.context.openMediaFile(
                    url
                )
            })

            val tempList = ArrayList<String>()
            it.forEach { url ->

                if(teacherCircular){
                    tempList.add(
                        "https://erp-revamp.s3.ap-south-1.amazonaws.com/" +
                                "dev/circular_files/" + branchName + "/" + url
                    )
                }else{
                    tempList.add(
                        "https://erp-revamp.s3.ap-south-1.amazonaws.com/" +
                                "dev/circular_files/" + url
                    )
                }

            }


            val adapter =
                RecyclerDynamicAdapter<AdapterHomeworkDetailsQuestionFilesBinding, String>(
                    R.layout.adapter_circular_media_files,
                    callbacks
                )
            recyclerView.adapter = adapter
            recyclerView.setHasFixedSize(true)

            adapter.submitList(tempList)
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