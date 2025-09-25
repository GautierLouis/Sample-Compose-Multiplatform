package com.louisgautier.composeApp.design.molecule

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.louisgautier.composeApp.design.atom.MultiLocalePreview
import org.jetbrains.compose.resources.stringResource
import sample.client.composeapp.generated.resources.Res
import sample.client.composeapp.generated.resources.greeting

@Composable
fun ActionButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    content: @Composable RowScope.() -> Unit,
) {

    Button(
        onClick = onClick,
        modifier = modifier,
        content = content
    )

}


@Composable
@MultiLocalePreview()
fun Preview_ActionButton() {

    ActionButton(
        onClick = {},
        content = { Text(stringResource(Res.string.greeting)) }
    )

}

