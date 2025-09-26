package com.louisgautier.gallery

actual class LoadLocalPictures {
    actual suspend fun loadPictures(): List<MediaItem> {
        return emptyList()
    }
}