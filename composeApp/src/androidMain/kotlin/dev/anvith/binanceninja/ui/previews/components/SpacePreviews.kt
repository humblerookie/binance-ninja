package dev.anvith.binanceninja.ui.previews.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.anvith.binanceninja.core.ui.components.PreviewBox
import dev.anvith.binanceninja.core.ui.theme.AppText
import dev.anvith.binanceninja.core.ui.components.Space

class SpacePreviews {

    @Preview("Horizontal Space")
    @Composable
    private fun Preview1() {
        PreviewBox {
            Row {
                AppText.Body1(text = "Caption")
                Space(width = 30.dp)
                AppText.H1(text = "Hello World")
            }
        }
    }

    @Preview("Vertical Space")
    @Composable
    private fun Preview2() {
        PreviewBox {
            Column {
                AppText.Body1(text = "Caption")
                Space(height = 30.dp)
                AppText.H1(text = "Hello World")
            }
        }
    }
}