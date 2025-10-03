package com.louisgautier.gallery

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.louisgautier.permission.PermissionHelper
import com.louisgautier.permission.PermissionResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class GalleryViewModel(
    private val permissionHelper: PermissionHelper,
    private val loadLocalPictures: LoadLocalPictures
) : ViewModel() {

    sealed class GalleryState {
        data object NoPermission : GalleryState()
        data object NoPictures : GalleryState()
        data class PicturesLoaded(val pictures: List<MediaItem>) : GalleryState()
    }

    var state = MutableStateFlow<GalleryState>(GalleryState.NoPictures)
        private set

    init {
        askPermission()
    }

    fun askPermission() {
        viewModelScope.launch(Dispatchers.Main) {
            val result = permissionHelper.checkOrAskForPermission()
            if (result == PermissionResult.DENIED) {
                state.emit(GalleryState.NoPermission)
            } else {
                loadInitialPictures()
            }
        }
    }

    suspend fun loadInitialPictures() {
        val result = loadLocalPictures.loadPictures()
        if (result.isEmpty()) {
            state.emit(GalleryState.NoPictures)
        } else {
            state.emit(GalleryState.PicturesLoaded(result))
        }
    }

}