package com.newletseduvate.utils.extensions

object FileExtension {

    private val video_extensions = arrayOf(".webm", ".mp4", ".mkv")
    private val file_extensions = arrayOf(".doc", ".docx", ".pdf")
    private val image_extensions = arrayOf(".png", ".jpg", ".PNG", ".jpeg", ".JPG", ".JPEG")
    private val audio_extensions = arrayOf(".waw", ".mp3", ".MP3")
    private val excel_extensions = arrayOf(".xls", "xlsx")

    @JvmStatic
    fun isVideoFile(filename: String): Boolean {
        for (i in video_extensions.indices) {
            if (filename.endsWith(video_extensions[i])) {
                return true
            }
        }
        return false
    }

    @JvmStatic
    fun isFile(filename: String): Boolean {
        for (i in file_extensions.indices) {
            if (filename.endsWith(file_extensions[i])) {
                return true
            }
        }
        return false
    }

    @JvmStatic
    fun isImageFile(filename: String): Boolean {
        for (i in image_extensions.indices) {
            if (filename.endsWith(image_extensions[i])) {
                return true
            }
        }
        return false
    }

    @JvmStatic
    fun isAudioFile(filename: String): Boolean {
        for (i in audio_extensions.indices) {
            if (filename.endsWith(audio_extensions[i])) {
                return true
            }
        }
        return false
    }

    @JvmStatic
    fun isExcelFile(filename: String): Boolean {
        for (i in excel_extensions.indices) {
            if (filename.endsWith(excel_extensions[i])) {
                return true
            }
        }
        return false
    }
}