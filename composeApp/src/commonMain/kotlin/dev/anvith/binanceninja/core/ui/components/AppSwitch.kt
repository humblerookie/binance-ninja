package dev.anvith.binanceninja.core.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import dev.anvith.binanceninja.core.ui.theme.AppText
import dev.anvith.binanceninja.core.ui.theme.AppThemeColors
import dev.anvith.binanceninja.core.ui.theme.Dimens

@Composable
fun AppSwitch(
    onLabel: String,
    offLabel: String,
    isChecked: Boolean,
    modifier: Modifier = Modifier,
    offColor: Color = AppThemeColors.offColor,
    onColor: Color = AppThemeColors.onColor,
    onCheckChanged: (isChecked: Boolean) -> Unit = {},
) {
    Row(modifier, verticalAlignment = Alignment.CenterVertically) {
        AppText.H4(text = offLabel.uppercase())
        Switch(
            checked = isChecked,
            modifier = Modifier.padding(horizontal = Dimens.spaceNormal),
            colors = SwitchDefaults.colors(
                checkedThumbColor = onColor,
                checkedBorderColor = onColor.copy(alpha = 0.8f),
                checkedTrackColor = onColor.copy(alpha = 0.2f),
                checkedIconColor = onColor.copy(alpha = 0.5f),
                uncheckedThumbColor = offColor,
                uncheckedBorderColor = offColor.copy(alpha = 0.8f),
                uncheckedTrackColor = offColor.copy(alpha = 0.2f),
            ),
            onCheckedChange = onCheckChanged
        )
        AppText.H4(text = onLabel.uppercase())
    }
}