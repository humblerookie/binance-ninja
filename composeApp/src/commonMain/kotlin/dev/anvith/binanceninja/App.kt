package dev.anvith.binanceninja

import androidx.compose.runtime.Composable
import cafe.adriel.lyricist.ProvideStrings
import cafe.adriel.voyager.navigator.Navigator
import dev.anvith.binanceninja.core.ui.presentation.bind
import dev.anvith.binanceninja.core.ui.theme.BinanceNinjaTheme
import me.tatarka.inject.annotations.Inject

typealias App = @Composable () -> Unit

typealias AppPresenterFactory = () -> AppPresenter

@Inject
@Composable
fun App(factory: AppPresenterFactory) {
  BinanceNinjaTheme { ProvideStrings { Navigator(AppScreen().bind(factory)) } }
}
