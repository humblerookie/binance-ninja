package dev.anvith.binanceninja

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult.ActionPerformed
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextDecoration
import cafe.adriel.lyricist.strings
import cafe.adriel.voyager.navigator.tab.CurrentTab
import cafe.adriel.voyager.navigator.tab.LocalTabNavigator
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabNavigator
import dev.anvith.binanceninja.AppContract.Event.Retry
import dev.anvith.binanceninja.AppContract.Event.SelectCurrency
import dev.anvith.binanceninja.core.res.getLocaleStrings
import dev.anvith.binanceninja.core.ui.components.LocalSnackbarProvider
import dev.anvith.binanceninja.core.ui.components.Space
import dev.anvith.binanceninja.core.ui.components.getSnackbar
import dev.anvith.binanceninja.core.ui.data.IList
import dev.anvith.binanceninja.core.ui.presentation.LifecyclePresenterScreen
import dev.anvith.binanceninja.core.ui.presentation.getPresenter
import dev.anvith.binanceninja.core.ui.theme.AppText
import dev.anvith.binanceninja.core.ui.theme.Dimens
import dev.anvith.binanceninja.core.ui.theme.TextModifier
import dev.anvith.binanceninja.core.ui.theme.ThemeColors
import dev.anvith.binanceninja.domain.models.CurrencyModel
import dev.anvith.binanceninja.features.ui.core.components.CurrencyDialog

class AppScreen : LifecyclePresenterScreen() {
  @Composable
  override fun Content() {
    val presenter: AppPresenter = getPresenter()
    val state by presenter.state.collectAsState()
    TabNavigator(presenter.getChildren().first()) {
      val snackbarHostState = remember { SnackbarHostState() }
      val scope = rememberCoroutineScope()
      val provider by remember { mutableStateOf(getSnackbar(scope, snackbarHostState)) }

      LaunchedEffect(state.errorMessage) {
        state.errorMessage?.let { error ->
          provider.showSnack(error, getLocaleStrings().actionRetry, false) {
            if (it == ActionPerformed) {
              presenter.dispatchEvent(Retry)
            }
          }
        }
      }
      CompositionLocalProvider(LocalSnackbarProvider provides provider) {
        Scaffold(
          snackbarHost = { SnackbarHost(snackbarHostState) },
          content = { Box(modifier = Modifier.padding(it)) { CurrentTab() } },
          topBar = {
            AppBar(
              currencies = state.currencies,
              selectedCurrency = state.userCurrency,
              isLoading = state.isLoading,
              onCurrencySelected = { presenter.dispatchEvent(SelectCurrency(it)) },
              onRetry = { presenter.dispatchEvent(Retry) }
            )
          },
          bottomBar = { NavigationBar { presenter.getChildren().map { TabNavigationItem(it) } } },
        )
      }
    }
  }

  @Composable
  private fun RowScope.TabNavigationItem(tab: Tab) {
    val tabNavigator = LocalTabNavigator.current
    NavigationBarItem(
      selected = tabNavigator.current == tab,
      onClick = { tabNavigator.current = tab },
      icon = {
        Icon(
          painter = tab.options.icon!!,
          contentDescription = tab.options.title,
        )
      }
    )
  }

  @OptIn(ExperimentalMaterial3Api::class)
  @Composable
  private fun AppBar(
    currencies: IList<CurrencyModel>,
    selectedCurrency: CurrencyModel?,
    isLoading: Boolean,
    onCurrencySelected: (CurrencyModel) -> Unit,
    onRetry: () -> Unit,
  ) {
    var expanded by rememberSaveable { mutableStateOf(false) }
    TopAppBar(
      title = {
        AppText.H3(text = strings.appName, textModifier = TextModifier.color(ThemeColors.onPrimary))
      },
      colors = TopAppBarDefaults.topAppBarColors(containerColor = ThemeColors.primary),
      actions = {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          modifier = Modifier.clickable { expanded = true }.padding(horizontal = Dimens.keyline)
        ) {
          val color = ThemeColors.onPrimary
          Icon(
            Icons.Outlined.Edit,
            tint = color,
            modifier = Modifier.size(Dimens.iconMicro),
            contentDescription = strings.changeFiat
          )
          Space(width = Dimens.spaceMin)
          AppText.Body1(
            text = selectedCurrency?.symbol.orEmpty(),
            textModifier =
              TextModifier.color(color)
                .size(Dimens.textXLarge)
                .decoration(TextDecoration.Underline),
          )
        }
      }
    )
    if (expanded) {
      CurrencyDialog(
        currencies,
        selectedCurrency,
        onClick = onCurrencySelected,
        onDismiss = { expanded = false },
        onRetry = onRetry,
        isLoading = isLoading,
      )
    }
  }
}
