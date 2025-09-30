package com.louisgautier.designsystem.theme.button

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.louisgautier.designsystem.AppSize
import com.louisgautier.designsystem.AppTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun Button(
    modifier: Modifier = Modifier,
    variant: ButtonVariant,
    type: ButtonType,
    enabled: Boolean,
    onClick: () -> Unit,
    text: @Composable () -> Unit
) {

    val family = type.getColorFamily()

    Button(
        modifier = modifier,
        shape = RoundedCornerShape(AppSize.size4),
        onClick = { onClick() },
        enabled = enabled,
        border = BorderStroke(1.dp, variant.borderColor(family)),
        colors = ButtonDefaults.buttonColors(
            contentColor = variant.contentColor(family),
            containerColor = variant.containerColor(family),
            disabledContentColor = variant.disableContentColor(family),
            disabledContainerColor = variant.disableContainerColor(family)
        )
    ) {
        text()
    }
}

@Composable
@Preview(
    name = "Night mode buttons list",
    widthDp = 500,
    showBackground = true,
    backgroundColor = 0xFF000000,
)
fun PreviewNight_Button() {

    AppTheme(isDarkTheme = true) {
        LazyColumn {
            items(ButtonType.entries.size) {
                val type = ButtonType.entries[it]
                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    ButtonVariant.entries.forEach { variant ->
                        Box {
                            Test_PreviewButton(type, variant, true)
                        }
                    }
                }

                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    ButtonVariant.entries.forEach { variant ->
                        Box {
                            Test_PreviewButton(type, variant, false)
                        }
                    }
                }
            }
        }
    }
}

@Composable
@Preview(
    name = "Day mode buttons list",
    widthDp = 500,
    showBackground = true
)
fun PreviewDay_Button() {

    AppTheme(isDarkTheme = false) {
        LazyColumn {
            items(ButtonType.entries.size) {
                val type = ButtonType.entries[it]
                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    ButtonVariant.entries.forEach { variant ->
                        Box {
                            Test_PreviewButton(type, variant, true)
                        }
                    }
                }

                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    ButtonVariant.entries.forEach { variant ->
                        Box {
                            Test_PreviewButton(type, variant, false)
                        }
                    }
                }
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun Test_PreviewButton(
    type: ButtonType = ButtonType.PRIMARY,
    variant: ButtonVariant = ButtonVariant.SOLID,
    enabled: Boolean = true
) {
    Button(
        variant = variant,
        type = type,
        enabled = enabled,
        onClick = { },
    ) {
        Text(text = "Button")
    }
}