package dev.anvith.binanceninja.core.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import cafe.adriel.lyricist.strings
import dev.anvith.binanceninja.core.ui.data.Constants.Assets
import dev.anvith.binanceninja.core.ui.theme.AppText
import dev.anvith.binanceninja.core.ui.theme.Dimens
import dev.anvith.binanceninja.core.ui.theme.ThemeColors
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource

@OptIn(ExperimentalResourceApi::class)
@Composable
fun ErrorSection(message: String, onRetry: () -> Unit) {
  Column(
    modifier = Modifier.fillMaxSize().padding(Dimens.keyline),
    verticalArrangement = Arrangement.Center,
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    Image(
      painterResource(Assets.EMPTY),
      null,
      contentScale = ContentScale.Inside,
      modifier = Modifier.size(Dimens.iconNormal),
      colorFilter = ColorFilter.tint(ThemeColors.onSurface)
    )
    Space(height = Dimens.spaceXLarge)
    AppText.Body1(
      text = message,
    )
    Space(height = Dimens.spaceLarge)
    PrimaryButton(
      label = strings.actionRetry,
      onClick = onRetry,
      modifier = Modifier.fillMaxWidth()
    )
  }
}
