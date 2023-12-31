package dev.anvith.binanceninja.core.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.anvith.binanceninja.core.ui.theme.BinanceNinjaTheme
import dev.anvith.binanceninja.core.ui.theme.Dimens
import dev.anvith.binanceninja.core.ui.theme.ThemeColors

@Composable
fun PreviewBox(isDark: Boolean = true, child: @Composable () -> Unit) {
    BinanceNinjaTheme(useDarkTheme = isDark) {
        Box(Modifier.background(color = ThemeColors.background).padding(vertical = Dimens.keyline)) {
            child()
        }
    }
}