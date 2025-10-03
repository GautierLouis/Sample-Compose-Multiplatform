package com.louisgautier.gallery

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Help
import androidx.compose.material.icons.filled.HourglassEmpty
import androidx.compose.material.icons.filled.Replay
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.louisgautier.designsystem.AppSize
import com.louisgautier.designsystem.AppTheme
import com.louisgautier.designsystem.theme.button.Button
import com.louisgautier.designsystem.theme.button.ButtonType
import com.louisgautier.designsystem.theme.button.ButtonVariant
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun GalleryHome(
    viewModel: GalleryViewModel = koinViewModel()
) {

    val state by viewModel.state.collectAsStateWithLifecycle()


    Scaffold { paddingValues ->

        when (state) {
            GalleryViewModel.GalleryState.NoPermission -> {
                GalleryNoPermission(paddingValues) { viewModel.askPermission() }
            }

            GalleryViewModel.GalleryState.NoPictures -> {
                GalleryHomeEmptyState(paddingValues)
            }

            is GalleryViewModel.GalleryState.PicturesLoaded -> {
                GalleryList(state as GalleryViewModel.GalleryState.PicturesLoaded, paddingValues)
            }
        }

    }
}

@Composable
@Preview
fun GalleryNoPermission(
    paddingValues: PaddingValues = PaddingValues(),
    onRetry: () -> Unit = {}
) {
    Box(
        modifier = Modifier.fillMaxSize()
            .padding(paddingValues)
            .background(AppTheme.colors.bg),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(AppSize.size16)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Default.Help,
                contentDescription = "empty state icon",
                modifier = Modifier.size(AppSize.size128),
                tint = AppTheme.colors.bgInverted
            )
            Text(
                text = "We need access to your pictures to display them",
                style = MaterialTheme.typography.titleLarge,
                color = AppTheme.colors.bgInverted,
                textAlign = TextAlign.Center
            )

            Button(
                variant = ButtonVariant.GHOST,
                type = ButtonType.ERROR,
                enabled = true,
                onClick = {
                    onRetry()
                },
            ) {
                Text(
                    text = "Retry",
                    color = AppTheme.colors.redFamily.solid,
                    modifier = Modifier.padding(end = AppSize.size8)
                )
                Icon(
                    imageVector = Icons.Default.Replay,
                    contentDescription = "retry to ask permission",
                    tint = AppTheme.colors.redFamily.solid,
                    modifier = Modifier.size(AppSize.size24)
                )
            }
        }
    }
}

@Composable
@Preview
fun GalleryHomeEmptyState(paddingValues: PaddingValues = PaddingValues()) {
    Box(
        modifier = Modifier.fillMaxSize()
            .padding(paddingValues)
            .background(AppTheme.colors.bg),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(AppSize.size16)
        ) {
            Icon(
                imageVector = Icons.Default.HourglassEmpty,
                contentDescription = "empty state icon",
                modifier = Modifier.size(AppSize.size128),
                tint = AppTheme.colors.bgInverted
            )
            Text(
                text = "No pictures found",
                style = MaterialTheme.typography.titleLarge,
                color = AppTheme.colors.bgInverted
            )
        }
    }
}

@Composable
fun GalleryList(
    state: GalleryViewModel.GalleryState.PicturesLoaded,
    paddingValues: PaddingValues
) {
    LazyColumn(
        modifier = Modifier.padding(paddingValues)
    ) {
        items(state.pictures.size) { index ->
            val item = state.pictures[index]
            ItemPicture(item)
        }
    }
}