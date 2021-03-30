package com.newletseduvate.utils.mediaview

import android.annotation.SuppressLint
import android.app.ActivityManager
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.bumptech.glide.Glide
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.upstream.*
import com.google.android.exoplayer2.util.Util
import com.newletseduvate.R
import com.newletseduvate.databinding.FragmentVideoImageViewerBinding
import com.newletseduvate.utils.extensions.openInChrome

/**
bundle.putString("url", url)
bundle.putString("type", VideoAndImageViewer.VIDEO_TAG)
or
bundle.putString("type", VideoAndImageViewer.IMAGE_TAG)
 */

class VideoAndImageViewer : DialogFragment() {

    private var player: ExoPlayer? = null
    private var defaultBandwidthMeter: DefaultBandwidthMeter? = null
    private lateinit var binding: FragmentVideoImageViewerBinding

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentVideoImageViewerBinding.inflate(LayoutInflater.from(context))
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
        binding.ivCloseView.setOnClickListener {
            dismiss()
        }

        binding.btnDownload.setOnClickListener {
            requireContext().openInChrome(requireArguments().getString("url",""))
        }
    }

    private fun initUI() {

        when (requireArguments().getString("type")) {
            VIDEO_TAG -> {
                loadVideoContent()
                binding.playerView.visibility = View.VISIBLE
            }
            IMAGE_TAG -> {
                hideLoading()
                loadImageContent()
                binding.ivImage.visibility = View.VISIBLE
                binding.btnDownload.visibility = View.VISIBLE
            }
        }
    }


    private fun loadImageContent() {
        Glide.with(requireView())
                .load(requireArguments().getString("url"))
                .placeholder(R.drawable.image_shape_empty)
                .fitCenter()
                .into(binding.ivImage)

    }

    private fun loadVideoContent() {
        try {
            // 1. Create a default TrackSelector
            val bandwidthMeter = DefaultBandwidthMeter()
            val videoTrackSelectionFactory = AdaptiveTrackSelection.Factory(bandwidthMeter)
            val trackSelector = DefaultTrackSelector(videoTrackSelectionFactory)

            // 2. Create the player
            player = ExoPlayerFactory.newSimpleInstance(requireContext(), trackSelector)
            //Set media controller
            binding.playerView.requestFocus()

            // Bind the player to the view.
            val mp4VideoUri = Uri.parse(requireArguments().getString("url"))
            //Measures bandwidth during playback. Can be null if not required.
            defaultBandwidthMeter = DefaultBandwidthMeter()

            val videoSource: MediaSource
            val dataSpec = DataSpec(mp4VideoUri)
            val fileDataSource = FileDataSource()
            try {
                fileDataSource.open(dataSpec)
            } catch (e: FileDataSource.FileDataSourceException) {
                e.printStackTrace()
            }

            DataSource.Factory { fileDataSource }
            val dataSourceFactory = DefaultDataSourceFactory(
                    requireContext(), Util.getUserAgent(
                    requireContext(), getString(
                    R.string.app_name
            )
            ), defaultBandwidthMeter
            )
            videoSource = ExtractorMediaSource.Factory(dataSourceFactory)
                    .createMediaSource(fileDataSource.uri!!)

            // Prepare the player with the source.
            player!!.prepare(videoSource)
            binding.playerView.player = player
            player!!.playWhenReady = true //run file/link when ready to play.
            hideLoading()
        } catch (e: Exception) {
            hideLoading()
            e.printStackTrace()
        }
    }

    override fun onStart() {
        super.onStart()

        val dialog = dialog
        if (dialog != null) {
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.MATCH_PARENT
            dialog.window?.also {
                it.setLayout(width, height)
                it.setBackgroundDrawable(ColorDrawable(Color.WHITE))
                it.setWindowAnimations(R.style.DialogAnimation)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        try {
            if (player != null) player!!.release()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onPause() {
        super.onPause()
        if (isAppIsInBackground(requireContext())) {
            player!!.playWhenReady = false
        }
    }

    @SuppressLint("ObsoleteSdkInt")
    private fun isAppIsInBackground(context: Context): Boolean {
        var isInBackground = true
        val am = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
            val runningProcesses = am.runningAppProcesses
            for (processInfo in runningProcesses) {
                if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    for (activeProcess in processInfo.pkgList) {
                        if (activeProcess == context.packageName) {
                            isInBackground = false
                        }
                    }
                }
            }
        } else {
            val taskInfo = am.runningAppProcesses
            val componentInfo = taskInfo[0].importanceReasonComponent
            if (componentInfo.packageName == context.packageName) {
                isInBackground = false
            }
        }
        return isInBackground
    }

    private fun hideLoading() {
        binding.progressBar.visibility = View.GONE
    }

    companion object {
        const val VIDEO_TAG = "VIDEO"
        const val IMAGE_TAG = "IMAGE"

    }
}
