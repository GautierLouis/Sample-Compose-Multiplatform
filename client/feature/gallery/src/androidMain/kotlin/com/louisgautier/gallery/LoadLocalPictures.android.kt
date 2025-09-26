package com.louisgautier.gallery

import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.media.MediaScannerConnection
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import com.louisgautier.logger.AppLogger
import com.louisgautier.utils.context.ContextWrapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

actual class LoadLocalPictures(
    private val contextWrapper: ContextWrapper
) {
    actual suspend fun loadPictures(): List<MediaItem> = withContext(Dispatchers.IO) {
        val resolver = contextWrapper.context.contentResolver
        val results = mutableListOf<MediaItem>()

        // helper to read cursor safely
        fun Cursor.getStringOrNull(index: Int) = if (isNull(index)) null else getString(index)
        fun Cursor.getLongOrNull(index: Int) = if (isNull(index)) null else getLong(index)

        rescanCommonMediaDirs(contextWrapper.context)

        //Query photos
        run {
            val projection = arrayOf(
                MediaStore.Images.Media._ID,
                MediaStore.Images.Media.DISPLAY_NAME,
                MediaStore.Images.Media.MIME_TYPE,
                MediaStore.Images.Media.DATE_ADDED,
                MediaStore.Images.Media.SIZE
            )

            val uri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                // Query all the device storage volumes instead of the primary only
                MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL)
            } else {
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            }

            resolver.query(
                uri,
                projection,
                null,
                null,
                "${MediaStore.Images.Media.DATE_ADDED} DESC"
            )?.use { cursor ->
                val idCol = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
                val nameCol = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME)
                val mimeCol = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.MIME_TYPE)
                val dateCol = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_ADDED)
                val sizeCol = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.SIZE)
                while (cursor.moveToNext()) {
                    val id = cursor.getLong(idCol)
                    val contentUri = ContentUris.withAppendedId(uri, id)
                    results += MediaItem(
                        id = id.toString(),
                        uri = contentUri.toString(),
                        displayName = cursor.getStringOrNull(nameCol),
                        mimeType = cursor.getStringOrNull(mimeCol),
                        dateAddedSeconds = cursor.getLongOrNull(dateCol),
                        sizeBytes = cursor.getLongOrNull(sizeCol),
                        durationMillis = null
                    )
                }
            }
        }

        // Query videos
        run {
            val projection = arrayOf(
                MediaStore.Video.Media._ID,
                MediaStore.Video.Media.DISPLAY_NAME,
                MediaStore.Video.Media.MIME_TYPE,
                MediaStore.Video.Media.DATE_ADDED,
                MediaStore.Video.Media.SIZE,
                MediaStore.Video.Media.DURATION
            )
            val uri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                // Query all the device storage volumes instead of the primary only
                MediaStore.Video.Media.getContentUri(MediaStore.VOLUME_EXTERNAL)
            } else {
                MediaStore.Video.Media.EXTERNAL_CONTENT_URI
            }

            resolver.query(
                uri,
                projection,
                null,
                null,
                "${MediaStore.Video.Media.DATE_ADDED} DESC"
            )?.use { cursor ->
                val idCol = cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID)
                val nameCol = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME)
                val mimeCol = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.MIME_TYPE)
                val dateCol = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATE_ADDED)
                val sizeCol = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.SIZE)
                val durationCol = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION)
                while (cursor.moveToNext()) {
                    val id = cursor.getLong(idCol)
                    val contentUri = ContentUris.withAppendedId(uri, id)
                    results += MediaItem(
                        id = id.toString(),
                        uri = contentUri.toString(),
                        displayName = cursor.getStringOrNull(nameCol),
                        mimeType = cursor.getStringOrNull(mimeCol),
                        dateAddedSeconds = cursor.getLongOrNull(dateCol),
                        sizeBytes = cursor.getLongOrNull(sizeCol),
                        durationMillis = cursor.getLongOrNull(durationCol)
                    )
                }
            }
        }

        results.sortedByDescending { it.dateAddedSeconds ?: 0L }
    }

    fun rescanCommonMediaDirs(context: Context) {
        val dirs = listOf(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM),
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
        )

        val filesToScan = mutableListOf<String>()
        for (dir in dirs) {
            if (dir != null && dir.exists()) {
                dir.listFiles()?.forEach { file ->
                    // optionally recurse only one level for Camera subfolder
                    if (file.isFile) filesToScan += file.absolutePath
                    else if (file.isDirectory) {
                        file.listFiles()?.forEach { f2 ->
                            if (f2.isFile) filesToScan += f2.absolutePath
                        }
                    }
                }
            }
        }

        if (filesToScan.isEmpty()) {
            AppLogger.d("rescanCommonMediaDirs: nothing found to scan (folders may be empty or inaccessible)")
            return
        }

        AppLogger.d("rescanCommonMediaDirs: scanning ${filesToScan.size} files")
        MediaScannerConnection.scanFile(context, filesToScan.toTypedArray(), null) { path, uri ->
            AppLogger.d("Photo fetch : scanned: $path -> $uri")
        }
    }

}