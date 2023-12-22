package dev.anvith.binanceninja.ui.previews.components

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import cafe.adriel.lyricist.strings
import dev.anvith.binanceninja.core.ui.components.AppSwitch
import dev.anvith.binanceninja.core.ui.components.PreviewBox
import dev.anvith.binanceninja.core.ui.theme.AppThemeColors


@Preview("Light Mode: Checked")
@Composable
private fun CheckBox1() = PreviewBox(isDark = false) {
    AppSwitch(
        onLabel = strings.actionBuy,
        offLabel = strings.actionSell,
        onColor = AppThemeColors.onColor,
        offColor = AppThemeColors.offColor,
        isChecked = true,
        modifier = Modifier.padding(30.dp)
    )
}

@Preview("Light Mode: Unchecked")
@Composable
private fun CheckBox2() = PreviewBox(isDark = false) {
    AppSwitch(
        onLabel = strings.actionBuy,
        offLabel = strings.actionSell,
        onColor = AppThemeColors.onColor,
        offColor = AppThemeColors.offColor,
        isChecked = false,
        modifier = Modifier.padding(30.dp)
    )
}

@Preview("Dark Mode: Checked")
@Composable
private fun CheckBox3() = PreviewBox {
    AppSwitch(
        onLabel = strings.actionBuy,
        offLabel = strings.actionSell,
        onColor = AppThemeColors.onColor,
        offColor = AppThemeColors.offColor,
        isChecked = true,
        modifier = Modifier.padding(30.dp)
    )
}

@Preview("Dark Mode: Unchecked")
@Composable
private fun CheckBox4() = PreviewBox {
    AppSwitch(
        onLabel = strings.actionBuy,
        offLabel = strings.actionSell,
        onColor = AppThemeColors.onColor,
        offColor = AppThemeColors.offColor,
        isChecked = false,
        modifier = Modifier.padding(30.dp)
    )
}
