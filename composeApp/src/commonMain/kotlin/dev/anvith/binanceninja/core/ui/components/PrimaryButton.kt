package dev.anvith.binanceninja.core.ui.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.anvith.binanceninja.core.ui.theme.AppText
import dev.anvith.binanceninja.core.ui.theme.Dimens
import dev.anvith.binanceninja.core.ui.theme.TextModifier
import dev.anvith.binanceninja.core.ui.theme.ThemeColors

@Composable
fun PrimaryButton(
  label: String,
  onClick: () -> Unit,
  modifier: Modifier = Modifier,
) {
  ElevatedButton(
    modifier = modifier,
    onClick = onClick,
    colors =
      ButtonDefaults.elevatedButtonColors(
        containerColor = ThemeColors.primary,
        contentColor = ThemeColors.onPrimary
      ),
    shape = RoundedCornerShape(corner = CornerSize(Dimens.radiusButton)),
    contentPadding = PaddingValues(horizontal = Dimens.spaceNormal, vertical = Dimens.spaceLarge)
  ) {
    AppText.Button1(text = label.uppercase(), TextModifier.color(ThemeColors.onPrimary))
  }
}
