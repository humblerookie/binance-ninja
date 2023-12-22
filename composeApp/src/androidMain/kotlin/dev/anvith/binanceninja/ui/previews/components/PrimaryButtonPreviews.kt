package dev.anvith.binanceninja.ui.previews.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import dev.anvith.binanceninja.core.ui.components.PreviewBox
import dev.anvith.binanceninja.core.ui.components.PrimaryButton
import dev.anvith.binanceninja.core.ui.theme.Dimens

class PrimaryButtonPreviews {


    @Preview
    @Composable
    private fun PrimaryButtonPreview1() = PreviewBox {
        PrimaryButton(
            label = "Login",
            onClick = {},
            modifier = Modifier
                .padding(Dimens.keyline)
                .fillMaxWidth()
        )
    }

    @Preview
    @Composable
    private fun PrimaryButtonPreview2() = PreviewBox(isDark = false) {
        PrimaryButton(
            label = "Login",
            onClick = {},
            modifier = Modifier
                .padding(Dimens.keyline)
                .fillMaxWidth()
        )
    }
}