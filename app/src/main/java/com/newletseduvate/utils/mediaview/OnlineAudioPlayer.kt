package com.newletseduvate.utils.mediaview

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import androidx.fragment.app.DialogFragment
import com.newletseduvate.R
import com.newletseduvate.databinding.FragmentOnlineAudioPlayerBinding
import java.io.IOException
import java.util.*

/**
 * bundle.putString("url", url)
 *
 */

class OnlineAudioPlayer : DialogFragment(), View.OnClickListener {

    private lateinit var binding: FragmentOnlineAudioPlayerBinding
    private var audioPlayer = UniqueMediaPlayer.mediaPlayer

    private var timerTask: TimerTask? = null
    private var isFragmentDestroyed = false

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentOnlineAudioPlayerBinding.inflate(LayoutInflater.from(context))
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvCurrentTime.text = "0:0"
        binding.tvEndTime.text = "0:0"

        try {
            val url = requireArguments().getString("url")
            audioPlayer?.reset()
            audioPlayer?.setDataSource(url)
            audioPlayer?.setOnPreparedListener { player ->
                if (!isFragmentDestroyed) {
                    player.start()
                    val totalMinutes = (player.duration / 1000) / 60
                    val totalSeconds = ((player.duration / 1000) % 60)

                    binding.tvEndTime.text = "$totalMinutes:$totalSeconds"
                    binding.audioSeekBar.max = player.duration
                }
            }
            audioPlayer?.prepareAsync()
        } catch (ignored: IOException) {
        }

        timerTask = object : TimerTask() {
            override fun run() {

                activity?.runOnUiThread {
                    if (audioPlayer != null) {
                        val currentProgress: Int = audioPlayer!!.currentPosition
                        binding.audioSeekBar.progress = currentProgress
                        val currentMinutes = currentProgress / 1000 / 60
                        val currentSeconds = currentProgress / 1000 % 60
                        binding.tvCurrentTime.text = "$currentMinutes:$currentSeconds"
                    }
                }

            }
        }

        Timer().scheduleAtFixedRate(timerTask, 0, 1000)

        binding.audioSeekBar.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, seek: Boolean) {
                if (seek) {
                    audioPlayer?.seekTo(progress)
                    seekBar.progress = progress
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })

        binding.audioPlay.setOnClickListener(this)
        binding.audioPause.setOnClickListener(this)
    }

    private fun playAudio() {
        audioPlayer?.start()

        binding.audioPlay.visibility = View.GONE
        binding.audioPause.visibility = View.VISIBLE
    }

    private fun pauseAudio() {
        audioPlayer?.pause()
        binding.audioPlay.visibility = View.VISIBLE
        binding.audioPause.visibility = View.GONE
    }

    override fun onStart() {
        super.onStart()

        val dialog = dialog
        if (dialog != null) {
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.WRAP_CONTENT
            dialog.window?.also {
                it.setLayout(width, height)
                it.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                it.setWindowAnimations(R.style.DialogAnimation)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        try {
            audioPlayer?.stop()
            isFragmentDestroyed = true

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onClick(v: View?) {

        when (v?.id) {
            binding.audioPlay.id -> {
                playAudio()
            }

            binding.audioPause.id -> {
                pauseAudio()
            }

        }
    }
}
