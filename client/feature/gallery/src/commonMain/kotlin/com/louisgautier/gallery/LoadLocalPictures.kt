package com.louisgautier.gallery

expect class LoadLocalPictures {
    suspend fun loadPictures(): List<MediaItem>
}

data class MediaItem(
    val id: String,
    val uri: String,
    val displayName: String?,
    val mimeType: String?,
    val dateAddedSeconds: Long?,
    val sizeBytes: Long?,
    val durationMillis: Long? = null
)