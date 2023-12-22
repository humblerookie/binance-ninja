package dev.anvith.binanceninja.ui.previews.home

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import dev.anvith.binanceninja.core.ui.components.PreviewBox
import dev.anvith.binanceninja.features.ui.CreateFilterScreen

@Preview("Light Mode")
@Composable
private fun PreviewScreen1() {
    PreviewBox(isDark = false) {
        CreateFilterScreen.Content()
    }
}

@Preview("Dark Mode")
@Composable
private fun PreviewScreen2() {
    PreviewBox(isDark = true) {
        CreateFilterScreen.Content()
    }
}