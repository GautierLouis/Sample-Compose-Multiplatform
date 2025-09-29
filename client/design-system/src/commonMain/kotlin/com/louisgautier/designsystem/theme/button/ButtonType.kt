package com.louisgautier.designsystem.theme.button

import androidx.compose.runtime.Composable
import com.louisgautier.designsystem.AppTheme

enum class ButtonType {
    PRIMARY,
    SECONDARY,
    ERROR,
    WARNING,
    PREMIUM;

    @Composable
    fun getColorFamily() = when (this) {
        PRIMARY -> AppTheme.colors.grayFamily
        SECONDARY -> AppTheme.colors.tealFamily
        ERROR -> AppTheme.colors.redFamily
        WARNING -> AppTheme.colors.orangeFamily
        PREMIUM -> AppTheme.colors.yellowFamily
    }
}