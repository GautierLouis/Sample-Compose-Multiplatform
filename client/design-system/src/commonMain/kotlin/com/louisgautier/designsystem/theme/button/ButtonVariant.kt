package com.louisgautier.designsystem.theme.button

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.louisgautier.designsystem.theme.color.ColorFamily

enum class ButtonVariant {
    SOLID,
    SUBTLE,
    OUTLINE,
    GHOST;

    //Text and Icon color
    @Composable
    fun contentColor(family: ColorFamily) = when (this) {
        SOLID -> family.contrast
        else -> family.fg
    }

    //Text and Icon color
    @Composable
    fun disableContentColor(family: ColorFamily) = when (this) {
        SOLID -> family.contrast.copy(alpha = .6f)
        else -> family.fg.copy(alpha = .2f)
    }

    //Background
    @Composable
    fun containerColor(family: ColorFamily) = when (this) {
        SOLID -> family.solid
        OUTLINE, GHOST -> Color.Transparent
        SUBTLE -> family.subtle
    }

    //Background
    @Composable
    fun disableContainerColor(family: ColorFamily) = when (this) {
        SOLID -> family.solid.copy(alpha = 0.1f)
        OUTLINE, GHOST -> Color.Transparent
        SUBTLE -> family.subtle.copy(alpha = .9f)
    }

    @Composable
    fun borderColor(family: ColorFamily) = when (this) {
        OUTLINE -> family.muted
        else -> Color.Transparent
    }
}