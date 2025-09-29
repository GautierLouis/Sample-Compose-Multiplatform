package com.louisgautier.designsystem

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import com.louisgautier.designsystem.theme.color.AppColors
import com.louisgautier.designsystem.theme.color.provideDayColors
import com.louisgautier.designsystem.theme.color.provideNightColors

@Composable
fun AppTheme(
    isDarkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val currentColors = remember(isDarkTheme) {
        if (isDarkTheme) provideNightColors() else provideDayColors()
    }

    CompositionLocalProvider(
        LocalAppColors provides currentColors,
    ) {
        content()
    }
}

object AppTheme {
    val colors: AppColors
        @Composable
        get() = LocalAppColors.current
}

val LocalAppColors = staticCompositionLocalOf { provideDayColors() }