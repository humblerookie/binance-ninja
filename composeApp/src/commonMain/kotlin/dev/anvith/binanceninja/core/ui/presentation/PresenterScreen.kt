package dev.anvith.binanceninja.core.ui.presentation

import cafe.adriel.voyager.core.screen.Screen

interface PresenterScreen : Screen

inline fun <reified T : PresenterScreen> T.bind(factory: () -> BasePresenter<*, *>): T = apply {
  if (!BasePresenter.presenters.containsKey(key)) {
    factory().bind(this)
  }
}

inline fun <reified T> PresenterScreen.getPresenter(): T {
  return BasePresenter.presenters[key] as T
}
