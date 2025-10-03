package com.louisgautier.gallery

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import platform.Foundation.NSData
import platform.Foundation.NSError
import platform.Foundation.NSMutableData
import platform.Foundation.NSTemporaryDirectory
import platform.Foundation.NSURL
import platform.Foundation.appendData
import platform.Foundation.writeToFile
import platform.Photos.PHAsset
import platform.Photos.PHAssetResource
import platform.Photos.PHAssetResourceManager
import platform.Photos.PHAssetResourceRequestOptions
import platform.Photos.PHFetchOptions
import platform.Photos.PHFetchResult
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

actual class LoadLocalPictures {
    actual suspend fun loadPictures(): List<MediaItem> = withContext(Dispatchers.Default) {

        // ---- Fetch options: newest first ----
        val fetchOptions = PHFetchOptions().apply {
            // sort by creationDate descending
            sortDescriptors = listOf(
                platform.Foundation.NSSortDescriptor.sortDescriptorWithKey(
                    "creationDate",
                    false
                )
            )
            // You can tweak predicate if you only want images/videos, etc.
        }

        val fetchResult: PHFetchResult = PHAsset.fetchAssetsWithOptions(fetchOptions)
        val total = fetchResult.count.toInt()

        // Safety cap to avoid loading the entire device (adjust as you like)
        val MAX_ITEMS = 200
        val limit = if (total > MAX_ITEMS) MAX_ITEMS else total

        val items = mutableListOf<MediaItem>()

        for (i in 0 until limit) {
            val asset = fetchResult.objectAtIndex(i.toULong()) as? PHAsset ?: continue
            val localId = asset.localIdentifier

            // pick the first available resource for this asset
            val resources = PHAssetResource.assetResourcesForAsset(asset)
            if (resources.count() == 0) continue
            val resource = (resources[0] as? PHAssetResource) ?: continue
            val displayName = resource.originalFilename

            // stream the resource bytes into an NSMutableData accumulator
            val accumulator = NSMutableData()
            val manager = PHAssetResourceManager.defaultManager()
            val reqOptions = PHAssetResourceRequestOptions().apply {
                networkAccessAllowed = true // for iCloud
            }

            // requestDataForAssetResource is async --> suspend until done
            suspendCoroutine<Unit> { cont ->
                try {
                    manager.requestDataForAssetResource(
                        resource,
                        reqOptions,
                        { chunk: NSData? ->
                            if (chunk != null) accumulator.appendData(chunk)
                        },
                        { error: NSError? ->
                            if (error != null) {
                                cont.resumeWithException(
                                    RuntimeException(
                                        error.localizedDescription
                                    )
                                )
                            } else {
                                cont.resume(Unit)
                            }
                        }
                    )
                } catch (t: Throwable) {
                    // defensive: resume with exception if requestDataForAssetResource itself throws
                    cont.resumeWithException(t)
                }
            }

            // write accumulated bytes to a temporary file (off main thread)
            val tmpDir = NSTemporaryDirectory()
            val ext = displayName.let {
                val dot = it.lastIndexOf('.')
                if (dot >= 0) it.substring(dot + 1) else "jpg"
            }
            val safeName = "asset_${localId.hashCode()}.$ext"
            val path = tmpDir + safeName
            val wrote = accumulator.writeToFile(path, true)

            if (!wrote) {
                // fallback: if write fails, skip this asset (or you could keep NSData in memory)
                continue
            }

            val fileUri = NSURL.fileURLWithPath(path).absoluteString ?: "file://$path"

            // Build MediaItem (adjust property names if your MediaItem differs)
            items += MediaItem(
                id = localId,
                displayName = displayName,
                uri = fileUri,
                mimeType = null,
                dateAddedSeconds = null,
                sizeBytes = null
            )
        }

        items
    }
}
