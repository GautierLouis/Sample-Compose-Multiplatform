package com.louisgautier.composeApp.design.atom

import org.jetbrains.compose.ui.tooling.preview.Preview

@Retention(AnnotationRetention.BINARY)
@Target(AnnotationTarget.ANNOTATION_CLASS, AnnotationTarget.FUNCTION)
@Preview(locale = "en", name = "English", group = "MultiLocalePreview")
@Preview(locale = "de", name = "German", group = "MultiLocalePreview")
@Preview(locale = "es", name = "Spanish", group = "MultiLocalePreview")
@Preview(locale = "fr", name = "French", group = "MultiLocalePreview")
@Preview(locale = "zh", name = "Chinese", group = "MultiLocalePreview")
@Preview(locale = "ja", name = "Japanese", group = "MultiLocalePreview")
annotation class MultiLocalePreview()