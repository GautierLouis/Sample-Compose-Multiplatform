package com.louisgautier.gallery

import kotlinx.cinterop.BooleanVarOf
import kotlinx.cinterop.CPointer
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.toKString
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext
import platform.CoreFoundation.CFStringCreateWithCString
import platform.CoreFoundation.CFStringGetCStringPtr
import platform.CoreFoundation.CFStringRef
import platform.CoreFoundation.kCFStringEncodingUTF8
import platform.CoreServices.UTTypeCopyPreferredTagWithClass
import platform.CoreServices.UTTypeCreatePreferredIdentifierForTag
import platform.CoreServices.kUTTagClassFilenameExtension
import platform.CoreServices.kUTTagClassMIMEType
import platform.Foundation.NSString
import platform.Foundation.pathExtension
import platform.Foundation.timeIntervalSince1970
import platform.Photos.PHAsset
import platform.Photos.PHAssetMediaTypeImage
import platform.Photos.PHAssetMediaTypeVideo
import platform.Photos.PHAssetResource
import platform.Photos.PHAssetResource.Companion.assetResourcesForAsset
import platform.Photos.PHFetchResult


actual class LoadLocalPictures {

    @OptIn(ExperimentalForeignApi::class)
    actual suspend fun loadPictures(): List<MediaItem> = withContext(Dispatchers.IO) {

        val imagesFetch: PHFetchResult =
            PHAsset.fetchAssetsWithMediaType(PHAssetMediaTypeImage, null)
        val videosFetch: PHFetchResult =
            PHAsset.fetchAssetsWithMediaType(PHAssetMediaTypeVideo, null)

        val results = buildMediaItem(imagesFetch) + buildMediaItem(videosFetch)

        results.sortedByDescending { it.dateAddedSeconds ?: 0L }
    }

    @OptIn(ExperimentalForeignApi::class)
    private fun buildMediaItem(fetch: PHFetchResult): List<MediaItem> {
        val results = mutableListOf<MediaItem>()

        fetch.enumerateObjectsUsingBlock { assetAny: Any?, idx: ULong, stop: CPointer<BooleanVarOf<Boolean>>? ->
            val asset: PHAsset? = assetAny as? PHAsset
            if (asset != null) {
                val id = asset.localIdentifier
                val uri = "ph://$id"
                val durationMs = getDuration(asset.duration)
                var displayName: String? = null
                var mime: String? = null

                try {
                    val resources = assetResourcesForAsset(asset)
                    if (resources.count() > 0) {
                        val r = resources[0] as? PHAssetResource
                        displayName = r?.originalFilename ?: ""
                        mime = getMimeType(r)
                    }
                } catch (_: Throwable) {
                }

                val dateAdded = asset.creationDate?.timeIntervalSince1970?.toLong()
                results += MediaItem(
                    id = id,
                    uri = uri,
                    displayName = displayName,
                    mimeType = mime,
                    dateAddedSeconds = dateAdded,
                    sizeBytes = null,
                    durationMillis = durationMs
                )
            }
        }
        return results

    }

    @Suppress("CAST_NEVER_SUCCEEDS") //<- it will
    @OptIn(ExperimentalForeignApi::class)
    private fun getMimeType(resource: PHAssetResource?): String? {
        val ext = (resource?.originalFilename as? NSString)?.pathExtension?.lowercase()
        val extCf: CFStringRef? = CFStringCreateWithCString(null, ext, kCFStringEncodingUTF8)
        val uti = UTTypeCreatePreferredIdentifierForTag(
            kUTTagClassFilenameExtension,
            extCf,
            null
        )

        val cf = UTTypeCopyPreferredTagWithClass(uti, kUTTagClassMIMEType)
        return CFStringGetCStringPtr(cf, kCFStringEncodingUTF8)?.toKString()
    }

    // duration <= 0 is true for pictures -> set to null to avoid confusion
    private fun getDuration(duration: Double): Long? {
        return if (duration <= 0) null
        else (duration * 1_000).toLong()
    }

}