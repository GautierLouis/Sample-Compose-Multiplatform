package com.louisgautier.gallery

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.layout.ContentScale
import coil3.request.ErrorResult
import coil3.request.ImageRequest
import com.louisgautier.designsystem.AppSize
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.coil3.CoilImage
import kotlinx.cinterop.ByteVar
import kotlinx.cinterop.CPointer
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.usePinned
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import platform.Foundation.NSData
import platform.Foundation.NSError
import platform.Foundation.NSMutableData
import platform.Foundation.appendData
import platform.Foundation.getBytes
import platform.Photos.PHAsset
import platform.Photos.PHAssetResource
import platform.Photos.PHAssetResourceManager
import platform.Photos.PHAssetResourceRequestOptions
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

@Composable
actual fun ItemPicture(
    item: MediaItem
) {
    var nsData by remember { mutableStateOf<NSData?>(null) }
    var byteArray by remember { mutableStateOf<ByteArray?>(null) }
    var loadError by remember { mutableStateOf<String?>(null) }

    val imageOption = ImageOptions(
        contentScale = ContentScale.Crop,
        alignment = Alignment.Center
    )

    val requestListener = object : ImageRequest.Listener {
        override fun onError(request: ImageRequest, result: ErrorResult) {
            super.onError(request, result)
            println("LOAD ERROR ${result.throwable.message}")
        }
    }

    LaunchedEffect(item) {
        try {
            val data = fetchAssetFromUri(item.id)
            nsData = data

            // If your loader accepts NSData directly you can skip conversion.
            // Otherwise convert to ByteArray for loaders that accept raw bytes.
            if (data != null) {
                // convert off main if heavy
                byteArray = withContext(Dispatchers.Default) {
                    data.toByteArray()
                }
            }
        } catch (t: Throwable) {
            loadError = t.message
        }
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(AppSize.size16)
    ) {
        Text(text = item.displayName.orEmpty())
        Text(text = item.uri)

        when {
            byteArray != null -> {
                // Pass ByteArray to your image loader
                CoilImage(
                    imageModel = { byteArray!! },
                    imageOptions = imageOption,
                    requestListener = { requestListener }
                )
            }

            nsData != null -> {
                CoilImage(
                    imageModel = { nsData!! },
                    imageOptions = imageOption,
                    requestListener = { requestListener }
                )
            }

            loadError != null -> {
                Text(text = "Error loading image: $loadError")
            }
        }
    }
}

suspend fun fetchAssetFromUri(uri: String): NSData? {

    val id = uri.removePrefix("ph://")
    val fetchResult = PHAsset.fetchAssetsWithLocalIdentifiers(listOf(id), null)
    val asset = fetchResult.firstObject as? PHAsset
        ?: throw IllegalStateException("$uri is not a valid asset")

    // Get asset resources (there may be multiple resources: original, adjustment, etc.)
    val resources = PHAssetResource.assetResourcesForAsset(asset)
    if (resources.count() == 0) {
        return null
    }

    // Prefer the "photo" or "full size" resource. We'll pick the first resource by default,
    // but you can scan `resources` for specific types (video, photo, paired, etc.).
    val resource = resources[0] as? PHAssetResource ?: return null


    val options = PHAssetResourceRequestOptions().apply {
        networkAccessAllowed = true
    }

    return withContext(Dispatchers.Default) {
        suspendCoroutine { cont ->
            try {
                val manager = PHAssetResourceManager.defaultManager()
                val accumulator = NSMutableData()
                manager.requestDataForAssetResource(
                    resource,
                    options,
                    { chunk: NSData? ->
                        // dataReceivedHandler: append chunks to accumulator
                        if (chunk != null) {
                            accumulator.appendData(chunk)
                        }
                    },
                    { error: NSError? ->
                        // completionHandler: called when finished or when error occurred
                        if (error != null) {
                            cont.resumeWithException(RuntimeException("PHAssetResourceManager error: ${error.localizedDescription}"))
                        } else {
                            cont.resume(accumulator)
                        }
                    }
                )
            } catch (t: Throwable) {
                cont.resumeWithException(t)
            }
        }
    }
}


@OptIn(ExperimentalForeignApi::class)
fun NSData.toByteArray(): ByteArray {
    val len = this.length.toInt()
    if (len == 0) return ByteArray(0)

    val result = ByteArray(len)
    // getBytes expects a pointer to a buffer
    memScoped {
        // pinned array to get stable pointer to write into
        result.usePinned { pinned ->
            val destPtr: CPointer<ByteVar> = pinned.addressOf(0)
            // NSData.getBytes expects platform pointer and length (NSUInteger)
            this@toByteArray.getBytes(destPtr, this@toByteArray.length)
        }
    }
    return result
}


