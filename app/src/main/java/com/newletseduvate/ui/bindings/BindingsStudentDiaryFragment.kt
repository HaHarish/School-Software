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
import com.newletseduvate.databinding.AdapterDiaryMediaFilesBinding
import com.newletseduvate.utils.extensions.FileExtension
import com.newletseduvate.utils.extensions.openMediaFile

object BindingsStudentDiaryFragment {

    @JvmStatic
    @BindingAdapter(value = ["bindRecyclerViewWithStudentDiaryMediaFiles"], requireAll = true)
    fun bindRecyclerViewWithStudentDiaryMediaFiles(recyclerView: RecyclerView, arrayList: ArrayList<String>?) {

        arrayList?.let {
            val linearLayoutManager =
                    LinearLayoutManager(recyclerView.context, RecyclerView.VERTICAL, false)
            recyclerView.layoutManager = linearLayoutManager

            val callbacks = java.util.ArrayList<CallBackModel<String>>()
            callbacks.add(CallBackModel(R.id.root) { url, position ->
                recyclerView.context.openMediaFile(url)
            })

            val tempArray = ArrayList<String>()
            it.forEach { url ->
                tempArray.add("https://erp-revamp.s3.ap-south-1.amazonaws.com/$url")
            }

            val adapter =
                    RecyclerDynamicAdapter<AdapterDiaryMediaFilesBinding, String>(
                            R.layout.adapter_diary_media_files,
                            callbacks
                    )
            recyclerView.adapter = adapter
            recyclerView.setHasFixedSize(true)

            adapter.submitList(tempArray)
        }

    }


    @JvmStatic
    @BindingAdapter(value = ["bindIconForFileTypeDiary"], requireAll = true)
    fun bindIconForFileTypeDiary(imageView: ImageView, url: String?) {
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