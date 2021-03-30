package com.newletseduvate.utils.mediaview

import android.media.MediaPlayer


object UniqueMediaPlayer {
    @get:Synchronized
    var mediaPlayer: MediaPlayer? = null
        get() {
            if (field == null) {
                field = MediaPlayer()
            }
            return field
        }
        private set
}