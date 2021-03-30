package com.newletseduvate.ui.bindings

import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.newletseduvate.BuildConfig
import com.newletseduvate.R
import com.newletseduvate.adapter.dynamic_recycler_adapter.CallBackModel
import com.newletseduvate.adapter.dynamic_recycler_adapter.RecyclerDynamicAdapter
import com.newletseduvate.databinding.AdapterHomeworkDetailsQuestionFilesBinding
import com.newletseduvate.databinding.AdapterOnlineClassAttendResourceFileBinding
import com.newletseduvate.model.online_class.TeacherViewClassDetailsModel
import com.newletseduvate.ui.online_class.OnlineClassAttendResourceFragment.Companion.textNotAvailableConstant
import com.newletseduvate.utils.extensions.FileExtension
import com.newletseduvate.utils.extensions.openMediaFile
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

object BindingOnlineClassAttendFragment {

    @JvmStatic
    @BindingAdapter(value = ["bindClassAcceptButtonVisibilityByAccepted"], requireAll = true)
    fun bindClassAcceptButtonVisibility(button: Button, isAccepted: Boolean) {

        if(isAccepted)
            button.visibility = View.VISIBLE
        else
            button.visibility = View.GONE
    }

    @JvmStatic
    @BindingAdapter(value = ["bindRecyclerViewWithResourceFile"], requireAll = true)
    fun bindRecyclerViewWithResourceFile(recyclerView: RecyclerView, arrayList: ArrayList<String>?) {

        arrayList?.let {
            val linearLayoutManager = LinearLayoutManager(recyclerView.context, RecyclerView.VERTICAL, false)
            recyclerView.layoutManager = linearLayoutManager

            val callbacks = ArrayList<CallBackModel<String>>()
            callbacks.add(CallBackModel(R.id.tv_view_file) { model, position ->
                if(model != textNotAvailableConstant){
                    recyclerView.context.openMediaFile("https://erp-revamp.s3.ap-south-1.amazonaws.com/$model")
                }
            })

            val adapter =
                RecyclerDynamicAdapter<AdapterOnlineClassAttendResourceFileBinding, String>(R.layout.adapter_online_class_attend_resource_file, callbacks)
            recyclerView.adapter = adapter
            recyclerView.setHasFixedSize(true)

            adapter.submitList(arrayList)
        }

    }

    @JvmStatic
    @BindingAdapter(value = ["bindButtonStyleForResourceFile"], requireAll = true)
    fun bindButtonStyleForResourceFile(button: Button, string: String) {

        if(string == textNotAvailableConstant){
            button.setTextAppearance(R.style.Common_TextButton)
            button.text = textNotAvailableConstant
        }else {
            button.setTextAppearance(R.style.Common_OutlinedMaterialButton)
            button.text = "View file"
        }

    }


    @JvmStatic
    @BindingAdapter(value = ["bindOnlineClassTeacherClassOverVisibility"], requireAll = true)
    fun bindOnlineClassTeacherClassOverVisibility(button: TextView, model: TeacherViewClassDetailsModel) {

        try {
            if (model.currentServerTime.isNotEmpty() && !model.date.isNullOrBlank()) {

                val timeFormat = SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.getDefault())


                val classEndTime = timeFormat.parse("${model.date} ${model.endTime}")
                val currServerTime = timeFormat.parse(model.currentServerTime)

                if(currServerTime.before(classEndTime))
                    button.visibility = View.GONE
                else
                    button.visibility = View.VISIBLE
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    @JvmStatic
    @BindingAdapter(value = ["bindOnlineClassTeacherAcceptDeclineVisibility"], requireAll = true)
    fun bindOnlineClassTeacherAcceptDeclineVisibility(button: Button, model: TeacherViewClassDetailsModel) {

        try {
            if (model.currentServerTime.isNotEmpty() && !model.date.isNullOrBlank()) {

                val timeFormat = SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.getDefault())

                val classEndTime = timeFormat.parse("${model.date} ${model.endTime}")
                val currServerTime = timeFormat.parse(model.currentServerTime)

                if(currServerTime.before(classEndTime))
                    button.visibility = View.VISIBLE
                else
                    button.visibility = View.GONE
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    @JvmStatic
    @BindingAdapter(value = ["bindOnlineClassResourceIconForFileType"], requireAll = true)
    fun bindOnlineClassResourceIconForFileType(imageView: ImageView, url: String?) {
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

                    Glide.with(imageView.context)
                        .load("${BuildConfig.MEDIA_S3_URL}$url")
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