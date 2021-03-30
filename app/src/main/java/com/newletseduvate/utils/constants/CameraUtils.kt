package com.newletseduvate.utils.constants

import android.annotation.SuppressLint
import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.text.TextUtils
import android.text.format.Time
import android.util.Log
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.loader.content.CursorLoader
import com.newletseduvate.R
import java.io.*
import java.text.SimpleDateFormat
import java.util.*

object CameraUtils {

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    private fun isExternalStorageDocument(uri: Uri): Boolean {
        return "com.android.externalstorage.documents" == uri.authority
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    private fun isDownloadsDocument(uri: Uri): Boolean {
        return "com.android.providers.downloads.documents" == uri.authority
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    private fun isMediaDocument(uri: Uri): Boolean {
        return "com.android.providers.media.documents" == uri.authority
    }

    fun generateTimeStampPhotoFileUri(context: Context): Uri? {
        var photoFileUri: Uri? = null
        val outputDir = getPhotoDirectory(context)
        if (outputDir != null) {
            val t = Time()
            t.setToNow()
            val photoFile = File(
                    outputDir, System.currentTimeMillis()
                    .toString() + ".png"
            )
            photoFileUri = Uri.fromFile(photoFile)
        }
        return photoFileUri
    }

    private fun getPhotoDirectory(context: Context): File? {
        var outputDir: File? = null
        val externalStorageStagte = Environment.getExternalStorageState()
        if (externalStorageStagte == Environment.MEDIA_MOUNTED) {
            val photoDir = Environment
                    .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
            outputDir = File(photoDir, context.resources.getString(R.string.app_name))
            if (!outputDir.exists()) if (!outputDir.mkdirs()) {
                Toast.makeText(
                        context, "Failed to create directory "
                        + outputDir.absolutePath,
                        Toast.LENGTH_SHORT
                ).show()
                outputDir = null
            }
        }
        return outputDir
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    private fun isGooglePhotosUri(uri: Uri): Boolean {
        return "com.google.android.apps.photos.content" == uri.authority || "com.google.android.apps.photos.contentprovider" == uri.authority
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Drive.
     */
    private fun isGoogleDriveUri(uri: Uri): Boolean {
        return "com.google.android.apps.docs.storage" == uri.authority || "com.google.android.apps.docs.storage.legacy" == uri.authority
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    private fun getDataColumn(
            context: Context, uri: Uri?, selection: String?,
            selectionArgs: Array<String>?
    ): String? {
        var cursor: Cursor? = null
        val column = "_data"
        val projection = arrayOf(
                column
        )
        try {
            cursor = context.contentResolver.query(
                    uri!!, projection, selection, selectionArgs,
                    null
            )
            if (cursor != null && cursor.moveToFirst()) {
                val column_index = cursor.getColumnIndexOrThrow(column)
                return cursor.getString(column_index)
            }
        } finally {
            cursor?.close()
        }
        return null
    }

    /**
     * Get content:// from segment list
     * In the new Uri Authority of Google Photos, the last segment is not the content:// anymore
     * So let's iterate through all segments and find the content uri!
     *
     * @param segments The list of segment
     */
    private fun getContentFromSegments(segments: List<String>): String {
        var contentPath = ""
        for (item in segments) {
            if (item.startsWith("content://")) {
                contentPath = item
                break
            }
        }
        return contentPath
    }

    /**
     * Check if a file exists on device
     *
     * @param filePath The absolute file path
     */
    private fun fileExists(filePath: String): Boolean {
        val file = File(filePath)
        return file.exists()
    }

    /**
     * Get full file path from external storage
     *
     * @param pathData The storage type and the relative path
     */
    private fun getPathFromExtSD(pathData: Array<String>): String {
        val type = pathData[0]
        val relativePath = "/" + pathData[1]
        var fullPath = ""
        if ("primary".equals(type, ignoreCase = true)) {
            fullPath =
                    Environment.getExternalStorageDirectory().toString() + relativePath
            if (fileExists(fullPath)) {
                return fullPath
            }
        }

        // Environment.isExternalStorageRemovable() is `true` for external and internal storage
        // so we cannot relay on it.
        //
        // instead, for each possible path, check if file exists
        // we'll start with secondary storage as this could be our (physically) removable sd card
        fullPath = System.getenv("SECONDARY_STORAGE") + relativePath
        if (fileExists(fullPath)) {
            return fullPath
        }
        fullPath = System.getenv("EXTERNAL_STORAGE") + relativePath
        return if (fileExists(fullPath)) {
            fullPath
        } else fullPath
    }

    fun getPath(context: Context, uri: Uri): String? {
        if (DocumentsContract.isDocumentUri(context, uri)) {
            when {
                isExternalStorageDocument(uri) -> {
                    val docId = DocumentsContract.getDocumentId(uri)
                    val split = docId.split(":").toTypedArray()
                    val type = split[0]
                    val fullPath = getPathFromExtSD(split)
                    return if (fullPath !== "") {
                        fullPath
                    } else {
                        null
                    }
                }
                isDownloadsDocument(uri) -> {
                    var cursor: Cursor? = null
                    try {
                        cursor = context.contentResolver.query(
                            uri,
                            arrayOf(MediaStore.MediaColumns.DISPLAY_NAME),
                            null,
                            null,
                            null
                        )
                        if (cursor != null && cursor.moveToFirst()) {
                            val fileName = cursor.getString(0)
                            val path =
                                Environment.getExternalStorageDirectory()
                                    .toString() + "/Download/" + fileName
                            if (!TextUtils.isEmpty(path)) {
                                return path
                            }
                        }
                    } finally {
                        cursor?.close()
                    }
                    val id = DocumentsContract.getDocumentId(uri)
                    return try {
                        val contentUri = ContentUris.withAppendedId(
                            Uri.parse("content://downloads/public_downloads"),
                            java.lang.Long.valueOf(id)
                        )
                        getDataColumn(context, contentUri, null, null)
                    } catch (e: NumberFormatException) {
                        uri.path!!.replaceFirst("^/document/raw:".toRegex(), "")
                            .replaceFirst("^raw:".toRegex(), "")
                    }
                }
                isMediaDocument(uri) -> {
                    val docId = DocumentsContract.getDocumentId(uri)
                    val split = docId.split(":").toTypedArray()
                    val type = split[0]
                    var contentUri: Uri? = null
                    if ("image" == type) {
                        contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                    } else if ("video" == type) {
                        contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                    } else if ("audio" == type) {
                        contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                    }
                    val selection = "_id=?"
                    val selectionArgs = arrayOf(
                        split[1]
                    )
                    return getDataColumn(context, contentUri, selection, selectionArgs)
                }
                isGoogleDriveUri(uri) -> {
                    return getDriveFilePath(uri, context)
                }
            }
        } else if ("content".equals(uri.scheme, ignoreCase = true)) {

            // Return the remote address
            if (isGooglePhotosUri(uri)) {
                val contentPath = getContentFromSegments(uri.pathSegments)
                return if (contentPath !== "") {
                    getPath(context, Uri.parse(contentPath))
                } else {
                    null
                }
            }
            return if (isGoogleDriveUri(uri)) {
                getDriveFilePath(uri, context)
            } else getDataColumn(context, uri, null, null)
        } else if ("file".equals(uri.scheme, ignoreCase = true)) {
            return uri.path
        }
        return null
    }

    @SuppressLint("NewApi")
    fun getRealPathFromURI_API11to18(
            context: Context?,
            contentUri: Uri?
    ): String? {
        val proj = arrayOf(MediaStore.Images.Media.DATA)
        var result: String? = null
        val cursorLoader =
                CursorLoader(context!!, contentUri!!, proj, null, null, null)
        val cursor = cursorLoader.loadInBackground()
        if (cursor != null) {
            val column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            cursor.moveToFirst()
            result = cursor.getString(column_index)
            cursor.close()
        }
        return result
    }

    private fun getDriveFilePath(
            uri: Uri,
            context: Context
    ): String? {
        val returnCursor =
                context.contentResolver.query(uri, null, null, null, null)
        /*
         * Get the column indexes of the data in the Cursor,
         *     * move to the first row in the Cursor, get the data,
         *     * and display it.
         * */
        val nameIndex = returnCursor!!.getColumnIndex(OpenableColumns.DISPLAY_NAME)
        val sizeIndex = returnCursor.getColumnIndex(OpenableColumns.SIZE)
        returnCursor.moveToFirst()
        val name = returnCursor.getString(nameIndex)
        val size = java.lang.Long.toString(returnCursor.getLong(sizeIndex))
        val file = File(context.cacheDir, name)
        try {
            val inputStream =
                    context.contentResolver.openInputStream(uri)
            val outputStream = FileOutputStream(file)
            var read = 0
            val maxBufferSize = 1 * 1024 * 1024
            val bytesAvailable = inputStream!!.available()

            //int bufferSize = 1024;
            val bufferSize = Math.min(bytesAvailable, maxBufferSize)
            val buffers = ByteArray(bufferSize)
            while (inputStream.read(buffers).also { read = it } != -1) {
                outputStream.write(buffers, 0, read)
            }
            Log.e("File Size", "Size " + file.length())
            inputStream.close()
            outputStream.close()
            Log.e("File Path", "Path " + file.path)
            Log.e("File Size", "Size " + file.length())
        } catch (e: java.lang.Exception) {
            returnCursor.close()
            Log.e("Exception", e.message!!)
        }
        return file.path
    }

    fun getAudioPath(
            context: Context?,
            uri: Uri?
    ): String? {
        val data = arrayOf(MediaStore.Audio.Media.DATA)
        val loader =
                CursorLoader(context!!, uri!!, data, null, null, null)
        val cursor = loader.loadInBackground()
        val column_index = cursor!!.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)
        cursor.moveToFirst()
        return cursor.getString(column_index)
    }

    fun getFileDataFromPath(
            context: Context?,
            path: String?
    ): ByteArray? {
        val baos = ByteArrayOutputStream()
        val fis: FileInputStream
        try {
            fis = FileInputStream(File(path))
            val buf = ByteArray(1024)
            var n: Int
            while (-1 != fis.read(buf).also { n = it }) baos.write(buf, 0, n)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return baos.toByteArray()
    }

    fun getFileDataFromUri(
            context: Context?,
            uri: Uri
    ): ByteArray? {
        val baos = ByteArrayOutputStream()
        val fis: FileInputStream
        try {
            fis = FileInputStream(File(uri.toString()))
            val buf = ByteArray(1024)
            var n: Int
            while (-1 != fis.read(buf).also { n = it }) baos.write(buf, 0, n)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return baos.toByteArray()
    }

    @Throws(IOException::class)
    fun createImageFile(directory: File?): File? {
        val timeStamp =
                SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault())
                        .format(Date())
        val imageFileName = "medLife_$timeStamp.jpg"
        return File(directory, imageFileName)
    }

    fun optimizeBitmap(sampleSize: Int, filePath: String?): Bitmap? {
        // bitmap factory
        val options = BitmapFactory.Options()

        // downsizing image as it throws OutOfMemory Exception for larger
        // images
        options.inSampleSize = sampleSize
        return BitmapFactory.decodeFile(filePath, options)
    }

    fun writeScaledDownImage(
            imageFile: File,
            context: Context?
    ) {
        val sizeInMB = imageFile.length() / (1024f * 1024)
        val options = BitmapFactory.Options()
        //read the scaled down image and write into file
        options.inJustDecodeBounds = false
        if (sizeInMB > 1) {
            Log.e(
                    "needs scaling down",
                    "needs scaling down because the size is $sizeInMB MB"
            )
            options.inSampleSize = 2
        } else {
            Log.e(
                    "no need of scaling down",
                    "no need of scaling down because the size is $sizeInMB MB"
            )
            return
        }
        val resizedBitmap = BitmapFactory.decodeFile(imageFile.path, options)
        val outSteam = ByteArrayOutputStream()
        resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 90, outSteam)
        try {
            val fileOutStram = FileOutputStream(imageFile)
            fileOutStram.write(outSteam.toByteArray())
        } catch (e: IOException) {
            e.printStackTrace()
        }
        Log.e(
                "new size of file",
                "new size of file = " + imageFile.length() / (1024f * 1024) + "MB"
        )
    }


    fun getImageUri(inContext: Context, inImage: Bitmap): Uri? {
        val bytes = ByteArrayOutputStream()
        inImage.compress(Bitmap.CompressFormat.PNG, 100, bytes)
        val path = MediaStore.Images.Media.insertImage(
                inContext.contentResolver,
                inImage,
                "Title",
                null
        )
        return Uri.parse(path)
    }

    /* fun getFileName(context: Context, uri: Uri): String? {
         var result: String? = null
         if (uri.scheme == "content") {
             val cursor =
                 context.contentResolver.query(uri, null, null, null, null)
             try {
                 if (cursor != null && cursor.moveToFirst()) {
                     result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME))
                 }
             } finally {
                 cursor!!.close()
             }
         }
         if (result == null) {
             result = uri.path
             val cut = result!!.lastIndexOf('/')
             if (cut != -1) {
                 result = result.substring(cut + 1)
             }
         }
         return result
     }*/

    @Throws(IOException::class)
    fun createImageFile(context: Context): File? {
        val timeStamp =
                SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault())
                        .format(Date())
        val imageFileName = "IMG_" + timeStamp + "_"
        val storageDir =
                context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(imageFileName, ".jpg", storageDir)
    }

    fun getOutputMediaFileUri(
            context: Context,
            file: File?
    ): Uri? {
        return FileProvider.getUriForFile(context, context.packageName + ".provider", file!!)
    }

    /**
     * Creates and returns the image or video file before opening the camera
     */
    fun getOutputMediaFile(type: Int): File? {

        // External sdcard location
        val mediaStorageDir = File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                "Camera"
        )

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.e(
                        "Camera", "Oops! Failed create "
                        + "Camera" + " directory"
                )
                return null
            }
        }

        // Preparing media file naming convention
        // adds timestamp
        val timeStamp = SimpleDateFormat(
                "yyyyMMdd_HHmmss",
                Locale.getDefault()
        ).format(Date())
        val mediaFile: File
        mediaFile = if (type == 1) {
            File(
                    mediaStorageDir.path + File.separator
                            + "IMG_" + timeStamp + "." + "jpg"
            )
        } else if (type == 2) {
            File(
                    mediaStorageDir.path + File.separator
                            + "VID_" + timeStamp + "." + "mp4"
            )
        } else {
            return null
        }
        return mediaFile
    }
}