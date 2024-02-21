package dev.anvith.binanceninja.core.ui.presentation

import cafe.adriel.voyager.core.lifecycle.JavaSerializable
import cafe.adriel.voyager.core.lifecycle.ScreenLifecycleOwner
import cafe.adriel.voyager.core.lifecycle.ScreenLifecycleProvider
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.screen.uniqueScreenKey
import cafe.adriel.voyager.navigator.tab.Tab

abstract class PresenterTab : Tab, ScreenLifecycleProvider, PresenterScreen {

  override val key = uniqueScreenKey

  private val lifecycleOwner = AppScreenLifecycleOwner()

  override fun getLifecycleOwner() = lifecycleOwner
}

abstract class LifecyclePresenterScreen : ScreenLifecycleProvider, PresenterScreen {

  override val key = uniqueScreenKey

  private val lifecycleOwner = AppScreenLifecycleOwner()

  override fun getLifecycleOwner() = lifecycleOwner
}

class AppScreenLifecycleOwner : ScreenLifecycleOwner, JavaSerializable {
  override fun onDispose(screen: Screen) {
    if (screen is PresenterScreen) {
      BasePresenter.removeBinding(screen.key)
    }
    super.onDispose(screen)
  }
}
