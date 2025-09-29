package com.louisgautier.designsystem.theme.color

import androidx.compose.ui.graphics.Color

data class AppColors(
    // Background
    val bg: Color,
    val bgSubtle: Color,
    val bgMuted: Color,
    val bgEmphasized: Color,
    val bgInverted: Color,
    val bgPanel: Color,
    val bgError: Color,
    val bgWarning: Color,
    val bgSuccess: Color,
    val bgInfo: Color,

    // Border
    val border: Color,
    val borderMuted: Color,
    val borderSubtle: Color,
    val borderEmphasized: Color,
    val borderInverted: Color,
    val borderError: Color,
    val borderWarning: Color,
    val borderSuccess: Color,
    val borderInfo: Color,

    // Text / Foreground
    val fg: Color,
    val fgMuted: Color,
    val fgSubtle: Color,
    val fgInverted: Color,
    val fgError: Color,
    val fgWarning: Color,
    val fgSuccess: Color,
    val fgInfo: Color,


    val grayFamily: ColorFamily,
    val redFamily: ColorFamily,
    val pinkFamily: ColorFamily,
    val purpleFamily: ColorFamily,
    val cyanFamily: ColorFamily,
    val blueFamily: ColorFamily,
    val tealFamily: ColorFamily,
    val greenFamily: ColorFamily,
    val yellowFamily: ColorFamily,
    val orangeFamily: ColorFamily,

    )