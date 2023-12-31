package dev.anvith.binanceninja.core.ui.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp

@Composable
fun Space(width: Dp? = null, height: Dp? = null, modifier: Modifier=Modifier) {
    Spacer(
        modifier = modifier
            .width(width ?: Dp.Hairline)
            .height(height ?: Dp.Hairline)
    )
}